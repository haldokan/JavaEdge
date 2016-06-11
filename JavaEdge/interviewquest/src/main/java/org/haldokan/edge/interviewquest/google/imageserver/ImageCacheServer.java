package org.haldokan.edge.interviewquest.google.imageserver;

import com.google.common.collect.*;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * My solution to a Google interview question
 * The Question: 5_STAR
 * Design (I actullay implemented) an image server that can serve requests concurrently and
 * keep only the N most recently served images in the cache.
 *
 * Created by haytham.aldokanji on 8/28/15.ß
 */
//TODO SHOULD handle the case when futures stored in the map throw exceptions
public class ImageCacheServer {
    private static String[] TAGS = new String[]{"funny", "cats", "aboriginal", "art", "aztec", "war"};
    private final int maxCacheSize;
    private final ExecutorService execService;
    private final Map<String, ImageContainer> cache = new ConcurrentHashMap<>();
    private final Multimap<String, ReqParkSpot> imageUploadSubscribers = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private final BlockingQueue<String> pendingUploads = new LinkedBlockingDeque<>();
    private final Multiset<String> uploadingAndUploadedImages = ConcurrentHashMultiset.create();
    private final LinkedHashSet<ImageAccess> imageAccessChain = new LinkedHashSet<>();
    private Random rand = new Random();

    // skipping params validation for brevity
    public ImageCacheServer(int concurLevel, int maxCacheSize) {
        this.execService = Executors.newFixedThreadPool(concurLevel);
        this.maxCacheSize = maxCacheSize;
    }

    public void startup() {
        execService.submit(this::runImageUploader);
        execService.submit(this::runCacheEntryEvicter);
        runForever();
    }

    /**
     * Get and image based on its id. Image may have to be loaded from persistent storage.
     *
     * @param imageId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Image getImage(String imageId) throws ExecutionException, InterruptedException {
        ImageContainer imageContainer = cache.get(imageId);
        if (imageContainer != null) {
            LocalTime accessTime = LocalTime.now();
            imageContainer.setAccessedAt(accessTime);
            // we are using the same executor to do cache maintenance; perhaps a different one would be better
            execService.submit(() -> updateImageAccessChain(imageId, accessTime));
            return imageContainer.getImage();
        }
        // upload image from storage. We park the requesting threads while loading is going on
        ReqParkSpot parkedReq = new ReqParkSpot();
        // update the subscription list for the images being loaded
        imageUploadSubscribers.put(imageId, parkedReq);
        // images to upload
        pendingUploads.add(imageId);
        // parked thread will return the image once the future image upload finishes: check ReqParkSpot
        return parkedReq.getImage();
    }

    public void runForever() {
        while (true) {
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Uploader reads pending image uploads from a queue and update the cache with future images. Queue may have multiple
     * requests to upload the same image that have come from different requesting threads but the uploader keeps track
     * of the images that are already loaded.
     */
    private void runImageUploader() {
        while (true) {
            // while loop to handle thread blocking interruptions: go back to blocking
            while (true) {
                try {
                    String imageId = pendingUploads.take();
                    // if not already uploaded
                    if (!uploadingAndUploadedImages.contains(imageId)) {
                        uploadingAndUploadedImages.add(imageId);
                        LocalTime accessTime = LocalTime.now();
                        // upload images in future: no waiting for upload to finish. Parked client threads will take care of that
                        ImageContainer imageContainer = new ImageContainer(execService.submit(() -> uploadImageFromStorage(imageId)),
                                accessTime);
                        // store the future in the cache
                        cache.put(imageId, imageContainer);
                        // update image access chain to cache size maintainer can clubber the old images
                        updateImageAccessChain(imageId, accessTime);
                        // notify subscribers to this image upload so they can all the futures and get the image once upload is done
                        notifyUploadSubscribers(imageId, imageContainer);
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Evict images based on their access time trimming the cache size to the specified size. Note that cache maintenance
     * is done concurrently with cache access: it is not a stop-the-world sweep/delete scenario
     */
    private void runCacheEntryEvicter() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // TODO frequency of eviction should be passed as param
        scheduler.scheduleWithFixedDelay(() -> {
            if (imageAccessChain.size() > maxCacheSize) {
                synchronized (cache) {
                    // iterate over the linked hash set removing elements from the cache till reaching the specified size
                    for (Iterator<ImageAccess> itr = imageAccessChain.iterator(); itr.hasNext(); ) {
                        ImageAccess imageAccess = itr.next();
                        itr.remove();
                        System.out.println("Evict ->" + imageAccess + " / cache size " + cache.size());
                        ImageContainer imageContainer = cache.get(imageAccess.getImageId());
                        // compare access time to make sure the image was not access in the meanwhile
                        if (imageAccess.getAccessTime() == imageContainer.getAccessedAt()) {
                            String imageId = imageContainer.getImage().getId();
                            cache.remove(imageId);
                            uploadingAndUploadedImages.remove(imageId);
                        }

                        if (imageAccessChain.size() <= maxCacheSize)
                            break;
                    }
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    //simulate upload from persistent storage
    private Image uploadImageFromStorage(String imageId) {
        delay(2000);
        Image image = new Image(imageId, TAGS[rand.nextInt(TAGS.length)], "image taken " + rand.nextInt(360) + " days ago");
        return image;
    }

    private void notifyUploadSubscribers(String imageId, ImageContainer image) {
        Collection<ReqParkSpot> parkedThreads = imageUploadSubscribers.get(imageId);
        for (ReqParkSpot parkedThread : parkedThreads) {
            parkedThread.notify(image);
        }
    }

    /**
     * Update image access chain replacing old entries with most recent ones. Note how we avoid using and expensive sorted
     * data structure: added images are sorted naturally since access time increased at each access
     * @param imageId
     * @param accessTime
     */
    private void updateImageAccessChain(String imageId, LocalTime accessTime) {
        ImageAccess imageAccess = new ImageAccess(imageId, accessTime);
        // has to be synchronized but that's ok since cache ops don't wait on updating the chain.
        synchronized (cache) {
            imageAccessChain.remove(imageAccess);
            imageAccessChain.add(imageAccess);
        }
    }

    private void delay(long msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
