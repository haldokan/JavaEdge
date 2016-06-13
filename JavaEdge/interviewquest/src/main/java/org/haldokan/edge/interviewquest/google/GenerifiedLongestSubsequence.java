package org.haldokan.edge.interviewquest.google;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - using dynamic programming to solve the problem at every new element of
 * the array we keep track of the longest sequence that can be formed from that element and link the element to the previous
 * sequence index. Once the tracking table is constructed we find the longest sequence length and track back the links in
 * reverse order to build the longest sequence. The complexity of the algorithm: At every index i we
 * have to scan all elements less than i so the number of iterations is the sum of this sequence 0 + 1 + 2 + 3 + ..... + n
 * which is quadratic: O(n^2).
 * <p>
 * The Question: 5_STAR
 * <p>
 * You are given an unsorted sequence of integers 'a'. Find the longest subsequence 'b' such that elements of this
 * subsequence are strictly increasing numbers. Elements in the subsequence 'b' must appear in the same relative order
 * as in the sequence 'a'. You may assume that 'a' can fit to the memory.
 * <p>
 * Example:
 * input: a = [-1 2 100 100 101 3 4 5 -7]
 * output: b = [-1 2 3 4 5]
 * Created by haytham.aldokanji on 5/27/16.
 */
public class GenerifiedLongestSubsequence<T> {
    private final Comparator<T> comparator;
    private final Class<T> klass;

    public GenerifiedLongestSubsequence(Comparator<T> comparator, Class<T> klass) {
        this.comparator = comparator;
        this.klass = klass;
    }

    public static void main(String[] args) {
        testSequenceTracker();
        testLongestSequence();
    }

    private static void testSequenceTracker() {
        GenerifiedLongestSubsequence<Integer> driver =
                new GenerifiedLongestSubsequence<>((v1, v2) -> v1 - v2, Integer.class);

        Integer[] arr = new Integer[]{-1, 2, 100, 100, 101, 3, 4, 5, -7, -6};
        int[][] seqTracker = driver.buildSequenceTracker(arr);
        assertThat(seqTracker[0], is(new int[]{1, 0}));
        assertThat(seqTracker[1], is(new int[]{2, 0}));
        assertThat(seqTracker[2], is(new int[]{3, 1}));
        assertThat(seqTracker[3], is(new int[]{3, 1}));
        assertThat(seqTracker[4], is(new int[]{4, 3}));
        assertThat(seqTracker[5], is(new int[]{3, 1}));
        assertThat(seqTracker[6], is(new int[]{4, 5}));
        assertThat(seqTracker[7], is(new int[]{5, 6}));
        assertThat(seqTracker[8], is(new int[]{1, 8}));
        assertThat(seqTracker[9], is(new int[]{2, 8}));
    }

    private static void testLongestSequence() {
        GenerifiedLongestSubsequence<Integer> driver =
                new GenerifiedLongestSubsequence<>((v1, v2) -> v1 - v2, Integer.class);

        Integer[] arr = new Integer[]{-1, 2, 100, 100, 101, 3, 4, 5, -7, -6};
        Integer[] longestSeq = driver.longestSequence(arr);
        assertThat(longestSeq, is(new Integer[]{-1, 2, 3, 4, 5}));

        arr = new Integer[]{-1, 2, 100, 100, 101, 3, 4, 5, -7, -6, -4, -3, -1, 0};
        longestSeq = driver.longestSequence(arr);
        assertThat(longestSeq, is(new Integer[]{-7, -6, -4, -3, -1, 0}));

        arr = new Integer[]{-1, 2, 100, 100, 101, 3, 4, 5, -7, -6, -4, 9, 11, -3, -1, 0};
        longestSeq = driver.longestSequence(arr);
        assertThat(longestSeq, is(new Integer[]{-1, 2, 3, 4, 5, 9, 11}));
    }

    public T[] longestSequence(T[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Invalid input: "
                    + (arr == null ? null : Arrays.toString(arr)));
        }
        if (arr.length == 1) {
            return Arrays.copyOf(arr, arr.length);
        }

        int[][] seqTracker = buildSequenceTracker(arr);

        Deque<Integer> sequenceStack = traceLongestSequence(seqTracker);
        // could have used Guava Lists.reverse but I am staying with arrays
        @SuppressWarnings("unchecked")
        T[] longestSequence = (T[]) Array.newInstance(klass, sequenceStack.size());

        int index = 0;
        while (!sequenceStack.isEmpty()) {
            longestSequence[index++] = arr[sequenceStack.remove()];
        }

        return longestSequence;
    }

    private int[][] buildSequenceTracker(T arr[]) {
        int[][] seqTracker = new int[arr.length][2];
        seqTracker[0][0] = 1; // count
        seqTracker[0][1] = 0; // index

        for (int activeIndex = 1; activeIndex < arr.length; ++activeIndex) {
            int k = activeIndex - 1;
            int longestForActiveIndex = 0;
            int bestPreviousIndex = activeIndex;
            while (k >= 0) {
                if (comparator.compare(arr[k], arr[activeIndex]) < 0) {
                    int currentSeqLen = seqTracker[k][0];
                    if (currentSeqLen > longestForActiveIndex) {
                        longestForActiveIndex = currentSeqLen;
                        bestPreviousIndex = k;
                    }
                }
                k--;
            }
            seqTracker[activeIndex][0] = longestForActiveIndex + 1;
            seqTracker[activeIndex][1] = bestPreviousIndex;
        }
        return seqTracker;
    }

    private Deque<Integer> traceLongestSequence(int[][] sequenceTracker) {
        int[] maxSeqLocation = null;
        int maxSeqIndex = -1;
        // not sure we can do that using streams in one scan since we need the value and its index
        for (int i = 0; i < sequenceTracker.length; i++) {
            int[] currentSeqLocation = sequenceTracker[i];
            if (maxSeqLocation == null || currentSeqLocation[0] > maxSeqLocation[0]) {
                maxSeqLocation = currentSeqLocation;
                maxSeqIndex = i;
            }
        }
        // will never be - silence IDEA code analysis warning
        assert maxSeqLocation != null;

        int currentSeqLen = maxSeqLocation[0];
        // has the indexes of the longest sequence
        Deque<Integer> sequenceStack = new ArrayDeque<>();
        sequenceStack.addFirst(maxSeqIndex);

        int[] currentSeqLocation = maxSeqLocation;
        while (currentSeqLen > 1) {
            sequenceStack.addFirst(currentSeqLocation[1]);
            // push the index (int) instead of the element itself which can be long (or any object) - save on memory
            currentSeqLocation = sequenceTracker[currentSeqLocation[1]];
            currentSeqLen = currentSeqLocation[0];
        }
        return sequenceStack;
    }

}
