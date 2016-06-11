package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * Given an array and a value, find min sub array whose sum is no less than value. Solution must be in O(n)
 * Created by haytham.aldokanji on 5/4/16.
 */
public class MinSubArrayWithSumGEValue {

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 1, 1, 4};
        int[] bounds = minArrayBounds(arr, 5);
        assertThat(bounds, is(new int[]{3, 5}));

        arr = new int[]{1, 2, 5, 1, 4};
        bounds = minArrayBounds(arr, 5);
        assertThat(bounds, is(new int[]{2, 3}));

        arr = new int[]{4, 1, 1, 1, 2, 1};
        bounds = minArrayBounds(arr, 5);
        assertThat(bounds, is(new int[]{0, 2}));

        arr = new int[]{1, 1, 1, 1, 1, 2, 2, 3, 2, 1, 5};
        bounds = minArrayBounds(arr, 6);
        assertThat(bounds, is(new int[]{9, 11}));

        arr = new int[]{1, 2, 3, 4, 5, 1, 9};
        bounds = minArrayBounds(arr, 10);
        assertThat(bounds, is(new int[]{5, 7}));

        arr = new int[]{10, 1, 2, 3, 4, 5, 1, 9};
        bounds = minArrayBounds(arr, 10);
        assertThat(bounds, is(new int[]{0, 1}));

        arr = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1};
        bounds = minArrayBounds(arr, 10);
        assertThat(bounds, is(new int[]{11, 13}));
    }

    public static int[] minArrayBounds(int[] arr, int limitVal) {
        int startMinSeq = 0, endMinSeq = arr.length;
        int startCurrSeq = 0, endCurrSeq;
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            int val = arr[i];
            sum += val;

            if (sum >= limitVal) {
                int start = startCurrSeq;
                for (int k = startCurrSeq; k <= i; k++) {
                    sum -= arr[k];
                    start = k;
                    if (sum < limitVal) {
                        sum += arr[k];
                        break;
                    }
                }
                startCurrSeq = start;
                endCurrSeq = i;
                if (endCurrSeq - startCurrSeq < endMinSeq - startMinSeq) {
                    startMinSeq = startCurrSeq;
                    endMinSeq = endCurrSeq;
                }
            }
        }
        return new int[]{startMinSeq, endMinSeq + 1};
    }
}
