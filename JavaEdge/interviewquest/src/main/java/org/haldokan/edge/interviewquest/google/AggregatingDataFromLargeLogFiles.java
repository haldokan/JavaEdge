package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * My solution to a Google interview question. Complexity of the presented solution is O(nlogn) for sorting plus n for
 * scanning which yields asymptotic complexity of O(nlogn). Space complexity is O(n).
 * <p>
 * You are given large numbers of logs, each one of which contains a start time (long), end time (long) and memory usage (int).
 * The time is recorded as MMDDHH (100317 means October 3rd 5PM) Write an algorithm that returns a specific time (hour)
 * when the memory usage is the highest. If there are multiple answers, return the first hour.
 * <p>
 * e.g.
 * 100207 100208 2
 * 100305 100307 5
 * 100207 100209 4
 * 100209 100211 5
 * 100208 100210 3
 * 111515 121212 1
 * Answer: 100209 8
 * <p>
 * (Need to consider different scenarios like the time slots could be very sparse)
 * Created by haytham.aldokanji on 10/1/15.
 */
public class AggregatingDataFromLargeLogFiles {
    List<Measurement> measurements = new ArrayList<>();

    public static void main(String[] args) {
        AggregatingDataFromLargeLogFiles driver = new AggregatingDataFromLargeLogFiles();
        driver.addMeasurement(Measurement.start(100209, 5));
        driver.addMeasurement(Measurement.end(100211, 5));

        driver.addMeasurement(Measurement.start(100305, 5));
        driver.addMeasurement(Measurement.end(100307, 5));

        driver.addMeasurement(Measurement.start(100207, 2));
        driver.addMeasurement(Measurement.end(100208, 2));

        driver.addMeasurement(Measurement.start(100207, 4));
        driver.addMeasurement(Measurement.end(100209, 4));

        driver.addMeasurement(Measurement.start(100208, 3));
        driver.addMeasurement(Measurement.end(100210, 3));

        driver.addMeasurement(Measurement.start(111515, 1));
        driver.addMeasurement(Measurement.end(121212, 1));

        System.out.println(driver.topMeasurement());
    }

    public void addMeasurement(Measurement m) {
        measurements.add(m);
    }

    public Measurement topMeasurement() {
        Collections.sort(measurements);
        System.out.println(measurements);
        int top = 0;
        long time = 0L;
        int current = 0;
        for (Measurement m : measurements) {
            if (m.isStart()) {
                current += m.val;
                if (current > top) {
                    top = current;
                    time = m.time;
                }
            } else {
                current -= m.val;
            }
        }
        return Measurement.start(time, top);
    }

    private static class Measurement implements Comparable<Measurement> {
        private long time;
        private int val;
        private int start;

        private Measurement(long time, int val, int start) {
            this.time = time;
            this.val = val;
            this.start = start;
        }

        public static Measurement start(long time, int val) {
            return new Measurement(time, val, 1);
        }

        public static Measurement end(long time, int val) {
            return new Measurement(time, val, 0);
        }

        public boolean isStart() {
            return start == 1;
        }

        public long getTime() {
            return time;
        }

        public int getVal() {
            return val;
        }

        public int getStart() {
            return start;
        }

        @Override
        public String toString() {
            return "Measurement{" +
                    "time=" + time +
                    ", val=" + val +
                    ", start=" + start +
                    '}';
        }

        @Override
        public int compareTo(Measurement o) {
            return Comparator.comparing(Measurement::getTime).thenComparing(Measurement::getStart).compare(this, o);
            // if an interval starts at the same time that another interval ends we want to process the ending interval first.
            // This has implication on the calculated top values
//            return time - o.time > 0 ? 1 : time - o.time < 0 ? -1 : start - o.start;
        }
    }
}
