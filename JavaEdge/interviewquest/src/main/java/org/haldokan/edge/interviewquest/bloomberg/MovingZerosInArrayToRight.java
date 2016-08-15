package org.haldokan.edge.interviewquest.bloomberg;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question
 *
 * The Question: 3_STAR
 * Given an unsorted integer array, place all zeros to the end of the array without changing the sequence of non-zero
 * elements. (i.e. [1,3,0,8,12, 0, 4, 0,7] --> [1,3,8,12,4,7,0,0,0])
 */
public class MovingZerosInArrayToRight {

    public static void main(String[] args) {
        MovingZerosInArrayToRight driver = new MovingZerosInArrayToRight();
        driver.test1();
        driver.test2();
    }

    public void move(int[] arr) {
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
    }

    private void test1() {
        int[] arr = new int[]{1, 3, 0, 8, 12, 0, 4, 0, 7};
        move(arr);

        System.out.println(Arrays.toString(arr));
        assertThat(arr, is(new int[]{1, 3, 8, 12, 4, 7, 0, 0, 0}));
    }

    private void test2() {
        int[] arr = new int[]{1, 3, 0, 8, 12, 0, 4, 0, 7};
        move2(arr);

        System.out.println(Arrays.toString(arr));
        assertThat(arr, is(new int[]{1, 3, 8, 12, 4, 7, 0, 0, 0}));
    }

    // a much better version (found on-line) that runs in O(n)
    public void move2(int[] a) {
        int pos = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                a[pos++] = a[i];
            }
        }
        for (int i = pos; i < a.length; i++) {
            a[i] = 0;
        }
    }
}
