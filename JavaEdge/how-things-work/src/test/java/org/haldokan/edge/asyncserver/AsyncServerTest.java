package org.haldokan.edge.asyncserver;

import java.util.concurrent.ExecutionException;

import org.haldokan.edge.asyncserver.AsyncServer;
import org.junit.Test;

public class AsyncServerTest {
    private static String sampleFile = "sample.txt";

    @Test
    public void testWordcount() throws InterruptedException, ExecutionException {
	AsyncServer server = new AsyncServer();
	Thread thrd = new Thread(new Runnable() {
	    @Override
	    public void run() {
		server.startup();
	    }
	});
	thrd.start();
	Thread.sleep(2000L);
	System.out.println(server.wordcount(sampleFile));
	// thrd.join();
    }
}