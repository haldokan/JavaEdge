package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * You are going to take some numbers as an input from a file. You need to write
 * a program to find longest increasing sequence. You should process it as soon as you are taking an input.
 * After finishing the last input immediately you should be able to tell the sequence. Input: 1 5 3 4 6 4 Output: 3 4 6
 * <p>
 * Created by haytham.aldokanji on 5/4/16.
 */
public class LongestIncreasingSequence {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 2, 3, 1};
        assertThat(sequenceBounds(arr), is(new int[]{1, 4}));

        arr = new int[]{1, 2, 3, 1, 2, 3, 4, 3};
        assertThat(sequenceBounds(arr), is(new int[]{3, 7}));

        arr = new int[]{1, 2, 3, 1, 2, 3, 4};
        assertThat(sequenceBounds(arr), is(new int[]{3, 7}));

        arr = new int[]{1, 2, 3};
        assertThat(sequenceBounds(arr), is(new int[]{0, 3}));

        arr = new int[]{3, 2, 1};
        assertThat(sequenceBounds(arr), is(new int[]{0, 1}));

        arr = new int[]{1, 2};
        assertThat(sequenceBounds(arr), is(new int[]{0, 2}));
    }

    public static int[] sequenceBounds(int[] arr) {
        if (arr.length == 1) {
            return new int[]{0, 1};
        }

        int longestStart = 0, longestEnd = 1, currSeqStart = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] <= arr[i - 1]) {
                if (i - currSeqStart > longestEnd - longestStart) {
                    longestStart = currSeqStart;
                    longestEnd = i;
                }
                currSeqStart = i;
            } else if (i == arr.length - 1) {
                longestStart = currSeqStart;
                longestEnd = arr.length;
            }
        }
        return new int[]{longestStart, longestEnd};
    }
}
