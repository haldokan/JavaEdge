package org.haldokan.edge.asyncserver;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * In progress...
 * 
 * Simulate how asynchronous sockets work
 * @author haldokan
 *
 */
public class AsyncServer {
    private static String FILE_REPO = "c:\\temp\\asyncserver";

    private Map<Long, BlockingQueue<ServiceRequest>> responseByThread = new ConcurrentHashMap<>();

    private BlockingQueue<ServiceRequest> serviceQu;
    private ListeningExecutorService dispatcher = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(12));

    public static void main(String[] args) {
	new AsyncServer().serviceLoop();

    }

    public AsyncServer() {
	this.serviceQu = new LinkedBlockingQueue<>();
    }

    public void startup() {
	serviceLoop();
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

    private Future<Long> processWordCountRequest(WordCount request) {
	Future<Map<String, Long>> wcf = getWordCount(request.getFileName());
	request.setWordcount(wcf);
	responseByThread.get(request.getThreadId()).offer(request);
	return persistWordCount(wcf, request.getFileName());
    }

    private Future<Map<String, Long>> getWordCount(String file) {
	// read the file and count the occurrences of each word
	// use open-with-resources
	return dispatcher.submit(new Callable<Map<String, Long>>() {
	    @Override
	    public Map<String, Long> call() throws Exception {
		try (Stream<String> lines = Files.lines(Paths.get(FILE_REPO, file))) {
		    return lines.flatMap(l -> Stream.of(l.split(" ")))
			    .collect(groupingBy(Object::toString, counting()));
		}
	    }
	});
    }

    // persist the wc and return how long it took to do so
    private Future<Long> persistWordCount(Future<Map<String, Long>> wcf, String file) {
	return dispatcher.submit(new Callable<Long>() {
	    @Override
	    public Long call() throws Exception {
		try {
		    Stopwatch watch = Stopwatch.createStarted();
		    Map<String, Long> wc = wcf.get();
		    Files.write(Paths.get(FILE_REPO, file, "wc"), wc.entrySet().stream().map(e -> e.toString())
			    .collect(Collectors.toList()));
		    return watch.elapsed(TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | IOException e) {
		    // not necessarily the best exception handling but that is
		    // not the
		    // focus here
		    throw new RuntimeException(e);
		}
	    }
	});
    }

    /**
     * Suppose we expose this method in an RPC api.
     *
     * @param fileName
     * @return string formatted as word1:count1;word2:count2
     * @throws ExecutionException
     */
    public String wordcount(String file) throws ExecutionException {
	WordCount wc = new WordCount(file, Thread.currentThread().getId());
	try {
	    responseByThread.put(wc.getThreadId(), new SynchronousQueue<ServiceRequest>());
	    serviceQu.offer(wc);
	    WordCount response = (WordCount) responseByThread.get(wc.getThreadId()).take();

	    return response.getWordcount().get().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
		    .collect(Collectors.joining(";"));
	} catch (InterruptedException e) {
	    // it actually makes sense to ignore interruption and go back to
	    // blocking on queue
	    throw new RuntimeException(e);
	} finally {
	    responseByThread.remove(wc.getThreadId());
	}
    }
}
