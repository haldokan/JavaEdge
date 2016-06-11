package org.haldokan.edge.interviewquest.linkedin;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * My solution to a Linkedin interview question. Problem description is provided in comments to the 1st 2 methods
 * The Question: 4_STAR
 * @author haldokan
 */
public class CoveringIntervalsForIntersectingRanges {
    private Set<Interval> intervals = new TreeSet<>();

    public static void main(String[] args) {
        CoveringIntervalsForIntersectingRanges cover = new CoveringIntervalsForIntersectingRanges();
        cover.addInterval(3, 6);
        System.out.println(cover.intervals);
        cover.addInterval(8, 10);
        System.out.println(cover.intervals);
        cover.addInterval(1, 5);
        System.out.println(cover.intervals);
        cover.addInterval(5, 7);
        System.out.println(cover.intervals);
        cover.addInterval(3, 9);
        System.out.println(cover.intervals);
        cover.addInterval(4, 5);
        System.out.println(cover.intervals);
        cover.addInterval(0, 12);
        System.out.println(cover.intervals);
        cover.addInterval(18, 21);
        System.out.println(cover.intervals);
        cover.addInterval(20, 21);
        System.out.println(cover.intervals);
        cover.addInterval(13, 14);
        System.out.println(cover.intervals);
        cover.addInterval(15, 17);
        System.out.println(cover.intervals);
        cover.addInterval(11, 16);
        System.out.println(cover.intervals);
        cover.addInterval(17, 17);
        System.out.println(cover.intervals);
        cover.addInterval(18, 18);
        System.out.println(cover.intervals);
        cover.addInterval(3, 19);
        System.out.println(cover.intervals);

        System.out.println(cover.getTotalCoveredLength());

    }

    /**
     * Adds an interval [from, to] into internal structure.
     */
    void addInterval(int from, int to) {
        Interval intr = new Interval(from, to);
        MergeStatus status = null;
        if (intervals.isEmpty())
            status = MergeStatus.INSERT;

        for (Iterator<Interval> itr = intervals.iterator(); itr.hasNext(); ) {
            if (status == MergeStatus.NOOP || status == MergeStatus.INSERT)
                break;
            Interval existing = itr.next();
            status = mergeIntervals(existing, intr);
            if (status == MergeStatus.REPLACE)
                itr.remove();
        }
        if (status == MergeStatus.REPLACE || status == MergeStatus.INSERT || status == MergeStatus.SKIP)
            intervals.add(intr);
    }

    /**
     * Returns a total length covered by intervals. If several intervals intersect, intersection should be counted only
     * once.
     * <p>
     * Example: addInterval(3, 6) addInterval(8, 9) addInterval(1, 5)
     * <p>
     * getTotalCoveredLength() -> 6 i.e. [1,5] and [3,6] intersect and give a total covered interval [1,6]. [1,6] and
     * [8,9] don't intersect so total covered length is a sum for both intervals, that is 6.
     * <p>
     * _________ ___ ____________
     * <p>
     * 0 1 2 3 4 5 6 7 8 9 10
     */
    public int getTotalCoveredLength() {
        int totalCovered = 0;
        for (Interval intr : intervals) {
            totalCovered += intr.to - intr.from;
        }
        return totalCovered;
    }

    // merged interval is updated in i2
    private MergeStatus mergeIntervals(Interval i1, Interval i2) {
        if (i1.equals(i2) || isNested(i2, i1))
            return MergeStatus.NOOP;
        if (i1.compareTo(i2) > 0)
            return MergeStatus.INSERT;
        if (i1.compareTo(i2) < 0)
            return MergeStatus.SKIP;

        if (isNested(i1, i2))
            return MergeStatus.REPLACE;
        if (i1.from >= i2.from && i1.from <= i2.to) {
            i2.to = i1.to;
            return MergeStatus.REPLACE;
        }
        if (i1.to >= i2.from && i1.to <= i2.to) {
            i2.from = i1.from;
            return MergeStatus.REPLACE;
        } else {
            throw new IllegalStateException("Invalid merge state b/w " + i1 + " and " + i2);
        }
    }

    private boolean isNested(Interval nestee, Interval nester) {
        return nestee.from >= nester.from && nestee.to <= nester.to;
    }

    private enum MergeStatus {
        REPLACE, INSERT, SKIP, NOOP
    }

    private static class Interval implements Comparable<Interval> {
        private int from, to;

        public Interval(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[" + from + "-" + to + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + from;
            result = prime * result + to;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Interval other = (Interval) obj;
            if (from != other.from)
                return false;
            if (to != other.to)
                return false;
            return true;
        }

        @Override
        public int compareTo(Interval o) {
            if (from > o.to)
                return 1;
            if (to < o.from)
                return -1;
            return 0;
        }
    }
}
