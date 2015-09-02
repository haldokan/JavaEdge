package org.haldokan.edge.interviewquest.google.imageserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Park the requesting thread until images become available in the cache. This happens when the requested image is a cache
 * miss and has to be loaded form storage
 *
 * Created by haytham.aldokanji on 8/28/15.
 */
public class ReqParkSpot {
    private CountDownLatch signal;
    private ImageContainer imageContainer;

    // we can pass the latch count as param for a more general sln
    public ReqParkSpot() {
        this.signal = new CountDownLatch(1);
    }

    // throwing these exceptions does not make sense. In commercial code proper handling of exceptions is needed
    public Image getImage() throws ExecutionException, InterruptedException {
        //in case the imageContainer is loaded even before we call await
        if (imageContainer != null)
            return imageContainer.getImage();

        for (; ; ) {
            try {
                signal.await();
                // will wait to the loading future to complete
                return imageContainer.getImage();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Notify parked thread that a future upload is in progress so they can get the image once upload is completed
     *
     * @param imageContainer
     */
    public void notify(ImageContainer imageContainer) {
        this.imageContainer = imageContainer;
        this.signal.countDown();
    }
}
