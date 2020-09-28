package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.Random;

/**
 * My solution of a Facebook interview question
 *
 * The Question: 4-STAR
 *
 * Given an array of integers, randomly return an index of the maximum value seen so far.
 *
 * e.g.
 * Given [11,30,2,30,30,30,6,2,62,62]
 *
 * Having iterated up to element index 5 (where is the last 30), randomly give an index among [1, 3, 4, 5] which
 * are indices of 30 - the max value so far. Each index should have a 1/4 chance to get picked.
 *
 * Having iterated through the entire array, randomly give an index between 8 and 9 which are indices of the max value 62.
 */
public class ArrayIndexOfMaxValWithProbability {
    private final Random random = new Random();
    private final ListMultimap<Integer, Integer> maxValueIndexesSoFarByIndex;
    private final int[] arr;

    public static void main(String[] args) {
        ArrayIndexOfMaxValWithProbability driver = new ArrayIndexOfMaxValWithProbability(new int[]{11,30,2,30,30,30,6,2,62,62});
        driver.test();
        System.out.println("-----------");
        driver = new ArrayIndexOfMaxValWithProbability(new int[]{30, 30, 11, 7, 1, 62});
        driver.test();
    }

    public ArrayIndexOfMaxValWithProbability(int[] input) {
        this.arr = input;
        this.maxValueIndexesSoFarByIndex = mapMaxValIndexes();
    }

    public int indexOfMaxValSoFarWithProbability(int index) {
        if (index < 0 || index > maxValueIndexesSoFarByIndex.size() - 1) {
            throw new IllegalArgumentException(String.format("Index out of bound: %d%n", index));
        }
        List<Integer> indexes = maxValueIndexesSoFarByIndex.get(index);
        return indexes.get(random.nextInt(indexes.size()));
    }

    private ListMultimap<Integer, Integer> mapMaxValIndexes() {
        ListMultimap<Integer, Integer> maxValueIndexesSoFarByIndex = ArrayListMultimap.create();
        ListMultimap<Integer, Integer> maxValueIndexesByValue = ArrayListMultimap.create();
        Integer maxVal = null;

        for (int i = 0; i < arr.length; i++) {
            int currentVal = arr[i];

            if (maxVal == null || currentVal > maxVal) {
                maxVal = currentVal;
                maxValueIndexesByValue.put(currentVal, i);
                maxValueIndexesSoFarByIndex.put(i, i);
            } else if (currentVal == maxVal) {
                maxValueIndexesSoFarByIndex.putAll(i, maxValueIndexesByValue.get(currentVal));
                maxValueIndexesSoFarByIndex.put(i, i);
                maxValueIndexesByValue.put(currentVal, i);
            } else {
                maxValueIndexesSoFarByIndex.putAll(i, maxValueIndexesByValue.get(maxVal));
            }
        }
        return maxValueIndexesSoFarByIndex;
    }

    private void test() {
        // not that multimap.keys repeat the key for each key value which is nice for this test - we can use keySet to remove repetition
        maxValueIndexesSoFarByIndex.keys().forEach(key ->
                System.out.printf("index/value %d/%d -> maxValIndex: %d%n", key, arr[key], indexOfMaxValSoFarWithProbability(key)));
    }
}
