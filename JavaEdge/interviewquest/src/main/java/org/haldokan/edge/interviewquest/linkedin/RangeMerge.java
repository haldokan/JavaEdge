package org.haldokan.edge.interviewquest.linkedin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * My implementation of a Linkedin interview question found on Careercup.
 * <p>
 * Given a collection of pair representing intervals write a function which inserts new interval into collection and
 * merges overlapping intervals. Example: [-10, -1], [0,2], [4,10] insert [-5, 1] output: [-10, 2], [4, 10]
 *
 * @author haldokan
 */
public class RangeMerge {

    public static void main(String[] args) {
        List<int[]> ranges = new ArrayList<>();
        ranges.add(new int[]{-10, -4});
        ranges.add(new int[]{0, 2});
        ranges.add(new int[]{4, 10});

        ranges.stream().forEach(e -> System.out.print(Arrays.toString(e) + ", "));
        System.out.println();

        RangeMerge rm = new RangeMerge();
        int[] newRange = new int[]{-9, 6};
        System.out.println("insert newRange " + Arrays.toString(newRange));
        rm.insertRange(ranges, newRange);
        ranges.stream().forEach(e -> System.out.print(Arrays.toString(e) + ", "));
        System.out.println();
    }

    public void insertRange(List<int[]> ranges, int[] nrange) {
        Iterator<int[]> it = ranges.iterator();
        while (it.hasNext()) {
            int[] range = it.next();
            if (merge(range, nrange)) {
                it.remove();
            }
        }
        // we are traversing the list for the 2nd time. There must be a way to find the insertIndex on the 1st pass
        int insertIndex = 0;
        for (int[] range : ranges) {
            if (nrange[0] > range[0])
                insertIndex++;
        }
        ranges.add(insertIndex, nrange);
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
