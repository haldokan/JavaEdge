package org.haldokan.edge.interviewquest.google;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * My solution to a Google interview question - implemented sliding window as a circular array. I run 2 scheduled
 * indexers: one for advancing the index every second and another that resets counts older than window length. When a
 * request is received we map the time diff to an index in the counter array. With this design we can actually get the
 * request count for any interval b/w 0 and and the length of the window. Contention is restricted to updating the
 * LongAdder that represents the count at the current second (there is no avoiding that). LongAdder in Java is a good
 * choice for updating a long by multiple threads.
 * Here is the Javadocs for LongAdder:
 * One or more variables that together maintain an initially zero long sum. When updates (method add(long)) are contended
 * across threads, the set of variables may grow dynamically to reduce contention.
 * Method sum() (or, equivalently, longValue()) returns the current total combined across the variables maintaining the sum.
 * <p>
 * The Question: 5_STAR
 * <p>
 * Given a server that has requests coming in. Design a data structure such that you can fetch the count of the
 * requests in the last second, minute and hour.
 * <p>
 * Created by haytham.aldokanji on 6/30/16.
 */
public class SlidingWindowServerRequestCounter {
    private final int windowLength;
    private final long startTime;

    private final LongAdder[] requestCounter;
    private int currentIndex;
    private int resetIndex;

    public SlidingWindowServerRequestCounter(int windowLength) {
        // add 1 to the window length so the reset indexer is ahead of the current index
        this.windowLength = windowLength + 1;
        this.startTime = System.currentTimeMillis() / 1000;
        requestCounter = new LongAdder[this.windowLength];
        IntStream.range(0, requestCounter.length).forEach(index -> requestCounter[index] = new LongAdder());
    }

    public static void main(String[] args) throws Exception {
        testMinuteWindow();
        testHourWindow();
    }

    // we cannot assert anything
    private static void testMinuteWindow() throws InterruptedException {
        int window = 60;
        SlidingWindowServerRequestCounter requestCounter = new SlidingWindowServerRequestCounter(window);
        requestCounter.run();

        for (int i = 0; i < 1000; i++) {
            requestCounter.indexRequest(System.currentTimeMillis());
            Thread.sleep(300);

            System.out.printf("last minute: %d%n", requestCounter.getNumberOfRequestsForLastInterval(window));
            System.out.printf("----------------%n");
        }
    }

    // we cannot assert anything
    private static void testHourWindow() throws InterruptedException {
        int window = 3600;
        SlidingWindowServerRequestCounter requestCounter = new SlidingWindowServerRequestCounter(window);
        requestCounter.run();

        for (int i = 0; i < 100; i++) {
            requestCounter.indexRequest(System.currentTimeMillis());
            Thread.sleep(300);

            System.out.printf("last minute: %d%n", requestCounter.getNumberOfRequestsForLastInterval(60));
            System.out.printf("last hour: %d%n", requestCounter.getNumberOfRequestsForLastInterval(window));
            System.out.printf("----------------%n");
        }
    }

    public void run() {
        runIndexer();
        runCountResetIndexer();
    }

    private void runIndexer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            System.out.printf("currentIndex %d%n", currentIndex);
            currentIndex = (currentIndex + 1) % windowLength;
        }, 0, 1, TimeUnit.SECONDS);
    }

    // note that it starts after a delay equal to window length
    private void runCountResetIndexer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            System.out.printf("resetIndex %d%n", resetIndex);
            requestCounter[resetIndex].reset();
            resetIndex = (resetIndex + 1) % windowLength;
        }, windowLength - 1, 1, TimeUnit.SECONDS);
    }

    public void indexRequest(long time) {
        int index = (int) ((time / 1000 - startTime) % windowLength);
        requestCounter[index].increment();
    }

    public int getNumberOfRequestsForLastInterval(int intervalInSeconds) {
        int sum = 0;
        int j = 0;
        int interval = Math.min(windowLength - 1, intervalInSeconds);

        int tmpIndex = currentIndex; // currentIndex is being changed by another thread
        for (int i = tmpIndex; i >= 0 && j < interval; i--) {
            j++;
            sum += requestCounter[tmpIndex - i].intValue();
        }

        for (int i = requestCounter.length - 1; i > tmpIndex && j < interval; i--) {
            sum += requestCounter[i].intValue();
            j++;
        }
        return sum;
    }
}
