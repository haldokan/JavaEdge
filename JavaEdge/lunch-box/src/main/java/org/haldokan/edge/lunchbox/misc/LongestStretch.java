package org.haldokan.edge.lunchbox.misc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Find the indexes of the shortest and longest stretches of all distinct values in an array
 *
 * @author haldokan
 */
public class LongestStretch {
    public static void main(String[] args) {
        // Integer[] a = new Integer[] {8, 8, 8, 8, 5, 5, 5, 0, 8, 8, 8, 8, 8,
        // 1, 1, 2, 2, 7, 7, 3, 3, 2, 5, 5, 6, 7, 7, 7, 7, 3, 3, 3, 0, 0, 0, 0,
        // 1, 1, 1, 5, 5, 5, 8, 8, 8, 8, 8, 8};
        Integer[] a = new Integer[]{0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0,
                1};

        System.out.println(a.length + "#" + Arrays.toString(a));
        if (a.length == 0)
            throw new IllegalArgumentException("array is null or empty");

        Integer cn = 0;
        Map<Integer, Stretch> maxStretch = new HashMap<>();

        Integer first = a[0];
        maxStretch.put(first, new Stretch(0, 1, 1));
        cn = 1;
        Integer prev = first;
        Stretch pendingStretch = new Stretch(0, 1, 1);

        for (int i = 1; i < a.length; i++) {
            // not that the else is used to set the larger stretch which means
            // if it happens it the last elements in the array they are not
            // going to be set (else will not eval to true).
            if (a[i].equals(prev)) {
                cn++;
                pendingStretch.start = i - cn;
                pendingStretch.end = i;
                pendingStretch.len = cn;
            } else {
                Stretch prevStretch = new Stretch(i - cn, i, cn);
                maxStretch.compute(prev, (k, v) -> (v == null) ? prevStretch : (prevStretch.len > v.len) ? prevStretch
                        : v);
                cn = 1;
                Stretch currStretch = new Stretch(i, i + 1, cn);
                maxStretch.compute(a[i], (k, v) -> (v == null) ? currStretch : (currStretch.len > v.len) ? currStretch
                        : v);
                prev = a[i];
                pendingStretch.reset();
            }
        }
        // in case the last stretch is the longest for 'prev' then set it its
        // len
        if (pendingStretch.valid())
            maxStretch.compute(prev, (k, v) -> (v == null) ? pendingStretch
                    : (pendingStretch.len > v.len) ? pendingStretch : v);
        System.out.println(maxStretch);
    }

    private static class Stretch {
        public Integer len;
        public Integer start;
        public Integer end;

        public Stretch(Integer start, Integer end, Integer len) {
            this.start = start;
            this.end = end;
            this.len = len;
        }

        public void reset() {
            start = -1;
            end = -1;
            len = 0;
        }

        public boolean valid() {
            return start > -1 && end > start && len > 0;
        }

        @Override
        public String toString() {
            return start + "-" + end + "(" + len + ")";
        }
    }
}