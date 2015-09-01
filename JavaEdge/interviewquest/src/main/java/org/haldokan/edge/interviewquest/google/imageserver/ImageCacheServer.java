package org.haldokan.edge.interviewquest.google.imageserver;

import com.google.common.collect.*;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * Design (I actullay implemented) an image server that can serve requests concurrently and
 * keep only the N most recently served images in the cache.
 * <p>
 * My solution to a Google interview question
 * <p>
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
    private final Multiset<String> uploadingImages = ConcurrentHashMultiset.create();
    private final LinkedHashSet<ImageAccess> imageAccessChain = new LinkedHashSet<>();
    private Random rand = new Random();

    // skipping params validation
    public ImageCacheServer(int concurLevel, int maxCacheSize) {
        this.execService = Executors.newFixedThreadPool(concurLevel);
        this.maxCacheSize = maxCacheSize;
    }

    public void startup() {
        execService.submit(this::runImageUploader);
        execService.submit(this::runCacheEntryEvicter);
        runForever();
    }

    public Image getImage(String imageId) throws ExecutionException, InterruptedException {
        ImageContainer imageContainer = cache.get(imageId);
        if (imageContainer != null) {
            LocalTime accessTime = LocalTime.now();
            imageContainer.setAccessedAt(accessTime);
            // we are usning the same executor to do cache maintenance; perhaps a different one would be better
            execService.submit(() -> updateImageAccessChain(imageId, accessTime));
            return imageContainer.getImage();
        }
        // upload
        ReqParkSpot parkedReq = new ReqParkSpot();
        imageUploadSubscribers.put(imageId, parkedReq);
        pendingUploads.add(imageId);
        return parkedReq.getImageContainer();
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

    private void runImageUploader() {
        while (true) {
            while (true) {
                try {
                    String imageId = pendingUploads.take();
                    if (!uploadingImages.contains(imageId)) {
                        uploadingImages.add(imageId);
                        LocalTime accessTime = LocalTime.now();
                        ImageContainer imageContainer = new ImageContainer(execService.submit(() -> uploadImageFromStorage(imageId)),
                                accessTime);
                        cache.put(imageId, imageContainer);
                        updateImageAccessChain(imageId, accessTime);
                        notifyUploadSubscribers(imageId, imageContainer);
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runCacheEntryEvicter() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // TODO frequency of eviction should be passed as param
        scheduler.scheduleWithFixedDelay(() -> {
            if (imageAccessChain.size() > maxCacheSize) {
                synchronized (cache) {
                    for (Iterator<ImageAccess> itr = imageAccessChain.iterator(); itr.hasNext(); ) {
                        ImageAccess imageAccess = itr.next();
                        itr.remove();
                        System.out.println("Evict ->" + imageAccess + " / cache size " + cache.size());
                        ImageContainer imageContainer = cache.get(imageAccess.getImageId());

                        if (imageAccess.getAccessTime() == imageContainer.getAccessedAt())
                            cache.remove(imageContainer.getImage().getId());

                        if (imageAccessChain.size() <= maxCacheSize)
                            break;
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
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

    // has to be synchronized but that's ok since cache ops don't wait on updating the chain
    private void updateImageAccessChain(String imageId, LocalTime accessTime) {
        ImageAccess imageAccess = new ImageAccess(imageId, accessTime);
        // remove if exist (old access time)
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
