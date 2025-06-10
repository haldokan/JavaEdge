package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question - Solution uses dynamic programming: a recurrence with an initial step is
 * found and each subsequent step is obtained based on the step that proceeds it
 * The Question: 4_STAR
 * input [2,3,1,4]
 * output [12,8,24,6]
 * <p>
 * Multiply all values except excluding the value at it's own position.
 * <p>
 * Restrictions:
 * 1. no use of division
 * 2. complexity in O(n)
 * <p>
 * Created by haytham.aldokanji on 5/7/16.
 */
public class ValueMultipInArrayWithDynamicProgramming {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 3, 1, 4};
        int[] result = multiply(arr);
        assertThat(result, is(new int[]{12, 8, 24, 6}));

        arr = new int[]{2, 3, 5, 4};
        result = multiply(arr);
        assertThat(result, is(new int[]{60, 40, 24, 30}));

        arr = new int[]{2, 3};
        result = multiply(arr);
        assertThat(result, is(new int[]{3, 2}));
    }

    public static int[] multiply(int[] arr) {
        if (arr == null) {
            throw new NullPointerException("Null value passed to function");
        }

        int[] result = new int[arr.length];
        if (arr.length == 1) {
            result[0] = arr[0];
            return result;
        }
        // recurrence initial step
        result[0] = arr[1];
        result[1] = arr[0];

        for (int i = 2; i < arr.length; i++) {
            result[i] = arr[i - 1] * result[i - 1];
        }

        int multiplier = 1;
        for (int i = arr.length - 1; i > 1; i--) {
            multiplier *= arr[i];
            // get the next step from the previous on
            result[i - 1] = multiplier * result[i - 1];
        }
        result[0] = multiplier * arr[1];

        return result;
    }
}
