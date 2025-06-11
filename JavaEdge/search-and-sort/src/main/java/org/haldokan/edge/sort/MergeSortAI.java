package org.haldokan.edge.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * ✅ Advantages
 * Stable sort: Maintains the order of equal elements.
 * Consistent performance: Always O(n log n).
 * Great for linked lists, external sorting, and large data sets that don’t fit in memory.
 * <p>
 * ❌ Disadvantages
 * Requires additional space (unlike in-place sorts like quicksort).
 * Typically slower on small arrays due to recursion overhead.
 */
public class MergeSortAI {
    private void sort(int[] arr) {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        var left = Arrays.copyOfRange(arr, 0, mid);
        var right = Arrays.copyOfRange(arr, mid, arr.length);
        sort(left);
        sort(right);

        merge(arr, left, right);
    }
    // Note how it writes to the original array
    private void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        while (j < left.length && k < right.length) {
            if (left[j] < right[k]) {
                arr[i++] = left[j++];
            } else {
                arr[i++] = right[k++];
            }
        }
        while (j < left.length) {
            arr[i++] = left[j++];
        }
        while (k < right.length) {
            arr[i++] = right[k++];
        }
    }

    @Test
    public void testIt() {
        int[] arr = {7, 2, 8, 1, 4, 11, 9, 10, 3};
        System.out.println("Before sorting: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("After sorting: " + Arrays.toString(arr));
    }
}
