package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

/**
 * My solution of a Facebook interview question. Look at SortingBuyIndex2.java for another solution using quick sort
 * The Question: 3_STAR
 * We have an array of objects A and an array of indexes B. Reorder objects in array A with given indexes in array B.
 * Do not change array A's length.
 * <p>
 * example:
 * <p>
 * <p>
 * var A = [C, D, E, F, G];
 * var B = [3, 0, 4, 1, 2];
 * <p>
 * sort(A, B);
 * A is now [D, F, G, C, E];
 */
public class SortingByIndex {

    public static void main(String[] args) {
        String[] arr = new String[]{"C", "D", "E", "F", "G"};
        int[] indexes = new int[]{3, 0, 4, 1, 2};

        System.out.println(Arrays.toString(sort(arr, indexes)));
    }

    private static String[] sort(String[] arr, int[] indexes) {
        String[] transposed = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            transposed[indexes[i]] = arr[i];
        }
        return transposed;
    }
}
