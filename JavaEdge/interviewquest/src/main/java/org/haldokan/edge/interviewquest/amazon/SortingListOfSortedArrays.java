package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * My solution to an Amazon interview question
 * <p>
 * Given a list of sorted arrays, like List<int[]>. Prepare and return a single sorted list.
 */

public class SortingListOfSortedArrays {

    public static void main(String[] args) {
        Integer[] a1 = new Integer[]{1, 3, 5, 7, 9, 11};
        Integer[] a2 = new Integer[]{2, 4, 6, 8, 10, 12, 14, 16};
        Integer[] a3 = new Integer[]{20, 22};
        Integer[] a4 = new Integer[]{-22, -20};

        List<Integer[]> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);

        System.out.println(sort(list));
    }

    private static List<Integer> sort(List<Integer[]> sortedArrs) {
        List<Integer> rs = new LinkedList<>();
        for (Integer[] arr : sortedArrs) {
            rs = merge(rs, arr);
        }
        return rs;
    }

    private static List<Integer> merge(List<Integer> rs, Integer[] arr) {
        List<Integer> merged = new ArrayList<>();

        if (rs.isEmpty()) {
            merged.addAll(Arrays.asList(arr));
            return merged;
        }

        int ndx = 0;

        for (int num : rs) {
            while (ndx < arr.length && num > arr[ndx]) {
                merged.add(arr[ndx++]);
            }
            merged.add(num);
        }
        for (int i = ndx; i < arr.length; i++) {
            merged.add(arr[i]);
        }
        return merged;
    }

}
