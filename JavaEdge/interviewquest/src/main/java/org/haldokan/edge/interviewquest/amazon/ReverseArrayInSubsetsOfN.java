package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * <p>
 * Reverse an array in subset of N. Example:
 * input: Array = [1,2,3,4,5,6,7,8,9], N = 3
 * output: [3,2,1,6,5,4,9,8,7]
 * <p>
 * Created by haytham.aldokanji on 7/6/16.
 */
public class ReverseArrayInSubsetsOfN {
    public static void main(String[] args) {
        ReverseArrayInSubsetsOfN driver = new ReverseArrayInSubsetsOfN();

        driver.testReverseRange();
        driver.testReverse();
    }

    public void reverse(int[] arr, int n) {
        int end = Math.min(arr.length, n);
        int index = 0;

        while (index < arr.length) {
            if (end <= arr.length) {
                reverseRange(arr, index, end);
            }
            index = end;
            end += n;
            if (end > arr.length) {
                end = arr.length;
            }
        }
    }

    private void reverseRange(int[] arr, int start, int end) {
        int mid = (start + end) / 2;
        int index = end - 1;
        for (int i = start; i < mid; i++) {
            int tmp = arr[i];
            arr[i] = arr[index];
            arr[index--] = tmp;
        }
    }

    private void testReverseRange() {
        int[] arr = new int[]{4, 2, 7, 3, 1, 9};
        reverseRange(arr, 0, arr.length);
        assertThat(arr, is(new int[]{9, 1, 3, 7, 2, 4}));

        arr = new int[]{4, 2, 7, 3, 1, 9, 6};
        reverseRange(arr, 0, arr.length);
        assertThat(arr, is(new int[]{6, 9, 1, 3, 7, 2, 4}));

        arr = new int[]{4, 2, 7, 3, 1, 9, 6};
        reverseRange(arr, 2, 5);
        assertThat(arr, is(new int[]{4, 2, 1, 3, 7, 9, 6}));
    }

    private void testReverse() {
        int[] arr = new int[]{4, 2, 7, 3, 1, 9, 11, 13, 12};
        reverse(arr, 3);
        assertThat(arr, is(new int[]{7, 2, 4, 9, 1, 3, 12, 13, 11}));

        arr = new int[]{4, 2, 7, 3, 1, 9, 11, 13, 12, 55, 77};
        reverse(arr, 3);
        assertThat(arr, is(new int[]{7, 2, 4, 9, 1, 3, 12, 13, 11, 77, 55}));

        arr = new int[]{4, 2, 7, 3, 1, 9, 11, 13, 12, 55, 77};
        reverse(arr, arr.length);
        assertThat(arr, is(new int[]{77, 55, 12, 13, 11, 9, 1, 3, 7, 2, 4}));

        arr = new int[]{4, 2, 7, 3, 1, 9, 11, 13, 12, 55, 77};
        reverse(arr, 700);
        assertThat(arr, is(new int[]{77, 55, 12, 13, 11, 9, 1, 3, 7, 2, 4}));
    }
}
