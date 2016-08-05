package org.haldokan.edge.interviewquest.bloomberg;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question - for the moving 1 month window I use a circular array that is indexed
 * by the LocalDate.DayofYear modulo 30 days. We have 2 indexing tasks running in separate threads. On tasks advances
 * the write index at the head of the circular array every day and the other task resets the contents at the tail of the
 * array every day. Each item is the circular array is a map b/w a product id and its count for the day. The circular array
 * enables us to get a product count for any interval less or equal the (configurable) window length.
 * <p>
 * For getting the top products I used a min heap that is ordered on the number of product occurrences. For every product
 * we read its count from the circular array (at the index dayOfYear modulo window length). Min heap insures that we keep
 * only the top N values but since product ids repeat we have to find if the product already exists and update it. This
 * leads to degrading heap operations from log n to n. Every product that is added to the heap has a dayOfYear modulo that
 * links it to the circular array indexes. When a day falls off the start end of the window (older than window length)
 * we remove from the heap all entries that have the dayOfYear module corresponding to the fallen array index. We use a min
 * heap because it enables us to control the size based on the min element on the heap top. This means that when we want to get
 * The top products based on their occurrence count we have to get a snapshot of the heap copied to a max heap (reversing
 * the order).
 * The question is a combination of 2 questions that I resolved else where in this repo:
 * One from Google (SlidingWindowServerRequestCounter), and the other from Bloomberg (TickServerWithPlotterClients).
 * <p>
 * The Question: 5_STAR
 * <p>
 * We have multiple log files that contain data of the format date, productId. Product ids can repeat at different dates.
 * Write the data structures to keep track of the top 10 products (number of occurrences) over a 1 month moving window
 * <p>
 * Created by haytham.aldokanji on 8/4/16.
 */
public class TopNProuductsInTimeWindow {

    private final int windowLengthInDays;
    private final long startTime;

    private final List<Map<String, Integer>> productCounter;
    private final Heap heap;
    private int currentIndex;
    private int resetIndex;

    public TopNProuductsInTimeWindow(int windowLengthInDays, int topSampleSize) {
        // add 1 to the window length so the reset indexer is ahead of the current index
        this.windowLengthInDays = windowLengthInDays + 1;
        this.startTime = LocalDate.now().getDayOfYear();

        productCounter = new ArrayList<>();
        IntStream.range(0, this.windowLengthInDays).forEach(index -> productCounter.add(new ConcurrentHashMap<>()));

        heap = new Heap(topSampleSize);
    }

    public static void main(String[] args) throws Exception {
        test();
    }

    // we cannot assert anything
    private static void test() throws InterruptedException {
        int window = 60;
        int topSampleSize = 7;
        TopNProuductsInTimeWindow productCounter = new TopNProuductsInTimeWindow(window, topSampleSize);
        productCounter.run();

        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            String productId = "pid" + random.nextInt(20);
            productCounter.indexProduct(productId, LocalDateTime.now());
            Thread.sleep(10);

            System.out.printf("last 10 days: %s->%d%n",
                    productId, productCounter.getNumberOfOccurrencesForProductInLastInterval(productId, 10));
            System.out.printf("----------------%n");
        }

        List<Product> topProducts = productCounter.getTopProducts();
        System.out.printf("%s%n", topProducts);

        assertThat(topProducts.size(), is(topSampleSize));

        Product[] sortedTopProducts = topProducts.stream().sorted((p1, p2) ->
                p2.count - p1.count).toArray(Product[]::new);
        assertThat(topProducts.toArray(), is(sortedTopProducts));

        System.exit(0);
    }

    public void run() {
        runIndexer();
        runCountResetIndexer();
    }

    private void runIndexer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            System.out.printf("currentIndex %d%n", currentIndex);
            currentIndex = (currentIndex + 1) % windowLengthInDays;
        }, 0, 1, TimeUnit.DAYS);
    }

    // note that is starts after a delay equal to window length
    private void runCountResetIndexer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            System.out.printf("resetIndex %d%n", resetIndex);

            productCounter.get(resetIndex).clear();
            heap.removeProductsForModuloDayOfYear(resetIndex);
            resetIndex = (resetIndex + 1) % windowLengthInDays;
        }, windowLengthInDays - 1, 1, TimeUnit.DAYS);
    }

    public void indexProduct(String productId, LocalDateTime dateTime) {
        int index = (int) ((dateTime.getDayOfYear() - startTime) % windowLengthInDays);
        Integer count = productCounter.get(index).compute(productId, (k, v) -> v == null ? 1 : v + 1);

        Product product = new Product(productId, count, dateTime, index);
        heap.onProduct(product);
    }

    public int getNumberOfOccurrencesForProductInLastInterval(String productId, int intervalInDays) {
        int sum = 0;
        int j = 0;
        int interval = Math.min(windowLengthInDays - 1, intervalInDays);

        int tmpIndex = currentIndex; // currentIndex is being changed by another thread
        for (int i = tmpIndex; i >= 0 && j < interval; i--) {
            j++;
            sum += productCounter.get(tmpIndex - i).getOrDefault(productId, 0);
        }

        for (int i = productCounter.size() - 1; i > tmpIndex && j < interval; i--) {
            sum += productCounter.get(i).getOrDefault(productId, 0);
            j++;
        }
        return sum;
    }

    public List<Product> getTopProducts() {
        Queue<Product> heapCopy = heap.topProducts();
        List<Product> result = new ArrayList<>();

        Product product;
        do {
            product = heapCopy.poll();
            if (product != null) {
                result.add(product);
            }
        } while (product != null);

        return result;
    }

    private static class Product {
        private final String productId;
        private final int count;
        private final LocalDateTime dateTime;
        private final int moduloDayOfYear;

        public Product(String productId, int count, LocalDateTime dateTime, int moduloDayOfYear) {
            this.productId = productId;
            this.count = count;
            this.dateTime = dateTime;
            this.moduloDayOfYear = moduloDayOfYear;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Product product = (Product) o;

            return productId.equals(product.productId);

        }

        @Override
        public int hashCode() {
            return productId.hashCode();
        }

        @Override
        public String toString() {
            return "Product{" +
                    "productId='" + productId + '\'' +
                    ", count=" + count +
                    ", dateTime=" + dateTime +
                    ", moduloDayOfYear=" + moduloDayOfYear +
                    '}';
        }
    }

    private static class Heap {
        private final Queue<Product> minHeap;
        private final int topSampleSize;

        public Heap(int topSampleSize) {
            this.topSampleSize = topSampleSize;
            // accessed by different 2 threads: add product, and remove products for dates that fall off the window start
            minHeap = new PriorityBlockingQueue<>(1024, (p1, p2) -> p1.count - p2.count);
        }

        public void removeProductsForModuloDayOfYear(int moduloDayOfYear) {
            minHeap.removeIf(product -> product.moduloDayOfYear == moduloDayOfYear);
        }

        public void onProduct(Product product) {
            Optional<Product> potentialProduct = minHeap.stream()
                    .filter(productCount -> productCount.productId.equals(product.productId))
                    .findAny();

            if (potentialProduct.isPresent()) {
                Product productAlreadyInHeap = potentialProduct.get();
                minHeap.remove(productAlreadyInHeap);
                minHeap.add(product);
            } else if (minHeap.size() < topSampleSize || product.count > minHeap.peek().count) {
                minHeap.add(product);
                if (minHeap.size() > topSampleSize) {
                    minHeap.remove();
                }
            }
        }

        public Queue<Product> topProducts() {
            Queue<Product> snapshot = new PriorityQueue<>((p1, p2) -> p2.count - p1.count);
            snapshot.addAll(minHeap);
            return snapshot;
        }
    }
}
