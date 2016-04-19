package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * My solution to a Facebook interview question
 * <p>
 * Given an array of positive, unique, increasingly sorted numbers A, e.g. A = [1, 2, 3, 5, 6, 8, 9, 11, 12, 13].
 * Given a positive value K, e.g. K = 3. Output all pairs in A that differ exactly by K.
 * Example:
 * 2, 5
 * 3, 6
 * 5, 8
 * 6, 9
 * 8, 11
 * 9, 12
 * what is the runtime for your code?
 */
public class SortedArrayElementsGreaterThanValue {

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 5, 6, 8, 9, 11, 12, 13};
        pairs(arr, 3).stream().map(a -> "* " + a[0] + ", " + a[1]).forEach(System.out::println);

        arr = new int[]{1, 2, 3, 4, 5};
        pairs(arr, 1).stream().map(a -> "+ " + a[0] + ", " + a[1]).forEach(System.out::println);

        arr = new int[]{1, 3, 5, 7, 8, 9, 10, 11};
        pairs(arr, 6).stream().map(a -> "% " + a[0] + ", " + a[1]).forEach(System.out::println);
    }

    public static List<int[]> pairs(int[] arr, int k) {
        List<int[]> result = new ArrayList<>();

        if (arr == null || arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements but found "
                    + Arrays.toString(arr));
        }

        int scanStart = 0;
        int index = 1;

        while (index < arr.length) {
            int diff = arr[index] - arr[scanStart];
            if (diff > k) {
                scanStart++;
            } else if (diff < k) {
                index++;
            } else if (diff == k) {
                result.add(new int[]{arr[scanStart], arr[index]});
                scanStart++;
                index++;
            }
        }
        return result;
    }
}
