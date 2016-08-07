package org.haldokan.edge.interviewquest.bloomberg;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question
 * <p>
 * The Question: 3_STAR
 * <p>
 * Given a sorted array of integers, using the same array, shuffle the integers to have unique elements and return the index.
 * <p>
 * Sample input : [3, 3, 4, 5, 5, 6, 7, 7, 7]
 * Sample output : [3, 4, 5, 6, 7, X, X, X, X]
 * In this case, it returns an index of 4.
 * The elements in the array after that index is negligible (don't care what value it is).
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class RemoveDupsFromSortedArrayInSitu {

    public static void main(String[] args) {
        RemoveDupsFromSortedArrayInSitu driver = new RemoveDupsFromSortedArrayInSitu();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
        driver.test7();
    }

    public int makeUnique(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("arr is null or empty");
        }

        int i = 0;
        for (int j = 1; j < arr.length; j++) {
            if (arr[i] != arr[j]) {
                arr[++i] = arr[j];
            }
        }
        // return the length of the new array
        return i + 1;
    }

    private void test1() {
        int[] arr = new int[]{3, 3, 4, 5, 5, 6, 7, 7, 7};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(5));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7}));
    }

    private void test2() {
        int[] arr = new int[]{3, 3, 4, 5, 5, 6, 7, 7, 7, 8};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(6));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7, 8}));
    }

    private void test3() {
        int[] arr = new int[]{3, 3, 4, 5, 5, 6, 7, 7, 7, 8};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(6));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7, 8}));
    }

    private void test4() {
        int[] arr = new int[]{3, 3, 4, 5, 5, 6, 7, 7, 7, 8, 8};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(6));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7, 8}));
    }

    private void test5() {
        int[] arr = new int[]{3, 4, 5, 6, 7, 8};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(6));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7, 8}));
    }

    private void test6() {
        int[] arr = new int[]{3, 4, 4, 4, 5, 6, 6, 6, 7, 8, 8, 9};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(7));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3, 4, 5, 6, 7, 8, 9}));
    }

    private void test7() {
        int[] arr = new int[]{3, 3, 3, 3, 3, 3};
        int lastIndex = makeUnique(arr);
        System.out.printf("%s%n", Arrays.toString(arr));

        assertThat(lastIndex, is(1));
        assertThat(Arrays.copyOf(arr, lastIndex), is(new int[]{3}));
    }

}
