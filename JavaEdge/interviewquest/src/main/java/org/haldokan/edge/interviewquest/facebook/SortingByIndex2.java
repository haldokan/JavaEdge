package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

/**
 * My solution of a Facebook interview question - used quick sort with a twist. Look at SortingByIndex.java for a better solution
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
public class SortingByIndex2 {

    public static void main(String[] args) {
        String[] arr = new String[]{"C", "D", "E", "F", "G"};
        int[] indexes = new int[]{3, 0, 4, 1, 2};

        sort(arr, indexes, 0, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    // let's impl quick sort adding the twist this question requires
    private static void sort(String[] arr, int[] indexes, int start, int end) {
        int pivot = (start + end) / 2;

        if (end <= start) {
            return;
        }
        for (int i = 0; i < pivot; i++) {
            if (indexes[i] > indexes[pivot]) {
                String tmpVal = arr[i];
                arr[i] = arr[pivot];
                arr[pivot] = tmpVal;

                int tmpIndex = indexes[i];
                indexes[i] = indexes[pivot];
                indexes[pivot] = tmpIndex;
            }
        }
        sort(arr, indexes, start, pivot);
        sort(arr, indexes, pivot + 1, end);
    }
}
