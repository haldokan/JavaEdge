package org.haldokan.edge.eventloop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class EventLoop {
    private enum ServiceId {
	WORD_COUNT
    };

    private static String serviceName = "service_name";
    private static String readTime = "read_time";
    private static String writeTime = "write_time";
    private static String wordCount = "word_count";
    private static String wordCountService = "word_count";
    private static String fileName = "file_name";
    private static String threadId = "thread_id";

    private Map<Long, BlockingQueue<Map<String, String>>> responseByThread = new ConcurrentHashMap<>();

    private BlockingQueue<Map<String, String>> serviceQu;
    private ListeningExecutorService dispatcher = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(12));

    public EventLoop() {
	this.serviceQu = new LinkedBlockingQueue<>();
    }

    private void loop() {
	for (;;) {
	    try {
		handleRequestAsynch(serviceQu.take());
	    } catch (Throwable t) {
		t.printStackTrace();
	    }
	}
    }

    private void handleRequestAsynch(Map<String, String> request) {
    }

    /**
     * Read a file, count the occurrences of each word and write the result into a new file filename-wc. The purpose is
     * to *simulate* how event loops work. Event loops don't start threads and it relies on non-blocking IO. Non
     * Blocking IO in Java can be done using SelectoS but we will not do that. We will actually start threads since the
     * exercise goal is not non-blocking IO but the design and implementation considerations that go into event loops
     * "eventing".
     *
     * @param fileName
     * @return time it took to read and write the file and the number of distinct words
     */
    public long[] wordCount(String file) {
	Map<String, String> request = new HashMap<>();
	request.put(serviceName, wordCountService);
	request.put(fileName, file);
	long tid = Long.valueOf(Thread.currentThread().getId());
	request.put(threadId, String.valueOf(tid));

	try {
	    responseByThread.put(tid, new SynchronousQueue<Map<String, String>>());
	    serviceQu.offer(request);

	    Map<String, String> response = responseByThread.get(tid).take();
	    return new long[] { Long.valueOf(response.get(readTime)), Long.valueOf(response.get(writeTime)),
		    Long.valueOf(response.get(wordCount)) };
	} catch (InterruptedException e) {
	    // it actually makes sense to ignore interruption and go back to
	    // blocking on queue
	    throw new RuntimeException(e);
	} finally {
	    responseByThread.remove(tid);
	}
    }
}