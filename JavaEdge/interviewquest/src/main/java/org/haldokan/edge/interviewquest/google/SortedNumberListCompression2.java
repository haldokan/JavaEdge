package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
 * Number list compression.
 * Given a sorted array (example):
 * 1, 2, 3,10, 25, 26, 30, 31, 32, 33
 * <p>
 * Output: find consecutive segments
 * print: 1-3, 10, 25-26, 30-33
 */
public class SortedNumberListCompression2 {
    public static void main(String[] args) {
        System.out.println(compress(new int[]{1, 2, 3,10, 25, 26, 30, 31, 32, 33}));
    }

    static String compress(int[] input) {
        int n1 = 0;
        int n2 = 1;
        StringBuilder sb = new StringBuilder();
        while (n2 <= input.length) {
            // I am sure there is a nicer way to do this check
            if (n2 == input.length || input[n2] - input[n2 - 1] > 1) {
                int count = n2 - n1;
                if (count > 1) {
                    sb.append(input[n1]).append("-").append(input[n2 - 1]).append(", ");
                } else {
                    sb.append(input[n1]).append(", ");
                }
                n1 += count;
            }
            n2++;
        }
        return sb.toString();
    }
}
