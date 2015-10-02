package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * My solution to a Google interview question. It is actually a slight variation on a Linked in question that I resolved
 * under linkedin/RangeMerge.
 *
 * Given a set of busy time intervals of two persons as in a calendar, find the free time intervals of both persons so
 * as to arrange a new meeting input: increasing sequence of pair of numbers
 * person 1: (1,5) (10, 14) (19,20) (27,30)
 * person 2: (3,5) (12,15) (18, 21) (23, 24)
 * ouput: (5,10) (15,18) (21,23) (24,27)
 * Created by haytham.aldokanji on 9/30/15.
 */
public class CalendarFreeIntervals {
    public static void main(String[] args) {
        CalendarFreeIntervals driver = new CalendarFreeIntervals();
        List<int[]> c1 = new ArrayList<>();
        c1.add(new int[]{1, 5});
        c1.add(new int[]{10, 14});
        c1.add(new int[]{19, 20});
        c1.add(new int[]{27, 30});

        List<int[]> c2 = new ArrayList<>();
        c2.add(new int[]{3, 5});
        c2.add(new int[]{12, 15});
        c2.add(new int[]{18, 21});
        c2.add(new int[]{23, 24});

        List<int[]> freeRanges = driver.freeRanges(c1, c2);
        freeRanges.stream().forEach(r -> System.out.println(Arrays.toString(r)));
    }

    public List<int[]> freeRanges(List<int[]> ranges1, List<int[]> ranges2) {
        List<int[]> mergedRanges = mergeRanges(ranges1, ranges2);
        List<int[]> freeRanges = new ArrayList<>();
        for (int i = 0; i < mergedRanges.size(); i++) {
            if (i + 1 < mergedRanges.size()) {
                freeRanges.add(new int[]{mergedRanges.get(i)[1], mergedRanges.get(i + 1)[0]});
            }
        }
        return freeRanges;
    }

    public List<int[]> mergeRanges(List<int[]> ranges1, List<int[]> ranges2) {
        List<int[]> mergedRanges = new ArrayList<>(ranges1);
        for (int[] range2 : ranges2) {
            Iterator<int[]> it = mergedRanges.iterator();
            while (it.hasNext()) {
                int[] range1 = it.next();
                if (merge(range1, range2)) {
                    it.remove();
                }
            }
            // we are traversing the list for the 2nd time. There must be a way to find the insertIndex on the 1st pass
            int insertIndex = 0;
            for (int[] range : mergedRanges) {
                if (range2[0] > range[0])
                    insertIndex++;
            }
            mergedRanges.add(insertIndex, range2);
        }
        return mergedRanges;
    }

    private boolean merge(int[] r1, int[] r2) {
        if (r1[0] >= r2[0] && r1[0] <= r2[1] || r1[1] >= r2[0] && r1[1] <= r2[1]) {
            int[] merged = new int[2];
            merged[0] = r1[0] < r2[0] ? r1[0] : r2[0];
            merged[1] = r1[1] > r2[1] ? r1[1] : r2[1];
            r2[0] = merged[0];
            r2[1] = merged[1];
            return true;
        }
        return false;
    }
}
