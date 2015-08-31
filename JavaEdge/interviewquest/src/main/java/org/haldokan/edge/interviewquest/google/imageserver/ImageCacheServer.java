package org.haldokan.edge.interviewquest.google.imageserver;

import com.google.common.collect.*;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Design (I actullay implemented) an image server that can serve requests concurrently and
 * (TODO) keep the N most recently served images in the cache.
 * <p>
 * My solution to a Google interview question
 * <p>
 * Created by haytham.aldokanji on 8/28/15.ß
 */
//TODO support keeping in cache only the N last accessed images: check LinkedHashMap.removeEldestEntry
//TODO SHOULD handle the case when futures stored in the map throw exceptions
public class ImageCacheServer {
    private static String[] TAGS = new String[]{"funny", "cats", "aboriginal", "art", "aztec", "war"};
    private final int maxCacheSize;
    private Random rand = new Random();

    private ExecutorService execService;
    private Map<String, Future<Image>> cache = new ConcurrentHashMap<>();
    private Multimap<String, ReqParkSpot> imageUploadSubscribers = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private BlockingQueue<String> pendingUploads = new LinkedBlockingDeque<>();
    private Multiset<String> uploadingImages = ConcurrentHashMultiset.create();

    // skipping params validation
    public ImageCacheServer(int concurLevel, int maxCacheSize) {
        this.execService = Executors.newFixedThreadPool(concurLevel);
        this.maxCacheSize = maxCacheSize;
    }

    public void startup() {
        execService.submit(this::runImageUploader);
        runForever();
    }

    public Image getImage(String imageId) throws ExecutionException, InterruptedException {
        Future<Image> image = cache.get(imageId);
        if (image != null) {
            return image.get();
        }
        ReqParkSpot parkedReq = new ReqParkSpot();
        imageUploadSubscribers.put(imageId, parkedReq);
        pendingUploads.add(imageId);
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

    private void runImageUploader() {
        while (true) {
            while (true) {
                try {
                    String imageId = pendingUploads.take();
                    if (!uploadingImages.contains(imageId)) {
                        uploadingImages.add(imageId);
                        Future<Image> loadingImage = execService.submit(() -> uploadImageFromStorage(imageId));
                        cache.put(imageId, loadingImage);
                        notifyUploadSubscribers(imageId, loadingImage);
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //simulate upload from persistent storage
    private Image uploadImageFromStorage(String imageId) {
        delay(2000);
        Image image = new Image(imageId, TAGS[rand.nextInt(TAGS.length)], "image taken " + rand.nextInt(360) + " days ago");
        image.setLoadedAt(LocalTime.now());
        return image;
    }

    private void notifyUploadSubscribers(String imageId, Future<Image> image) {
        Collection<ReqParkSpot> parkedThreads = imageUploadSubscribers.get(imageId);
        for (ReqParkSpot parkedThread : parkedThreads) {
            parkedThread.notify(image);
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
