package org.haldokan.edge.interviewquest.google.imageserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by haytham.aldokanji on 8/28/15.
 */
public class ReqParkSpot {
    private CountDownLatch signal;
    private Future<Image> image;

    // we can pass the latch count as param for a more general sln
    public ReqParkSpot() {
        this.signal = new CountDownLatch(1);
    }

    // throwing these exceptions does not make sense. In commercial code proper handling of exceptions is needed
    public Image getImage() throws ExecutionException, InterruptedException {
        //in case the image is loaded even before we call await
        if (image != null)
            return image.get();

        for (; ; ) {
            try {
                signal.await();
                return image.get();
            } catch (InterruptedException e) {
            }
        }
    }

    public void notify(Future<Image> image) {
        this.image = image;
        this.signal.countDown();
    }
}
