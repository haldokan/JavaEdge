package org.haldokan.edge.interviewquest.amazon;

import java.util.HashMap;
import java.util.Map;

/**
 * My solution to an Amazon interview question in O(n)
 * The Question: 3_STAR
 * Given an integer array, find all pair combinations which sum to a given number.
 * If a number is used once, it must not be used again.
 * eg if input array is 6444 and sum =10
 * output must be just 6 4
 * Give an O(n) solution
 */

public class SumCombinationsInArray {

    public static void main(String[] args) {
        pairCombs(new int[]{6, 4, 4, 3, 2, 7, 6, 8, 7, 3}, 10);
        pairCombs(new int[]{-6, -4, -4, -3, -2, -7, -6, -8, -7, -3}, -10);
    }

    private static void pairCombs(int[] nums, int k) {
        Map<Integer, Integer> complements = new HashMap<>();

        for (int num : nums) {
            if (complements.containsKey(num)) {
                System.out.println(k - num + ", " + num);

                int howmany = complements.get(num);
                if (howmany == 1) {
                    complements.remove(num);
                } else {
                    complements.put(num, howmany - 1);
                }
            } else {
                complements.put(k - num, 1);
            }
        }

    }
}
