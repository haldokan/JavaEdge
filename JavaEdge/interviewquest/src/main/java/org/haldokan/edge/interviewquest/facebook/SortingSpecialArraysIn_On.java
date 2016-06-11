package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * You are given an array of 1's 2's and 3's. Sort this list so the 1's are first, the 2's come second, and the 3's come third.
 * <p>
 * Ex: Input [1, 3, 3, 2, 1]
 * Output [1, 1, 2, 3, 3]
 * <p>
 * But there is a catch!! The algorithm must be one pass, which means no merge/quick sort.
 * Also no extra list allocations are allowed, which means no bucket/radix/counting sorts.
 * <p>
 * You are only permitted to swap elements.
 * Created by haytham.aldokanji on 4/23/16.
 */
public class SortingSpecialArraysIn_On {

    public static void main(String[] args) {
        int[] arr = new int[]{3, 2, 3, 2, 1, 3, 3, 2, 3, 3, 2, 1, 2, 3, 1};
        specialSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void specialSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                int j = i;
                while (j > 0 && arr[j - 1] != 1) {
                    swap(arr, j - 1, j);
                    j--;
                }
            } else if (arr[i] == 2) {
                int j = i;
                while (j > 0 && arr[j - 1] != 2 && arr[j - 1] != 1) {
                    swap(arr, j - 1, j);
                    j--;
                }
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        arr[i] ^= arr[j];
        arr[j] ^= arr[i];
        arr[i] ^= arr[j];
    }
}
