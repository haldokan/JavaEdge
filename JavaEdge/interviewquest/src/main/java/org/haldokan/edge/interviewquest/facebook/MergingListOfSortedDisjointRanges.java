package org.haldokan.edge.interviewquest.facebook;

import java.util.*;

/**
 * My solution to a Facebook interview question
 * <p>
 * Given two arrays/Lists (choose whatever you want to) with sorted and non intersecting intervals.
 * Merge them to get a new sorted non intersecting array/list.
 * <p>
 * Eg:
 * Given:
 * Arr1 = [3-11, 17-25, 58-73];
 * Arr2 = [6-18, 40-47];
 * <p>
 * Wanted:
 * Arr3 = [3-25, 40-47, 58-73];
 * Created by haytham.aldokanji on 4/24/16.
 */
public class MergingListOfSortedDisjointRanges {

    public static void main(String[] args) {
        MergingListOfSortedDisjointRanges driver = new MergingListOfSortedDisjointRanges();

        List<Range> ranges1 = Arrays.asList(new Range(3, 11), new Range(17, 25), new Range(58, 73));
        List<Range> ranges2 = Arrays.asList(new Range(6, 18), new Range(40, 47));
        System.out.println("Merged->" + driver.merge(ranges1, ranges2));

        ranges1 = Arrays.asList(new Range(3, 11), new Range(17, 40), new Range(58, 73));
        ranges2 = Arrays.asList(new Range(6, 18), new Range(40, 60));
        System.out.println("Merged->" + driver.merge(ranges1, ranges2));

        ranges1 = Arrays.asList(new Range(3, 11), new Range(17, 40), new Range(58, 73));
        ranges2 = Arrays.asList(new Range(6, 18), new Range(40, 80));
        System.out.println("Merged->" + driver.merge(ranges1, ranges2));

        ranges1 = Arrays.asList(new Range(3, 11), new Range(17, 40), new Range(58, 73));
        ranges2 = Arrays.asList(new Range(12, 18), new Range(40, 50));
        System.out.println("Merged->" + driver.merge(ranges1, ranges2));
    }

    public Set<Range> merge(List<Range> ranges1, List<Range> ranges2) {
        Set<Range> ranges = new TreeSet<>((r1, r2) -> r1.start - r2.start);
        ranges.addAll(ranges1);

        for (Range range : ranges2) {
            Range newRange = range;
            Set<Range> rangesToDelete = new HashSet<>();

            for (Range existingRange : ranges) {
                if (newRange.intersect(existingRange)) {
                    Range mergedRange = newRange.merge(existingRange);
                    rangesToDelete.add(existingRange);
                    newRange = mergedRange;
                }
            }
            ranges.removeAll(rangesToDelete);
            ranges.add(newRange);
        }
        return ranges;
    }

    private static class Range {
        private final int start, end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean intersect(Range other) {
            Range[] ranges = align(other);
            boolean disjoint = ranges[0].start < ranges[1].start && ranges[0].end < ranges[1].start;
            return !disjoint;
        }

        public Range merge(Range other) {
            Range[] ranges = align(other);
            Range r1 = ranges[0];
            Range r2 = ranges[1];

            int mergedRangeStart = r1.start;
            int mergedRangeEnd = r1.end > r2.end ? r1.end : r2.end;

            return new Range(mergedRangeStart, mergedRangeEnd);
        }

        private Range[] align(Range other) {
            Range[] ranges = new Range[]{this, other};
            Arrays.sort(ranges, (r1, r2) -> r1.start - r2.start);
            return ranges;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            if (start != range.start) return false;
            return end == range.end;

        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "[" + start + "-" + end + "]";
        }
    }
}
