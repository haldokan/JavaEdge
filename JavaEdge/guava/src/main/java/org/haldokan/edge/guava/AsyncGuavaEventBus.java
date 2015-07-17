package org.haldokan.edge.guava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

/**
 * When post is called, all registered subscribers for an event are run in sequence, so subscribers should be reasonably
 * quick. If an event may trigger an extended process (such as a database load), spawn a thread or queue it for later.
 * (For a convenient way to do this, use an AsyncEventBus.)
 * 
 * @author haldokan
 *
 */
public class AsyncGuavaEventBus {
    private static AsyncEventBus aeb;

    public static void main(String[] args) {
	ExecutorService executor = Executors.newFixedThreadPool(5);
	aeb = new AsyncEventBus("eb", executor);
	aeb.register(new Sub1());
	aeb.register(new Sub2());

	for (int i = 0; i < 10; i++) {
	    aeb.post(i);
	    aeb.post("s" + i);
	}
	executor.shutdown();
    }

    private static class Sub1 {
	@Subscribe
	public void sub(String s) throws InterruptedException {
	    System.out.println("sub1->" + s);
	    Thread.sleep(500);
	}
    }

    private static class Sub2 {
	@Subscribe
	@AllowConcurrentEvents
	public void sub(Integer i) {
	    System.out.println("sub2->" + i);
	}
    }
}
