package org.haldokan.edge.slidingwindow;

import java.time.LocalTime;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sliding window keeps updates for a specified interval (say last 15 mins) while shifting the window at specified ticks
 * (say every 10 seconds). If we have very high volume of updates there will be high contention on b/w add &
 * remove threads: this is a blocking queue. The WindowShard is designed to reduce such contention.
 */
public class SlidingTimeWindow {
    // window length in seconds
    private final int length;
    // shift interval in seconds
    private final int shift;
    private ScheduledExecutorService ticker;
    private TrendDetector trendDetector;
    private List<WindowShard<Update>> windowShards = new CopyOnWriteArrayList<>();

    public SlidingTimeWindow(int length, int shift, TrendDetector trendDetector) {
        this.length = length;
        this.shift = shift;
        this.trendDetector = trendDetector;
        // TODO: find a way to specify how many sliding windows and how to
        // increase/decrease them
        windowShards.add(new WindowShard<Update>());
        windowShards.add(new WindowShard<Update>());
        windowShards.add(new WindowShard<Update>());
    }

    // should have at lease 1 shard; count can be +/-
    public void changeWindowShardsSizeTo(int size) {

    }

    public void start() {
        ticker = Executors.newScheduledThreadPool(windowShards.size());
        for (WindowShard<Update> shard : windowShards) {
            ticker.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Update upd = shard.peekFirst();
                    while (upd != null && upd.getTime().isBefore(LocalTime.now().minusSeconds(length))) {
                        shard.remove(upd);
                        trendDetector.notify(new TagRank(upd.getTag(), getSize()));
                        // System.out.println("removed: " + upd);
                        upd = shard.peekFirst();
                    }
                }
            }, 0, shift, TimeUnit.SECONDS);
        }
    }

    public void add(Update upd) {
        WindowShard<Update> shard = windowShards.get(upd.hashCode() % windowShards.size());
        shard.add(upd);
        trendDetector.notify(new TagRank(upd.getTag(), getSize()));
    }

    public int getSize() {
        int size = 0;
        for (WindowShard<Update> shard : windowShards) {
            size += shard.size();
        }
        return size;
    }

    private static class WindowShard<E> {
        private AtomicInteger size = new AtomicInteger();
        private Deque<E> shard = new ConcurrentLinkedDeque<E>();

        public int add(E upd) {
            shard.add(upd);
            return size.incrementAndGet();
        }

        public int remove(E upd) {
            shard.remove(upd);
            return size.decrementAndGet();
        }

        public int size() {
            return size.intValue();
        }

        public E peekFirst() {
            return shard.peekFirst();
        }
    }
}
