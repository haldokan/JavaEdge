package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - simple when Fibonacci is solved using dynamic programming rather than
 * recursion
 * <p>
 * Assume we only take the least significant digit of each value in fibonacci sequence, and form the sequence of digits
 * into pairs. In those pairs, the first value of one pair is the same as second value of its predecessor.
 * <p>
 * As we know the fibonacci sequence is 1, 1, 2, 3, 5, 8, 13, 21...
 * so the pair sequence is:
 * [1, 1], [1, 2], [2, 3], [3, 5], [5, 8], [8, 13], [13, 21] ...
 * <p>
 * Write a function to output the first n pairs of this sequence.
 * void Outputpairs(int n)
 * Created by haytham.aldokanji on 5/22/16.
 */
public class GenerateNewSeqBasedOnFibonacci {
    public static void main(String[] args) {
        GenerateNewSeqBasedOnFibonacci driver = new GenerateNewSeqBasedOnFibonacci();

        List<long[]> sequence = driver.fibBasedSequence(8);
        sequence.stream().forEach(e -> System.out.println(Arrays.toString(e)));
        assertThat(sequence, contains(
                        new long[]{1, 1},
                        new long[]{1, 2},
                        new long[]{2, 3},
                        new long[]{3, 5},
                        new long[]{5, 8},
                        new long[]{8, 13},
                        new long[]{13, 21})
        );
    }

    public List<long[]> fibBasedSequence(long sequence) {
        List<long[]> result = new ArrayList<>();
        long nextNum = 1;

        if (sequence == 1) {
            return result;
        }
        result.add(new long[]{nextNum, nextNum});

        long num1 = 1;
        long num2 = 1;
        // set the dynamic programming recurrence in motion
        for (int i = 3; i <= sequence; i++) {
            nextNum = num1 + num2;
            result.add(new long[]{num2, nextNum});
            num1 = num2;
            num2 = nextNum;
        }
        return result;
    }
}
