package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question
 * <p>
 * Given an array of positive integers (excluding zero) and a target number. Detect whether there is a set of
 * consecutive elements in the array that add up to the target.
 * <p>
 * Example: a = {1, 3, 5, 7, 9}
 * target = 8
 * <p>
 * output = true ({3, 5})
 * <p>
 * or target = 15
 * output = true : {3, 5, 7}
 * <p>
 * but if target = 6, output would be false. since 1 and 5 are not next to each other.
 * <p>
 * Created by haytham.aldokanji on 4/27/16.
 */
public class ContinuousSeqAddingToValInArray {

    public static void main(String[] args) {
        ContinuousSeqAddingToValInArray driver = new ContinuousSeqAddingToValInArray();
        driver.test();
    }

    public Sequence detectSequence(int[] arr, int targetVal) {
        int start = 0;
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            int currVal = arr[i];
            sum += currVal;

            if (currVal == targetVal) {
                return new Sequence(arr, i, i, currVal, true);
            } else if (sum == targetVal) {
                return new Sequence(arr, start, i, sum, true);
            } else if (arr[i] > targetVal) {
                start = i + 1;
                sum = 0;
            } else if (sum > targetVal) {
                Sequence seq = adjustSeqBounds(new Sequence(arr, start, i, sum), targetVal);
                if (seq.found) {
                    return new Sequence(seq);
                } else {
                    start = seq.start;
                    sum = seq.sum;
                }
            }
        }
        return new Sequence(arr, -1, -1, -1);
    }

    private Sequence adjustSeqBounds(Sequence seq, int targetVal) {
        int sum = seq.sum;

        for (int i = seq.start; i < seq.end - 1; i++) {
            sum -= seq.arr[i];
            if (sum <= targetVal) {
                return new Sequence(seq.arr, i + 1, seq.end, sum, sum == targetVal);
            }
        }
        return new Sequence(seq.arr, seq.end + 1, -1, 0);
    }

    private void test() {
        int[] arr1 = new int[]{1, 3, 5, 7, 9, 12, 10, 1};

        Sequence seq = detectSequence(arr1, 8);
        System.out.println(seq);
        assertThat(seq.found, is(true));

        seq = detectSequence(arr1, 15);
        System.out.println(seq);
        assertThat(seq.found, is(true));

        seq = detectSequence(arr1, 11);
        System.out.println(seq);
        assertThat(seq.found, is(true));

        seq = detectSequence(arr1, 7);
        System.out.println(seq);
        assertThat(seq.found, is(true));

        seq = detectSequence(arr1, 6);
        System.out.println(seq);
        assertThat(seq.found, is(false));
    }
    
    private static class Sequence {
        private final boolean found;
        private final int start, end, sum;
        private final int[] arr;

        public Sequence(int[] arr, int start, int end, int sum) {
            this(arr, start, end, sum, false);
        }

        public Sequence(int arr[], int start, int end, int sum, boolean found) {
            this.arr = arr;
            this.start = start;
            this.end = end;
            this.sum = sum;
            this.found = found;
        }

        public Sequence(Sequence other) {
            this.arr = other.arr;
            this.start = other.start;
            this.end = other.end;
            this.sum = other.sum;
            this.found = other.found;
        }

        @Override
        public String toString() {
            // filtering on found in the steram does not work - seems that skip is called first regardless of
            // its position in the pipe
            int[] seq = found ?
                    Arrays.stream(arr)
                            .skip(start)
                            .limit(end - start + 1)
                            .toArray() :
                    new int[]{};

            return "Sequence{" +
                    "found=" + found +
                    ", start=" + start +
                    ", end=" + end +
                    ", sum=" + sum +
                    ", seq=" + Arrays.toString(seq) +
                    '}';
        }
    }
}
