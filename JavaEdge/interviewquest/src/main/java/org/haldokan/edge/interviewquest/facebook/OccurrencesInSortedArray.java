package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - solved in log(n). I implemented the binary search that does
 * the trick: when an item is not found in a sorted array the (negative) index is returned for its position if it were in the array
 * The Question: 4_STAR
 * Given an array of ages (integers) sorted lowest to highest, output the number of occurrences for each age.
 * For instance:
 * [8,8,8,9,9,11,15,16,16,16]
 * should output something like:
 * 8: 3
 * 9: 2
 * 11: 1
 * 15: 1
 * 16: 3
 * <p>
 * This should be done in less than O(n).
 * Created by haytham.aldokanji on 5/11/16.
 */
public class OccurrencesInSortedArray {

    public static void main(String[] args) {
        OccurrencesInSortedArray driver = new OccurrencesInSortedArray();

        int[] arr = new int[]{8, 8, 8, 9, 9, 11, 13, 13, 15, 16, 16, 16};
        List<int[]> occurrences = driver.occurrences(arr);
        occurrences.stream().forEach(v -> System.out.println(Arrays.toString(v)));
        assertThat(occurrences.get(0), is(new int[]{8, 3}));
        assertThat(occurrences.get(1), is(new int[]{9, 2}));
        assertThat(occurrences.get(2), is(new int[]{11, 1}));
        assertThat(occurrences.get(3), is(new int[]{13, 2}));
        assertThat(occurrences.get(4), is(new int[]{15, 1}));
        assertThat(occurrences.get(5), is(new int[]{16, 3}));
    }

    public List<int[]> occurrences(int[] arr) {
        List<int[]> occurrences = new ArrayList<>();

        int numberOccurrences;
        for (int i = 0; i < arr.length; i += numberOccurrences) {
            int val = arr[i];

            int nextValIndex = binarySearch(arr, val + 1);
            if (nextValIndex < 0) {
                nextValIndex *= -1;
            }
            numberOccurrences = nextValIndex - i;
            occurrences.add(new int[]{val, numberOccurrences});
        }
        return occurrences;
    }

    private int binarySearch(int[] arr, int val) {
        return binarySearch(arr, 0, arr.length, val);
    }

    private int binarySearch(int[] arr, int start, int end, int val) {
        int mid = (start + end) / 2;
        if (end - start <= 1) {
            return -(mid + 1);
        }
        if (val == arr[mid]) {
            return mid;
        }
        if (val < arr[mid]) {
            return binarySearch(arr, start, mid, val);
        } else {
            return binarySearch(arr, mid, end, val);
        }
    }
}
