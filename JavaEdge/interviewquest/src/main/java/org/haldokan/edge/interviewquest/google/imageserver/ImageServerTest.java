package org.haldokan.edge.interviewquest.google.imageserver;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by haytham.aldokanji on 8/28/15.
 */
public class ImageServerTest {
    private static Random rand = new Random();

    @Test
    public void tesImageServer() throws InterruptedException, ExecutionException {
        ImageCacheServer server = new ImageCacheServer(5, 30);
        Thread thrd = new Thread(() -> {
            server.startup();
        });
        thrd.start();

        // fire requests in separate threads
        for (int i = 0; i < 100; i++) {
            final int ndx = i;
            new Thread(() -> {
                try {
                    // it is a cache miss; time of the print stmt is earlier by the amount of image load time
                    System.out.println(LocalTime.now() + ">" + server.getImage("image-" + ndx));
                    // it is a cache hit; time of the print stmt shoud be almost equal to the image load time
                    System.out.println(LocalTime.now() + ">>" + server.getImage("image-" + ndx));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        thrd.join();
    }
}
