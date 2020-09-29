package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * My solution to a Facebook interview question
 *
 * The Question: 4-STAR
 *
 * Given a list of arrays of time intervals, write a function that calculates the total amount of time covered by the intervals.
 * For example:
 * input = [(1,4), (2,3)]
 * return 3
 *
 * input = [(4,6), (1,2)]
 * return 3
 *
 * input = {{1,4}, {6,8}, {2,4}, {7,9}, {10, 15}}
 * return 11
 */
public class SumTimeIntervals {
    public static void main(String[] args) {
        SumTimeIntervals driver = new SumTimeIntervals();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
    }

    public int totalTime(Interval[] intervals) {
        if (intervals.length == 0) {
            return 0;
        }
        if (intervals.length == 1) {
            return intervals[0].end - intervals[0].start;
        }
        Interval[] arrCopy = new Interval[intervals.length];
        System.arraycopy(intervals, 0, arrCopy, 0, intervals.length);
        Arrays.sort(arrCopy);
        System.out.println(Arrays.toString(arrCopy));

        List<Interval> mergedIntervals = new ArrayList<>();
        int ndx1 = 0;
        int ndx2 = 1;

        Interval mergedInterval = arrCopy[ndx1];
        while (ndx2 < arrCopy.length) {
            if (mergedInterval.end >= arrCopy[ndx2].start) {
                mergedInterval = Interval.create(mergedInterval.start, arrCopy[ndx2].end);
                ndx2++;
            } else {
                mergedIntervals.add(mergedInterval);
                ndx1++;
                mergedInterval = Interval.create(arrCopy[ndx2].start, arrCopy[ndx2].end);
                ndx2++;
            }
        }
        // account for a last record that's disjoint from the one before
        Interval lastAdded = mergedIntervals.get(mergedIntervals.size() - 1);
        if (!lastAdded.equals(mergedInterval)) {
            mergedIntervals.add(mergedInterval);
        }
        System.out.println(mergedIntervals);

        int total = 0;
        for (Interval interval : mergedIntervals) {
            total += interval.end - interval.start;
        }
        return total;
    }

    private static class Interval implements Comparable<Interval> {
        private final int start;
        private final int end;

        private static Interval create(int start, int end) {
            return new Interval(start, end);
        }

        private Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Interval other) {
            return this.start < other.start ? -1 : 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Interval interval = (Interval) o;
            return start == interval.start &&
                    end == interval.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", start, end);
        }
    }

    private void test1() {
        System.out.println(totalTime(new Interval[]{
                Interval.create(1, 4),
                Interval.create(6, 8),
                Interval.create(2, 4),
                Interval.create(7, 9),
                Interval.create(10, 15)
        }));
    }

    private void test2() {
        System.out.println(totalTime(new Interval[]{
                Interval.create(1, 4),
                Interval.create(6, 8),
                Interval.create(2, 4),
                Interval.create(7, 9)
        }));
    }

    private void test3() {
        System.out.println(totalTime(new Interval[]{Interval.create(1, 4), Interval.create(6, 8)}));
    }

    private void test4() {
        System.out.println(totalTime(new Interval[]{Interval.create(1, 4)}));
    }
}
