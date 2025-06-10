package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - World population is about 7.5 billions so we cannot fit their ages in an
 * array since Integer max value is about 2 billions and 147 million. I assumed that we can fit the array in memory and
 * provided 2 solutions: one assuming the array is sorted and another assuming it is not.
 *
 * The Question: 3_STAR
 *
 * You have a sorted array containing the age of every person on Earth.
 * [0, 0, 0, 0, ..., 1, 1, ..., 28, 28, ..., 110, ...]
 * Find out how many people have each age.
 * <p>
 * Created by haytham.aldokanji on 5/31/16.
 */
public class NumberRepeatedValsInSortedArray {

    public static void main(String[] args) {
        NumberRepeatedValsInSortedArray driver = new NumberRepeatedValsInSortedArray();

        driver.testNotSorted();
        driver.testFindAgeGroupEndIndex();
        driver.testSorted();
        driver.testRandomPopulationArray();
    }


    public int[] ageGroupSizes_sorted(byte arr[]) {
        int maxAge = arr[arr.length - 1];
        int[] ageGroupSizes = new int[maxAge + 1];

        int startIndex = -1;
        for (byte age = 0; age <= maxAge; age++) {
            int currentEndIndex = findAgeGroupEndIndex(arr, age, startIndex, arr.length);
            ageGroupSizes[age] += currentEndIndex - startIndex;
            startIndex = currentEndIndex;
        }
        return ageGroupSizes;
    }

    private int findAgeGroupEndIndex(byte[] arr, byte age, int start, int end) {
        if (end < start) {
            return -end;
        }
        int mid = (start + end) / 2;

        if (arr[mid] == age) {
            int next = mid + 1;
            if (next == arr.length || arr[next] - arr[mid] == 1) {
                return mid;
            } else {
                return findAgeGroupEndIndex(arr, age, next, end);
            }
        } else if (age < arr[mid]) {
            return findAgeGroupEndIndex(arr, age, start, mid - 1);
        } else {
            return findAgeGroupEndIndex(arr, age, mid + 1, end);
        }
    }

    // very concise solution for unsorted age array in O(n) time and O(1) space
    public int[] ageGroupSizes_notSorted(byte arr[]) {
        int maxAge = 150;
        int[] ageGroupSizes = new int[maxAge];
        for (byte age : arr) {
            ageGroupSizes[age]++;
        }
        return ageGroupSizes;
    }

    private void testNotSorted() {
        byte[] arr = new byte[]{1, 4, 2, 4, 2, 3, 4, 3, 4, 3};
        int[] result = ageGroupSizes_notSorted(arr);
        assertThat(Arrays.copyOfRange(result, 1, 5), is(new int[]{1, 2, 3, 4}));
    }

    private void testFindAgeGroupEndIndex() {
        byte[] arr = new byte[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 6};
        int index = findAgeGroupEndIndex(arr, (byte) 2, 0, arr.length);
        assertThat(index, is(2));

        // pass end last index of age 2 instead of 0
        index = findAgeGroupEndIndex(arr, (byte) 3, 2, arr.length);
        assertThat(index, is(5));

        // pass end last index of age 3 instead of 0
        index = findAgeGroupEndIndex(arr, (byte) 4, 5, arr.length);
        assertThat(index, is(9));

        index = findAgeGroupEndIndex(arr, (byte) 1, 0, arr.length);
        assertThat(index, is(0));

        index = findAgeGroupEndIndex(arr, (byte) 5, 9, arr.length);
        assertThat(index, is(10));

        index = findAgeGroupEndIndex(arr, (byte) 6, 10, arr.length);
        assertThat(index, is(11));

        arr = new byte[]{1};
        index = findAgeGroupEndIndex(arr, (byte) 1, 0, arr.length);
        assertThat(index, is(0));

        arr = new byte[]{2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        index = findAgeGroupEndIndex(arr, (byte) 2, 0, arr.length);
        assertThat(index, is(1));

        arr = new byte[]{2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4};
        index = findAgeGroupEndIndex(arr, (byte) 3, 0, arr.length);
        assertThat(index, is(arr.length - 2));

        arr = new byte[]{2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4};
        index = findAgeGroupEndIndex(arr, (byte) 3, 0, arr.length);
        assertThat(index, is(arr.length - 3));

        arr = new byte[]{2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4};
        index = findAgeGroupEndIndex(arr, (byte) 4, 0, arr.length);
        assertThat(index, is(arr.length - 1));
    }

    private void testSorted() {
        byte[] arr = new byte[]{0, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 6};
        int[] ageGroupSizes = ageGroupSizes_sorted(arr);
        assertThat(ageGroupSizes, is(new int[]{1, 1, 2, 3, 4, 1, 1}));
    }

    private void testRandomPopulationArray() {
        byte[] arr = makePopulation();
        int[] ageGroupSizes = ageGroupSizes_sorted(arr);
//        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(ageGroupSizes));
    }

    private byte[] makePopulation() {
        int worldPopulation = 1000_000;
        byte[] population = new byte[worldPopulation];
        int maxAge = 99;

        Random random = new Random();
        for (int i = 0; i < population.length; i++) {
            byte val = (byte) (i % maxAge);
            if (random.nextInt(10) < 7) {
                val += (byte) random.nextInt(17);
            }
            population[i] = val;
        }
        Arrays.sort(population);

        return population;
    }
}
