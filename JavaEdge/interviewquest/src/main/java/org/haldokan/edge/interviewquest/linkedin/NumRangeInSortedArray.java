package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * My solution to a Linkedin interview question
 * The Question: 3_STAR
 * Given a sorted array with duplicates and a number, find the range in the form of (startIndex, endIndex) of that
 * number. For example,
 * <p>
 * find_range({0 2 3 3 3 10 10}, 3) should return (2,4). find_range({0 2 3 3 3 10 10}, 6) should return (-1,-1). The
 * array and the number of duplicates can be large.
 *
 * @author haldokan
 */
public class NumRangeInSortedArray {

    public static void main(String[] args) {
        NumRangeInSortedArray range = new NumRangeInSortedArray();
        Integer[] arr = new Integer[]{0, 2, 3, 3, 3, 10, 10};

        System.out.println(Arrays.toString(range.findRange(arr, 3)));
        System.out.println(Arrays.toString(range.findRange(arr, 10)));
        System.out.println(Arrays.toString(range.findRange(arr, 2)));
        System.out.println(Arrays.toString(range.findRange(arr, 0)));
        System.out.println(Arrays.toString(range.findRange(arr, 6)));
    }

    // we can implement the binary search ourselves which is an easy thing but I will use Java's
    private int[] findRange(Integer[] arr, Integer num) {
        int[] range = new int[]{-1, -1};
        int index = Collections.binarySearch(Arrays.asList(arr), num);
        if (index < 0)
            return range;
        int end = index;
        for (end = index; end < arr.length && arr[end].equals(num); end++) {
            range[1] = end;
        }

        int start = index;
        for (start = index; start >= 0 && Objects.equals(arr[start], num); start--) {
            range[0] = start;
        }
        return range;
    }
}
