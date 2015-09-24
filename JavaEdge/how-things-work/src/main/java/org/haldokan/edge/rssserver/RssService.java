package org.haldokan.edge.rssserver;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by haytham.aldokanji on 9/23/15.
 */
public class RssService {
    private final Item POISON_PILL = new Item(null, null, null);

    private final BlockingQueue<Item> fetchedItems;
    private final BlockingQueue mergeQu;
    private AtomicBoolean keepRunning = new AtomicBoolean(true);
    private RssFetcher fetcher;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RssService(RssFetcher fetcher, int maxPending, BlockingQueue mergeQu) {
        this.fetcher = fetcher;
        this.fetchedItems = new ArrayBlockingQueue<>(maxPending);
        this.mergeQu = mergeQu;
    }

    public void start() {
        executorService.submit(() -> fetchItems());
        executorService.submit(() -> moveItemsToMergeQu());
    }

    public void stop() {
        fetchedItems.offer(POISON_PILL);
    }

    public void fetchItems() {
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
        System.out.println("fetching items stopped");
    }

    private void moveItemsToMergeQu() {
        while (keepRunning.get()) {
            try {
                Item item = fetchedItems.take();
                if (item == POISON_PILL) { //it is ==, not equals
                    keepRunning.set(false);
                } else {
                    mergeQu.offer(item);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Moving items to merge queue stopped");
    }
}
