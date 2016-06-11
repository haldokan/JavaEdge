package org.haldokan.edge.interviewquest.bloomberg;

import java.util.Arrays;

/**
 * My solution to a Bloomberg interview question
 * The Question: 3_STAR
 * Given an unsorted integer array, place all zeros to the end of the array without changing the sequence of non-zero
 * elements. (i.e. [1,3,0,8,12, 0, 4, 0,7] --> [1,3,8,12,4,7,0,0,0])
 */
public class MovingZerosInArrayToRight {

    public static void main(String[] args) {
        MovingZerosInArrayToRight driver = new MovingZerosInArrayToRight();
        System.out.println(Arrays.toString(driver.move(new int[]{1, 3, 0, 8, 12, 0, 4, 0, 7})));
    }

    public int[] move(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                for (int j = i; j < arr.length; j++) {
                    if (arr[j] != 0) {
                        arr[i] = arr[j];
                        arr[j] = 0;
                        break;
                    }
                }
            }
        }
        return arr;
    }
}
