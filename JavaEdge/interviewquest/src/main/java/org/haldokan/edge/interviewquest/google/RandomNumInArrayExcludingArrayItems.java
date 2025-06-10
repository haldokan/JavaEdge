package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - this is an unclear question. The way it is stated, to truly select a
 * random number we have to call a random number generator and check that the generated number is not in the array. I
 * choose to make it more interesting and interpret the question to mean selecting a random integer that is probabilistic
 * to the ranges of numbers b/w min and max and not present in the array. The random number we generate is based on the
 * probability of its range (based on Math.random) and within the range the equally distributed probability of selecting
 * an integer (based on Random.nextInt)
 * <p>
 * The Question: 3_STAR
 * <p>
 * Given a min and max of an integer array, write a function to randomly return a number inside of this range,
 * but not in the array.
 * <p>
 * Created by haytham.aldokanji on 7/4/16.
 */
public class RandomNumInArrayExcludingArrayItems {
    private final List<Range> ranges;

    public RandomNumInArrayExcludingArrayItems(int[] arr) {
        this.ranges = computeProbabilityRanges(arr);
    }

    public static void main(String[] args) {
        testComputeRanges();
        testRandom();
    }

    private static void testComputeRanges() {
        int[] arr = new int[]{6, 2, 9, 5, 1, 3};
        RandomNumInArrayExcludingArrayItems driver = new RandomNumInArrayExcludingArrayItems(arr);

        List<Range> ranges = driver.computeRanges(arr);

        assertThat(ranges.size(), is(2));
        assertThat(ranges, contains(new Range(4, 5), new Range(7, 9)));
    }

    private static void testRandom() {
        int[] arr = new int[]{6, 2, 9, 5, 1, 3, 10, 12, 11, 13};
        RandomNumInArrayExcludingArrayItems driver = new RandomNumInArrayExcludingArrayItems(arr);

        IntStream.range(0, 100).forEach(i -> {
            int rand = driver.random();
            System.out.printf("%d, ", rand);
            assertThat(rand, isOneOf(4, 7, 8));
        });
    }

    public int random() {
        double rand = Math.random();
        for (Range range : ranges) {
            if (range.inRange(rand)) {
                return range.random();
            }
        }
        throw new IllegalStateException("probability " + rand + " could not be found in ranges");
    }

    private List<Range> computeProbabilityRanges(int[] arr) {
        List<Range> ranges = computeRanges(arr);
        int rangesTotalLen = ranges.stream().collect(Collectors.summingInt(Range::rangeLength));

        double rangeStart = 0d;
        for (Range range : ranges) {
            double rangeEnd = rangeStart + (double) range.rangeLength() / (double) rangesTotalLen;
            range.setProbabilityRange(rangeStart, rangeEnd);
            rangeStart = rangeEnd;
        }
        return ranges;
    }

    private List<Range> computeRanges(int[] arr) {
        Arrays.sort(arr);
        List<Range> ranges = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            int j = i + 1;
            if (j < arr.length) {
                int item1 = arr[i];
                int item2 = arr[j];
                if (item2 - item1 > 1) {
                    ranges.add(new Range(item1 + 1, item2));
                }
            }
        }
        return ranges;
    }

    private static class Range {
        private static final Random randomGenerator = new Random();
        private final int start, end;
        private double probabilityStart, getProbabilityEnd;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean inRange(double random) {
            return random >= probabilityStart && random < getProbabilityEnd;
        }

        public void setProbabilityRange(double probabilityStart, double probabilityEnd) {
            this.probabilityStart = probabilityStart;
            this.getProbabilityEnd = probabilityEnd;
        }

        public int random() {
            int rand = randomGenerator.nextInt(end - start);
            return start + rand;
        }

        public int rangeLength() {
            return end - start;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;
            return start == range.start && end == range.end;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ")";
        }
    }
}
