package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
 * Return the pivot index of the given array of numbers. The pivot index is the index where the sum of the numbers on
 * the left is equal to the sum of the numbers on the right. Input Array {1,2,3,4,0,6}
 * <p>
 * Created by haldokanji on 12/4/16.
 */
public class ArrayPivotIndex {

    public static void main(String[] args) throws Exception {
        ArrayPivotIndex driver = new ArrayPivotIndex();
        driver.test();
    }

    public int pivot(int[] arr) {
        if (arr == null || arr.length < 3) {
            return -1;
        }

        int leftSum = arr[0];
        int rightSum = 0;

        for (int i = 2; i < arr.length; i++) {
            rightSum += arr[i];
        }

        for (int i = 1; i < arr.length - 1; i++) {
            if (leftSum == rightSum) {
                return i;
            }
            leftSum += arr[i];
            rightSum -= arr[i + 1];
        }
        return -1;
    }

    private void test() throws Exception {
        int[] arr = new int[]{1, 2, 3, 4, 0, 6};
        assertThat(pivot(arr), is(3));

        arr = new int[]{1, 2, 3, 4, 0, 6, 1, 3};
        assertThat(pivot(arr), is(4));

        arr = new int[]{2, 3, 4, 0, 6, 6, 15};
        assertThat(pivot(arr), is(5));

        arr = new int[]{15, 6, 6, 0, 4, 3, 2};
        assertThat(pivot(arr), is(1));

        arr = new int[]{1, 1, 1, 1, 1, 1, 5};
        assertThat(pivot(arr), is(5));
    }
}
