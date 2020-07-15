package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * My solution to an Amazon interview question - I don't think it can be solved in-situ
 *
 * The Question: 3.5-STAR
 *
 * Given an array of positive and negative integers {-1,6,9,-4,-10,-9,8,8,4} (repetition allowed) arrange the array in a way
 * such that starting from a positive number, the elements should be arranged as one positive and one negative element
 * maintaining insertion order. First element should be starting from positive integer and the resultant array should look
 * like {6,-1,9,-4,8,-10,8,-9,4}
 */
public class ArrangeArrayOfPositiveAndNegativeIntegers {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(arrange(new int[]{-1, 6, 9, -4, -10, -9, 8, 8, 4})));
        System.out.println(Arrays.toString(arrange(new int[]{-1, -2, -3, -4, 1, 2, 3, 4})));
        System.out.println(Arrays.toString(arrange(new int[]{1, 2, 3, 4, -1, -2, -3, -4})));
    }

    static int[] arrange(int[] arr) {
        int pIndex = 0;
        int nIndex = 0;

        int index = 0;
        int[] result = new int[arr.length];

        while (index < result.length) {
            pIndex = nextIndex(arr, pIndex, val -> val >= 0);
            nIndex = nextIndex(arr, nIndex, val -> val < 0);

            if (pIndex >= 0) {
                result[index++] = arr[pIndex];
            }
            if (nIndex >= 0) {
                result[index++] = arr[nIndex];
            }

            pIndex++;
            nIndex++;
        }
        return result;
    }

    static int nextIndex(int[] arr, int currentIndex, Function<Integer, Boolean> function) {
        while (currentIndex < arr.length) {
            if (function.apply(arr[currentIndex])) {
                return currentIndex;
            }
            currentIndex++;
        }
        return -1;
    }
}
