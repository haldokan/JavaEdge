package org.haldokan.edge.executorservice2;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;

/**
 * Executor service that uses fixed size thread pool. It implements the 'submit' method to return a future. New tasks
 * (callables) are added to a service queue. The executor takes the tasks from the service queue and hands them over to
 * an available warm thread from the pool returning a future immediately to the calling client code. Blocking on
 * unavailable resources (when all threads are in usage for example) is done using blocking queues.
 * 
 * @author haldokan
 *
 * @param <V>
 */
public class FixedThreadPoolExecutorService<V> implements ExecutorService2<V> {
    private final BlockingQueue<FutureTask<V>> serviceQu;
    private final BlockingQueue<PoolThread<V>> availableWorkers;
    private final Map<Long, PoolThread<V>> busyWorkers;
    private final BlockingQueue<Long> doneNotifications;

    public FixedThreadPoolExecutorService(int poolSize) {
	this.serviceQu = new LinkedBlockingQueue<>();
	this.availableWorkers = new ArrayBlockingQueue<>(poolSize);
	this.doneNotifications = new ArrayBlockingQueue<>(poolSize);
	this.busyWorkers = new ConcurrentHashMap<>();
	createPoolWorkers(poolSize);
	taskDispatcher();
	slaveDriver();
    }

    public Future<V> submit(Callable<V> task) {
	FutureTask<V> future = new FutureTask<V>(task);
	serviceQu.offer(future);
	return future;
    }

    private void taskDispatcher() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		for (;;) {
		    try {
			FutureTask<V> task = serviceQu.take();
			PoolThread<V> worker = availableWorkers.take();
			busyWorkers.put(worker.getId(), worker);
			worker.submit(task);
		    } catch (InterruptedException e) {
		    }
		}
	    }
	}).start();
    }

    // get done thread back to the pool to do more work
    private void slaveDriver() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		for (;;) {
		    try {
			availableWorkers.offer(busyWorkers.remove(doneNotifications.take()));
		    } catch (InterruptedException e) {
			// we actually want to ignore the interruption and go
			// back to polling the queue
		    }
		}
	    }
	}).start();
    }

    private void createPoolWorkers(int poolSize) {
	for (int i = 0; i < poolSize; i++) {
	    availableWorkers.offer(new PoolThread<>(doneNotifications));
	}
	for (Thread worker : availableWorkers) {
	    worker.start();
	}
    }

    private static class PoolThread<V> extends Thread {
	private final BlockingQueue<Long> doneQu;
	private final BlockingQueue<RunnableFuture<V>> taskHolder = new SynchronousQueue<>();

	public PoolThread(BlockingQueue<Long> doneQu) {
	    this.doneQu = doneQu;
	}

	public void submit(RunnableFuture<V> future) {
	    taskHolder.offer(future);
	}

	public void run() {
	    for (;;) {
		try {
		    taskHolder.take().run();
		    doneQu.offer(getId());
		} catch (InterruptedException e) {
		}
	    }
	}
    }
}