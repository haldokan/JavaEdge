package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * My rough-edged solution to an Amazon interview question - Circular array of max-heaps.
 *
 * The Question: 4-STAR
 *
 * Given that we have access to a real-time stream of sold product ids.
 * How to find out the top trending products on a last hour/day/monthly basis?
 *
 * 07/16/20
 */
public class TopTrendingProducts {
    PriorityQueue<Product>[] trending = new PriorityQueue[30 * 24];
    int pass = 0; // used to indicate a new loop in the circular array
    int index = 0;

    void aggregate(Map<String, Integer> result, PriorityQueue<Product> currentHeap) {
        if (currentHeap != null) {
            Product product = currentHeap.peek();
            result.compute(product.id, (k, v) -> v == null ? product.count : v + product.count);
        }
    }

    List<Product> trending(int hours) {
        // check if hours > index and get entries from both sides of array
        // if trending[index] != null => trending[index].get(firstKey)
        Map<String, Integer> topSellers = new HashMap<>();
        int diff = index - hours;
        if (diff >= 0) {
            for (int i = 0; i <= diff; i++) {
                aggregate(topSellers, this.trending[index - i]);
            }
        }
        if (diff < 0) {
            for (int i = 0; i <= -diff; i++) {
                aggregate(topSellers, this.trending[this.trending.length - 1 - i]);
            }
        }
        // aggregate again on product to return top sellers sorted in descending order for the period
        return topSellers.entrySet().stream()
            .map(e -> new Product(e.getKey(), e.getValue(), 0))
            .sorted(Comparator.comparingInt(p -> -p.count))
            .collect(Collectors.toList());
    }

    void onProduct(String id) {
        LocalDateTime time = LocalDateTime.now();
        index = time.getDayOfMonth() * 24 % trending.length;
        if (index == 0) {
            pass += 1;
        }

        PriorityQueue<Product> currentHourHeap = trending[index];
        if (currentHourHeap == null || currentHourHeap.peek().pass != pass) {
            currentHourHeap = new PriorityQueue<>(Comparator.comparingInt(p -> -p.count));
            trending[index] = currentHourHeap;
        }

        Product newEntry = Product.create(id, pass);
        if (currentHourHeap.contains(newEntry)) {
            Product previous = currentHourHeap.stream().filter(p -> p.id.equals(id)).findFirst().get();
            currentHourHeap.remove(newEntry);
            newEntry.increaseCount(previous.count);
        }
        currentHourHeap.add(newEntry);
    }

    static class Product {
        String id;
        int count;
        int pass;

        public Product(String id, int count, int pass) {
            this.id = id;
            this.count = count;
            this.pass = pass;
        }

        void increaseCount(int by) {
            count += by;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Objects.equals(id, product.id);
        }

        static Product create(String id, int pass) {
            return new Product(id, 1, pass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
