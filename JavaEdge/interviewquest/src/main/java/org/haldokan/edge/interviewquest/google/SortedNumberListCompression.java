package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
 * Number list compression.
 * Given an sorted array (example):
 * 1, 2, 3,10, 25, 26, 30, 31, 32, 33
 * <p>
 * Output: find consecutive segments
 * print: 1-3, 10, 25-26, 30-33
 */
public class SortedNumberListCompression<E> {

    public static void main(String[] args) {
//        int[] arr = new int[] {1, 2, 3,10, 25, 26, 30, 31, 32, 33};
//        int[] arr = new int[] {1, 2, 3, 3, 10, 10, 25, 25, 26, 26, 30, 31, 32, 33};
        int[] arr = new int[]{1, 1, 2, 3, 3, 10, 10, 25, 25, 26, 26, 30, 31, 32, 33, 40, 40};
        SortedNumberListCompression driver = new SortedNumberListCompression();
        System.out.println(driver.compress(arr));
    }

    public List<String> compress(int[] sortedArr) {
        if (sortedArr == null || sortedArr.length == 0)
            throw new IllegalArgumentException("Invalid input");

        List<String> compressed = new ArrayList<>();

        if (sortedArr.length == 1) {
            addSegment(sortedArr[0], sortedArr[0], compressed);
        }

        int numAtSegStart = sortedArr[0];
        int numAtSegEnd = numAtSegStart;

        for (int i = 1; i < sortedArr.length; i++) {
            int diff = sortedArr[i] - sortedArr[i - 1];
            if (diff == 1 || diff == 0) {
                numAtSegEnd = sortedArr[i];
            } else {
                addSegment(numAtSegStart, numAtSegEnd, compressed);
                numAtSegStart = sortedArr[i];
                numAtSegEnd = numAtSegStart;
            }
        }
        addSegment(numAtSegStart, numAtSegEnd, compressed);
        return compressed;
    }

    private void addSegment(int n1, int n2, List<String> compressed) {
        String seg = String.valueOf(n1);
        if (n1 != n2)
            seg += "-" + n2;
        compressed.add(seg);
    }
}
