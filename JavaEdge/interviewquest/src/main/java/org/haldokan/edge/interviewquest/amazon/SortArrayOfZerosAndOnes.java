package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;

/**
 * Neat solution to an Amazon interview question
 * Given an array of zeros and  ones sort it so that all 1s move to the left and 0s to the right
 */
public class SortArrayOfZerosAndOnes {
    public static void main(String[] args) {
        int[] arr = new int[]{0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0};
        sort(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{0, 0, 0, 1, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{0, 1, 0, 1, 0, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{1, 0, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    static void sort(int[] arr) {
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                arr[index++] = 1;
            }
        }
        for (int i = index; i < arr.length; i++) {
            arr[i] = 0;
        }
    }
}
