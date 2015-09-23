package org.haldokan.edge.rssserver;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by haytham.aldokanji on 9/23/15.
 */
public class RssService {
    private final BlockingQueue<Item> fetchedItems;
    private AtomicBoolean keepRunning = new AtomicBoolean(true);
    private RssFetcher fetcher;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RssService(RssFetcher fetcher, int maxPending) {
        this.fetcher = fetcher;
        this.fetchedItems = new ArrayBlockingQueue<>(maxPending);
    }

    public void stop() {
        keepRunning.set(false);
    }

    public void run() {
        long waitTime = 0;
        while (keepRunning.get()) {
            // called on a separate thread assuming that we can do other work while fetching is taking place
            if (waitTime > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Future<FetchedData> futureData = executorService.submit(fetcher::fetch);
            // but we actually wait for completion of the future here
            try {
                FetchedData data = futureData.get();
                if (data != null && !data.isFailed()) {
                    data.getItems().forEach(fetchedItems::offer);
                    waitTime = LocalTime.now().until(data.getNext(), ChronoUnit.MILLIS);
                } else {
                    waitTime = 2000;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public Item getItem() {
        for (; ; ) {
            try {
                return fetchedItems.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
