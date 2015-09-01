package org.haldokan.edge.interviewquest.google.imageserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
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
    public Image getImageContainer() throws ExecutionException, InterruptedException {
        //in case the imageContainer is loaded even before we call await
        if (imageContainer != null)
            return imageContainer.getImage();

        for (; ; ) {
            try {
                signal.await();
                return imageContainer.getImage();
            } catch (InterruptedException e) {
            }
        }
    }

    public void notify(ImageContainer imageContainer) {
        this.imageContainer = imageContainer;
        this.signal.countDown();
    }
}
