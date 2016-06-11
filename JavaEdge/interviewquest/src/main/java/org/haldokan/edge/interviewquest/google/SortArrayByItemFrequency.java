package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - O(n) space and O(nlogn) time.
 *
 * The Question: 3_STAR
 *
 * Given an unsorted array of natural numbers where numbers repeat in array. Output up to N numbers in order of frequency.
 * N is passed as parameter.
 * <p>
 * For Ex:
 * Array -> [0, 0, 100, 3, 5, 4, 6, 4, 2, 100, 2, 100]
 * n -> 2
 * Output -> [100, 0] or [100, 2], here's why:
 * <p>
 * Since 100 is repeated 3 times and each of 0, 2 is repeated 2 times, output up to 2 most frequent elements, program should
 * output either 100, 0 or 100, 2
 * <p>
 * Created by haytham.aldokanji on 5/22/16.
 */
public class SortArrayByItemFrequency {

    public static void main(String[] args) {
        SortArrayByItemFrequency driver = new SortArrayByItemFrequency();

        int[] arr = new int[]{2, 10, 0, 0, 10, 3, 5, 4, 6, 4, 2, 10, 2, 10};
        Integer[] sorted = driver.sortedByFrequency(arr, 3);
        assertThat(Arrays.asList(sorted), contains(10, 2, 0));

        arr = new int[]{0, 2, 10, 0, 0, 10};
        sorted = driver.sortedByFrequency(arr, 3);
        assertThat(Arrays.asList(sorted), contains(0, 10, 2));

        arr = new int[]{0, 2, 10, 0, 0, 10};
        sorted = driver.sortedByFrequency(arr, 77);
        assertThat(Arrays.asList(sorted), contains(0, 10, 2));
    }

    public Integer[] sortedByFrequency(int[] arr, int topN) {
        if (arr == null || arr.length == 0 || topN == 0) {
            throw new IllegalArgumentException("Invalid input: arr = "
                    + (arr != null ? Arrays.toString(arr) : null) + ", topN = " + topN);
        }

        Map<Integer, Integer> frequency = new HashMap<>();
        for (int num : arr) {
            frequency.compute(num, (k, v) -> v == null ? 1 : v + 1);
        }

        int effectiveTopN = topN > frequency.size() ? frequency.size() : topN;
        return frequency.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(effectiveTopN)
                .map(Map.Entry::getKey)
                .toArray(Integer[]::new);
    }
}
