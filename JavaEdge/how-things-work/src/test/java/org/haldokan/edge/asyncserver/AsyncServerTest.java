package org.haldokan.edge.asyncserver;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class AsyncServerTest {
    private static String sampleFile = "sample.txt";

    // A valid test should have a known sample file and assert the values
    // This is just a driver to run the server and post some requests
    @Test
    public void testWordcount() throws InterruptedException, ExecutionException {
        AsyncServer server = new AsyncServer();
        Thread thrd = new Thread(() -> {
            server.startup();
        });
        thrd.start();
        Thread.sleep(500L);
        // fire a few requests in separate threads
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    System.out.println(server.wordcount(sampleFile));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        // have to wait bcz the response does not wait the persistence which has
        // to complete in order to get the service time
        Thread.sleep(500L);
        System.out.println("wc avg time " + server.getServiceAverageTime(ServiceName.WORD_COUNT));
        server.shutdown();
        // we don't accept new requests
        Thread.sleep(100);
        System.out.println(server.wordcount(sampleFile));
        server.shutdownAll();
        thrd.join();
    }
}