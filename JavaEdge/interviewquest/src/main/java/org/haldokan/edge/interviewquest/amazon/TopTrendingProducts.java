package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * My rough-edged solution to an Amazon interview question - This is a design question that I wanted to make sense of
 * by providing impl of the basic concepts
 *
 * The Question: 4-STAR
 *
 * Given that we have access to a real-time stream of sold product ids.
 * How to find out the top trending products on a last hour/day/monthly basis?
 *
 * 06/09/25
 */
public class TopTrendingProducts {
    private final StreamHour[] productStream = new StreamHour[365 * 24];
    private final Deque<String> products = new ArrayDeque<>(); // Potentially kafka topic
    private static final LocalDateTime startTime = LocalDateTime.now();
    private static final int TOP_N = 8;
    private int lastHour = 0;

    public void subscribe() {
        var product = products.poll();
        while (product != null) {
            processProduct(product);
            product = products.poll();
        }
    }

    public List<ProductRank> hourTrending() {
        return productStream[lastHour].getTrending();
    }

    public List<ProductRank> dayTrending() {
        var startHour = Math.max(lastHour - 24, 0);
        var aggregateTopN = new ArrayList<ProductRank>();
        for (int hour = startHour;  hour <= lastHour; hour++) {
            aggregateTopN.addAll(productStream[hour].getTopN());
        }
        Map<String, Integer> topNByProduct = new HashMap<>();
        aggregateTopN.forEach(topN ->
                topNByProduct.compute(topN.product, (k, v) -> v == null ? topN.count : v + topN.count));

        return topNByProduct.entrySet().stream()
                .map(entry -> new ProductRank(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(o -> -o.count)).limit(TOP_N).toList();
    }

    public void queueProduct(String product) {
        products.add(product);
    }

    private void processProduct(String product) {
        var time = LocalDateTime.now();
        // Simplified assumption that we get events for *all* the hour intervals
        var hour = time.getHour() - startTime.getHour();
        lastHour = hour;

        StreamHour streamHour = productStream[hour] != null ? productStream[hour] : new StreamHour();
        streamHour.add(product);
        productStream[hour] = streamHour;
    }

    private static final class StreamHour {
        private final Map<String, ProductRank> productCounts = new HashMap<>();
        private final PriorityQueue<ProductRank> minHeap = new PriorityQueue<>(Comparator.comparingInt(o -> -o.count));
        private final Map<String, ProductRank> heapRanks = new HashMap<>();

        public void add(String pid) {
            if (productCounts.containsKey(pid)) {
                productCounts.get(pid).increment();
            } else {
                productCounts.put(pid, new ProductRank(pid));
            }
            rank(productCounts.get(pid));
        }

        public List<ProductRank> getTrending() {
            return minHeap.stream().sorted(Comparator.comparingInt(o -> -o.count)).toList();
        }

        public List<ProductRank> getTopN() {
            return minHeap.stream().toList();
        }

        // Use min heap to always keep track of the TOP-N products
        private void rank(ProductRank productRank) {
            if (heapRanks.containsKey(productRank.product)) {
                minHeap.remove(productRank); // Rmv & add so that the priority queue re-order the heap
                minHeap.add(productRank);
            } else if (minHeap.size() < TOP_N) {
                minHeap.add(productRank);
                heapRanks.put(productRank.product, productRank);
            } else if (productRank.count > minHeap.peek().count) { // Add only if ranked higher than the smallest rank in heap
                minHeap.remove(); // O(N) and there is no way around it
                minHeap.add(productRank);
                heapRanks.put(productRank.product, productRank);
            }
        }
    }

    public static final class ProductRank {
        private final String product;
        private int count;

        public ProductRank(String product) {
            this(product, 1);
        }
        //
        public ProductRank(String product, int count) {
            this.product = product;
            this.count = count;
        }

        public void increment() {
            count += 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ProductRank that = (ProductRank) o;
            return Objects.equals(product, that.product);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(product);
        }
    }
}
