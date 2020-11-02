package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * My solution to a Linkedin question - simpler version of weighted probability facebook questions I solved elsewhere in this repo
 *
 * The Question: 3.5-STAR
 *
 * Given an array frequency where freq[i] represents the occurrence frequency of the ith element in the array, how to randomly
 * select an element in the array based on its frequency.
 */
public class SelectArrayItemBasedOnProbability<T> {
    private final static Random random = new Random();
    private final T[] arr;
    private final int[] freq;
    private final int freqRange;

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{3, 7, 1, 2, 5, 9, 4, 6, 8, 0}; // this stupid incompatible duality in Java b/w native types and their objects
        int[] freq = new int[] {1, 2, 3, 4, 20, 6, 7, 8, 9, 30}; // we should see many zero freq-random selected
        SelectArrayItemBasedOnProbability<Integer> driver = new SelectArrayItemBasedOnProbability<>(arr, freq);

        // we should see many 0s and 5s freq-random selected
        Map<Integer, Integer> selectFreq = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            int selected = driver.freqBasedRandomNum();
            selectFreq.compute(selected, (k, v) -> v == null ? 1 : v + 1);
        }
        System.out.printf("%s%n,", selectFreq);
    }

    public SelectArrayItemBasedOnProbability(T[] arr, int[] freq) {
        this.arr = arr;
        this.freq = freq;
        freqRange = Arrays.stream(freq).sum();
    }

    public T freqBasedRandomNum() {
        int randomIndex = random.nextInt(freqRange);
        int accumulatedFreq = 0;
        for (int i = 0; i < arr.length; i++) {
            accumulatedFreq += freq[i];
            if (accumulatedFreq >= randomIndex) {
                return arr[i];
            }
        }
        throw new IllegalStateException("should never happen if algorithm is correct: rand-index: %d - accum-freq: %d%n");
    }
}
