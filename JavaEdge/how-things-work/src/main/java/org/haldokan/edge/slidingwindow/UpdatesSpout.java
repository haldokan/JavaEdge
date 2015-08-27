package org.haldokan.edge.slidingwindow;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Push the updates into the system and detect trending hash tags with the help of our SlidingTimeWindow and
 * TrendDetecttor
 *
 * @author haldokan
 */
public class UpdatesSpout {
    private final BlockingQueue<Update> updateQueue;
    private final HashMap<String, SlidingTimeWindow> windowPerTag;
    private final TrendDetector trendDetector;
    private final int concurrLevel;
    private int windowLength = 10;
    private int windowShift = 1;
    private ExecutorService exec;

    public UpdatesSpout(BlockingQueue<Update> updateQueue, TrendDetector trendDetector, int concurrLevel) {
        this.updateQueue = updateQueue;
        this.windowPerTag = new HashMap<>();
        this.trendDetector = trendDetector;
        this.exec = Executors.newFixedThreadPool(16);
        this.concurrLevel = concurrLevel;
    }

    public void setWindowLength(int windowLength) {
        this.windowLength = windowLength;
    }

    public void setWindowShift(int windowShift) {
        this.windowShift = windowShift;
    }

    public void start() throws InterruptedException {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Update upd = updateQueue.take();
                        SlidingTimeWindow sw = windowPerTag.get(upd.getTag());
                        if (sw == null) {
                            sw = new SlidingTimeWindow(windowLength, windowShift, trendDetector);
                            windowPerTag.put(upd.getTag(), sw);
                            sw.start();
                        }
                        sw.add(upd);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}