package org.haldokan.edge.interviewquest.google;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * My solution to a Google interview question - implemented sliding window as a circular array. I run 2 scheduled
 * indexers: one for advancing the index every second and another that resets counts older than 3600 seconds. When a
 * request is received we map the time diff to an index in the counter array
 * <p>
 * The Question: 5_STAR
 * <p>
 * Given a server that has requests coming in. Design a data structure such that you can fetch the count of the number
 * requests in the last second, minute and hour.
 * <p>
 * Created by haytham.aldokanji on 6/30/16.
 */
public class ServerRequestCountInSlidingWindow {
    // circular array
    private static final int COUNT_RESOLUTION = 3600;
    private final AtomicInteger[] requestCounter = new AtomicInteger[COUNT_RESOLUTION];
    private long startTime;
    private int currentIndex;
    private int resetIndex;

    public ServerRequestCountInSlidingWindow() {
        init();
    }

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws InterruptedException {
        ServerRequestCountInSlidingWindow requestCounter = new ServerRequestCountInSlidingWindow();
        requestCounter.run();

        for (int i = 0; i < 1000; i++) {
            requestCounter.countRequest(System.currentTimeMillis() / 1000);
            Thread.sleep(10);

            System.out.printf("last second: %d%n", requestCounter.getNumberOfRequestsInLastTimeUnit(TimeUnit.SECONDS));
            System.out.printf("last minute: %d%n", requestCounter.getNumberOfRequestsInLastTimeUnit(TimeUnit.MINUTES));
            System.out.printf("last hour: %d%n", requestCounter.getNumberOfRequestsInLastTimeUnit(TimeUnit.HOURS));
            System.out.printf("----------------%n");
        }
    }

    private void init() {
        this.startTime = System.currentTimeMillis() / 1000;
        for (int i = 0; i < requestCounter.length; i++) {
            requestCounter[i] = new AtomicInteger(0);
        }
    }

    public void run() {
        runIndexer();
        runCountResetter();
    }

    private void runIndexer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            System.out.printf("Index/second %d%n", currentIndex);
            currentIndex = (currentIndex + 1) % COUNT_RESOLUTION;
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void runCountResetter() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            requestCounter[resetIndex].set(0);
            resetIndex = (resetIndex + 1) % COUNT_RESOLUTION;
        }, 3600, 1, TimeUnit.SECONDS);
    }

    public void countRequest(long time) {
        int index = (int) ((time - startTime) % COUNT_RESOLUTION);
        requestCounter[index].getAndIncrement();
    }

    public int getNumberOfRequestsInLastTimeUnit(TimeUnit timeUnit) {
        if (timeUnit == TimeUnit.SECONDS) {
            int index = currentIndex;
            return requestCounter[index].get();
        } else if (timeUnit == TimeUnit.MINUTES) {
            return getNumberOfRequestsForInterval(60);
        } else if (timeUnit == TimeUnit.HOURS) {
            return getNumberOfRequestsForInterval(3600);
        } else {
            throw new IllegalArgumentException("Time unit is not supported: " + timeUnit);
        }
    }

    private int getNumberOfRequestsForInterval(int seconds) {
        int sum = 0;
        int j = 0;
        for (int i = currentIndex; i >= 0 && j < seconds; i--) {
            j++;
            sum += requestCounter[currentIndex - i].get();
        }

        for (int i = requestCounter.length - 1; i > currentIndex && j < seconds; i--) {
            sum += requestCounter[i].get();
            j++;
        }
        return sum;
    }
}
