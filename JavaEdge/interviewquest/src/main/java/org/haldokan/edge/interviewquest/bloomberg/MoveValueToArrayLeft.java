package org.haldokan.edge.interviewquest.bloomberg;

import java.util.Arrays;

/**
 * My solution to a Bloomberg interview question
 * The Question: 3_STAR
 * You have an array of ints. Move all elements of a specific value to the left while keep the sequence.
 * Ex: move all 2s to the left in [5, 2, 3, 2, 2, 7, 5, 2, 9, 8]
 */
public class MoveValueToArrayLeft {

    public static void main(String[] args) {
        int[] arr = new int[]{5, 2, 3, 2, 2, 7, 5, 2, 9, 8};
        move(arr, 2);
        System.out.println(Arrays.toString(arr));
    }

    public static void move(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            int j = i;
            if (arr[j] == val) {
                while (j > 0 && arr[j - 1] != val) {
                    int tmp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = tmp;
                    j--;
                }
            }
        }
    }
}
