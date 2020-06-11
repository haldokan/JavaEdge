package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - use a map to track frequency and a heap to track top N
 *
 * The Question: 3.5_STAR
 *
 * Given an unsorted array of natural numbers where numbers repeat in array. Output up to N numbers in order of frequency.
 * N is passed as parameter.
 * <p>
 * For Ex:
 * Array -> [0, 0, 100, 3, 5, 4, 6, 2, 100, 2, 100]
 * n -> 2
 * Output -> [100, 0] or [100, 2], here's why:
 * <p>
 * Since 100 is repeated 3 times and each of 0, 2 is repeated 2 times, output up to 2 most frequent elements, program should
 * output either 100, 0 or 100, 2
 * <p>
 * Created by haytham.aldokanji on 5/22/16.
 */
public class SortArrayByItemFrequency2 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(topFreq(new int[]{0, 0, 100, 3, 5, 4, 6, 4, 2, 100, 2, 100, 0, 0}, 2)));
        System.out.println(Arrays.toString(topFreq(new int[]{0, 0, 100, 3, 5, 4, 6, 4, 2, 100, 2, 100, 0, 0, 2}, 3)));
    }

    static Item[] topFreq(int[] arr, int maxSize) {
        PriorityQueue<Item> heap = new PriorityQueue<>(Comparator.comparingInt((item) -> item.freq));
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : arr) {
            int freq = freqMap.compute(num, (k, v) -> v == null ? 1 : v + 1);
            Item item = new Item(num, freq);
            heap.remove(item);
            if (heap.size() < maxSize) {
                heap.add(item);
            } else if (freq > heap.peek().freq) {
                heap.remove();
                heap.add(item);
            }
        }
        Item[] top = new Item[maxSize];
        for (int i = 0; i < maxSize; i++) {
            top[maxSize - i - 1] = heap.remove();
        }
        return top;
    }

    static class Item {
        int num;
        int freq;

        public Item(int num, int freq) {
            this.num = num;
            this.freq = freq;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return num == item.num;
        }

        @Override
        public int hashCode() {
            return Objects.hash(num);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", num, freq);
        }
    }
}
