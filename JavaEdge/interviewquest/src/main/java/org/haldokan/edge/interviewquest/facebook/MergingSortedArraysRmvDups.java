package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * Given two arrays of sorted integers, merge them keeping in mind that there might be common elements in the arrays
 * and that common elements must only appear once in the merged array.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class MergingSortedArraysRmvDups {

    public static void main(String[] args) {
        MergingSortedArraysRmvDups driver = new MergingSortedArraysRmvDups();
        driver.test();
    }

    public int[] merge(int[] arr1, int[] arr2) {
        int[][] arrs = new int[][]{arr1, arr2};
        Arrays.sort(arrs, (a1, a2) -> a2.length - a1.length);

        int[] first = arrs[0];
        int[] second = arrs[1];

        int[] merged = new int[arr1.length + arr2.length];
        int secondIndex = 0;
        int mergeIndex = 0;

        for (int val : first) {
            while (secondIndex < second.length && val > second[secondIndex]) {
                int prevMergeIndex = mergeIndex - 1;
                if (prevMergeIndex < 0 || merged[prevMergeIndex] != second[secondIndex]) {
                    merged[mergeIndex++] = second[secondIndex];
                }
                secondIndex++;
            }
            int prevMergeIndex = mergeIndex - 1;
            if (prevMergeIndex < 0 || merged[prevMergeIndex] != val) {
                merged[mergeIndex++] = val;
            }
        }

        for (int i = secondIndex; i < second.length; i++) {
            int val = second[i];
            int prevMergeIndex = mergeIndex - 1;

            if (val != merged[prevMergeIndex]) {
                merged[mergeIndex++] = val;
            }
        }
        return Arrays.copyOf(merged, mergeIndex);
    }

    private void test() {
        int[] arr1 = new int[]{4, 4, 6, 8, 8, 10, 14, 14};
        int[] arr2 = new int[]{1, 1, 2, 3, 5, 5, 6, 7, 9, 11, 12, 13, 15, 15};
        int[] merged = merge(arr1, arr2);
        assertThat(merged, is(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));

        arr1 = new int[]{4, 4, 4, 5, 5, 5, 6, 7};
        arr2 = new int[]{1, 1, 3, 3, 6, 7};
        merged = merge(arr1, arr2);
        assertThat(merged, is(new int[]{1, 3, 4, 5, 6, 7}));

        arr1 = new int[]{1, 2, 3, 4, 4, 4};
        arr2 = new int[]{4, 4, 4, 10, 10, 12, 12};
        merged = merge(arr1, arr2);
        assertThat(merged, is(new int[]{1, 2, 3, 4, 10, 12}));

        arr1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        arr2 = new int[]{9, 10, 10, 12, 12};
        merged = merge(arr1, arr2);
        assertThat(merged, is(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12}));
    }
}
