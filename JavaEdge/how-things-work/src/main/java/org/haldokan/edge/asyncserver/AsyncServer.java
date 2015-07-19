package org.haldokan.edge.asyncserver;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class AsyncServer {

    private Map<Long, BlockingQueue<ServiceRequest>> responseByThread = new ConcurrentHashMap<>();

    private BlockingQueue<ServiceRequest> serviceQu;
    private ListeningExecutorService dispatcher = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(12));

    public static void main(String[] args) {
	new AsyncServer().serviceLoop();
    }

    public AsyncServer() {
	this.serviceQu = new LinkedBlockingQueue<>();
    }

    private void serviceLoop() {
	for (;;) {
	    try {
		handleRequestAsync(serviceQu.take());
	    } catch (Throwable t) {
		t.printStackTrace();
	    }
	}
    }

    private void handleRequestAsync(ServiceRequest request) {
	switch (request.getServiceName()) {
	case WORD_COUNT:
	    processWordCountRequest((WordCount) request);
	    break;
	default:
	    throw new IllegalArgumentException("Unsupported service " + request.getServiceName());
	}
    }

    private void processWordCountRequest(WordCount request) {
	// TODO Auto-generated method stub

    }

    /**
     * Suppose we expose this method in an RPC api.
     * @param fileName
     * @return time it took to read and write the file and the number of distinct words
     */
    public long[] wordCount(String file) {
	WordCount wc = new WordCount(file, Thread.currentThread().getId());
	try {
	    responseByThread.put(wc.getThreadId(), new SynchronousQueue<ServiceRequest>());
	    serviceQu.offer(wc);

	    WordCount response = (WordCount) responseByThread.get(wc.getThreadId()).take();
	    return new long[] { response.getReadTime(), response.getWriteTime(), response.getCount() };
	} catch (InterruptedException e) {
	    // it actually makes sense to ignore interruption and go back to
	    // blocking on queue
	    throw new RuntimeException(e);
	} finally {
	    responseByThread.remove(wc.getThreadId());
	}
    }
}