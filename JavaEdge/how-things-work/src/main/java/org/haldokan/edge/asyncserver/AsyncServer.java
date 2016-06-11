package org.haldokan.edge.asyncserver;

import com.google.common.base.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Simulate how asynchronous sockets work
 * The Question: 5_STAR
 * @author haldokan
 */
public class AsyncServer {
    private static String FILE_REPO = "/Users/haytham.aldokanji/dev/sink/asyncserver";
    // does not matter what Service request we create since pill is checked
    // using identity (==).
    private static ServiceRequest POISON_PILL = new AdminRequest(AdminRequestType.SHUTDOWN);

    private final Map<Long, BlockingQueue<ServiceRequest>> responseByThread;
    private final BlockingDeque<ServiceRequest> serviceQu;
    private final BlockingQueue<ServiceRequest> adminQu;
    private final ExecutorService dispatcher;
    private final Map<ServiceName, Future<Long>> averageTimeByService = new HashMap<>();
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private CountDownLatch terminator;

    public AsyncServer() {
        this.serviceQu = new LinkedBlockingDeque<>();
        this.adminQu = new LinkedBlockingQueue<>();
        this.dispatcher = Executors.newFixedThreadPool(12);
        this.responseByThread = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        new AsyncServer().startup();

    }

    public void startup() {
        runRequestServicer();
        runAdminServicer();
        // 2 servicers for admin and business requests
        terminator = new CountDownLatch(2);
        runForever();
    }

    private void runForever() {
        for (; ; ) {
            try {
                terminator.await();
                return;
            } catch (InterruptedException e) {
                // ignore interruption and go back to waiting on latch
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        System.out.println("Shutting down service loop");
        adminQu.offer(new AdminRequest(AdminRequestType.SHUTDOWN));
    }

    public void shutdownAll() {
        System.out.println("Shutting down server");
        adminQu.offer(new AdminRequest(AdminRequestType.SHUTDOWN_ALL));
    }

    private void runRequestServicer() {
        dispatcher.submit(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        ServiceRequest request = serviceQu.take();
                        if (request == POISON_PILL) {
                            terminator.countDown();
                            break;
                        }
                        handleRequestAsync(request);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private void runAdminServicer() {
        dispatcher.submit(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        ServiceRequest request = adminQu.take();
                        if (request == POISON_PILL) {
                            terminator.countDown();
                            break;
                        }
                        handleAdminRequest(request);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private void handleAdminRequest(ServiceRequest sreq) {
        AdminRequest request = (AdminRequest) sreq;
        if (request.getType() == AdminRequestType.SHUTDOWN) {
            shuttingDown.set(true);
            // put poison on tails of the queue allowing requests already there
            // to be processed
            serviceQu.offer(POISON_PILL);
        } else if (request.getType() == AdminRequestType.SHUTDOWN_ALL) {
            shuttingDown.set(true);
            serviceQu.offer(POISON_PILL);
            adminQu.offer(POISON_PILL);
        } else {
            throw new IllegalArgumentException("unsupported request type " + sreq);
        }
    }

    private void handleRequestAsync(ServiceRequest request) {
        switch (request.getServiceName()) {
            case WORD_COUNT:
                Future<Long> timeTaken = processWordCountRequest((WordCount) request);
                // we don't wait on computing the service time
                averageTimeByService.compute(ServiceName.WORD_COUNT,
                        (k, v) -> v == null ? timeTaken : dispatcher.submit(() -> (v.get() + timeTaken.get()) / 2));
                break;
            default:
                throw new IllegalArgumentException("Unsupported service " + request.getServiceName());
        }
    }

    private Future<Long> processWordCountRequest(WordCount request) {
        Stopwatch watch = Stopwatch.createStarted();
        Future<Map<String, Long>> wcf = getWordCount(request.getFileName());
        request.setWordcount(wcf);
        // note that the response is made available to the user on its
        // thread-id-specific synchronous queue.
        // User does not wait for persisting the file
        responseByThread.get(request.getThreadId()).offer(request);
        return persistWordCount(wcf, request.getFileName(), watch);
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
    private Future<Long> persistWordCount(Future<Map<String, Long>> wcf, String file, Stopwatch watch) {
        return dispatcher.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                try {
                    Map<String, Long> wc = wcf.get();
                    Files.write(Paths.get(FILE_REPO, file + "-wc." + System.currentTimeMillis()), wc.entrySet()
                            .stream().map(e -> e.toString()).collect(Collectors.toList()));
                    return watch.elapsed(TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | IOException e) {
                    // not necessarily the best exception handling but that is not the focus here
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Suppose we expose this method in an RPC api.
     *
     * @param file
     * @return string formatted as word1:count1;word2:count2
     * @throws ExecutionException
     */
    public String wordcount(String file) throws ExecutionException {
        if (shuttingDown.get())
            return null;

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

    public Long getServiceAverageTime(ServiceName service) throws InterruptedException, ExecutionException {
        Future<Long> time = averageTimeByService.get(service);
        return time == null ? null : time.get();
    }
}