package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - O(n) time and O(1) space since we remove the entries that have repetitions
 * of 3 as soon as we find them
 *
 * The Question: 3_STAR
 *
 * You have an array of integers. Each integer in the array should be listed three times in the array.
 * Find the integer which does not comply to that rule.
 * <p>
 * Created by haytham.aldokanji on 5/19/16.
 */
public class ThreeOfEachInArray {

    public static void main(String[] args) {
        ThreeOfEachInArray driver = new ThreeOfEachInArray();
        int[] arr = new int[]{7, 7, 7, 1, 1, 3, 3, 3};
        assertThat(driver.find(arr), is(1));

        arr = new int[]{7, 1, 1, 1, 3, 3, 3};
        assertThat(driver.find(arr), is(7));

        arr = new int[]{7, 7, 7, 1, 1, 1, 3, 3};
        assertThat(driver.find(arr), is(3));

        arr = new int[]{7, 7, 7, 1, 1, 1, 3};
        assertThat(driver.find(arr), is(3));
    }

    public int find(int[] arr) {
        Map<Integer, Integer> repeatsByNum = new HashMap<>();

        for (int num : arr) {
            int newVal = repeatsByNum.compute(num, (k, v) -> v == null ? 1 : ++v);
            if (newVal == 3) {
                repeatsByNum.remove(num);
            }
        }
        if (repeatsByNum.size() != 1) {
            throw new IllegalStateException("Input is mal-formatted: " + Arrays.toString(arr));
        }
        return Iterables.getFirst(repeatsByNum.keySet(), null);
    }

}
