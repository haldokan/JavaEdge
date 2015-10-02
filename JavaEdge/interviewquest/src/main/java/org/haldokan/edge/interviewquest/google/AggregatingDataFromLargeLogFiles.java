package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * I cannot think of any valid solution to this friggen problem. Put off for a better day
 * <p>
 * You are given large numbers of logs, each one of which contains a start time (long), end time (long) and memory usage (int).
 * The time is recorded as MMDDHH (100317 means October 3rd 5PM) Write an algorithm that returns a specific time (hour)
 * when the memory usage is the highest. If there are multiple answers, return the first hour.
 * <p>
 * e.g.
 * 100207 100208 2
 * 100305 100307 5
 * 100207 100209 4
 * 111515 121212 1
 * Answer: 100207
 * <p>
 * (Need to consider different scenarios like the time slots could be very sparse)
 * Created by haytham.aldokanji on 10/1/15.
 */
public class AggregatingDataFromLargeLogFiles {

    public void addMeasure(long t1, long t2, int val) {
        Measure m = new Measure(t1, t2, val);
//        List<Measure> ms = m.partition()
    }

    private static class Measure {
        private long t1, t2;
        private int val;

        public Measure(long t1, long t2, int val) {
            this.t1 = t1;
            this.t2 = t2;
            this.val = val;
        }

        private boolean intersect(Measure m) {
            return (t1 >= m.t1 && t1 <= m.t2 || t2 >= m.t1 && t2 <= m.t2);
        }

        private boolean partOf(long t1, long t2) {
            return t1 >= this.t1 && t2 <= this.t2;
        }

        public List<Measure> partition(Measure m) {
            List<Measure> partitions = new ArrayList<>();
            if (intersect(m)) {
                long[] bounds = new long[4];
                bounds[0] = t1;
                bounds[1] = t2;
                bounds[2] = m.t1;
                bounds[3] = m.t2;
                Arrays.sort(bounds);
                // both ranges are the same
                if (bounds[0] == bounds[1] && bounds[2] == bounds[3]) {
                    partitions.add(new Measure(t1, t2, val + m.val));
                } else if (bounds[0] == bounds[1] && bounds[2] < bounds[3]) {
                    partitions.add(new Measure(bounds[0], bounds[1], val + m.val));
                    int pval = val;
                    if (m.partOf(bounds[2], bounds[3]))
                        pval = m.val;
                    partitions.add(new Measure(bounds[2], bounds[3], pval));
                } else if (bounds[2] == bounds[3] && bounds[1] > bounds[0]) {
                    partitions.add(new Measure(bounds[2], bounds[3], val + m.val));
                    int pval = val;
                    if (m.partOf(bounds[0], bounds[1]))
                        pval = m.val;
                    partitions.add(new Measure(bounds[0], bounds[1], pval));
                } else if (bounds[0] < bounds[1] && bounds[1] < bounds[2] && bounds[2] < bounds[3]) {
                    int pval1 = val;
                    int pval2 = m.val;
                    if (m.partOf(bounds[0], bounds[1])) {
                        pval1 = m.val;
                        pval2 = val;
                    }
                    partitions.add(new Measure(bounds[0], bounds[1], pval1));
                    partitions.add(new Measure(bounds[1], bounds[2], pval1 + pval2));
                    partitions.add(new Measure(bounds[2], bounds[3], pval2));
                }
            }
            return partitions;
        }

        @Override
        public String toString() {
            return "Measure{" +
                    "t1=" + t1 +
                    ", t2=" + t2 +
                    ", val=" + val +
                    '}';
        }
    }
}
