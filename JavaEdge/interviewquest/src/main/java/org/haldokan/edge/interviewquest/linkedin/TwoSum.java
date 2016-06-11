package org.haldokan.edge.interviewquest.linkedin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * My solution to a Linkedin interview question.
 * The Question: 2_STAR
 * The question is describe in comments for each method
 *
 * @author haldokan
 */
public class TwoSum {
    private List<Integer> nums = new ArrayList<>();
    private Set<Integer> sums = new HashSet<>();

    public static void main(String[] args) {
        TwoSum twoSum = new TwoSum();
        twoSum.store(1);
        twoSum.store(-2);
        twoSum.store(3);
        twoSum.store(6);
        System.out.println(twoSum.nums);
        System.out.println(twoSum.sums);

        System.out.println(twoSum.test(4));
        System.out.println(twoSum.test(-1));
        System.out.println(twoSum.test(9));
        System.out.println(twoSum.test(10));
        System.out.println(twoSum.test(5));
        System.out.println(twoSum.test(0));
    }

    /**
     * Stores input in an internal data structure.
     */
    public void store(int input) {
        for (Integer i : nums) {
            sums.add(i + input);
        }
        nums.add(input);
    }

    /**
     * Returns true if there is any pair of numbers in the internal data structure which have sum val, and false
     * otherwise. For example, if the numbers 1, -2, 3, and 6 had been stored, the method should return true for 4, -1,
     * and 9, but false for 10, 5, and 0
     */
    public boolean test(int val) {
        return sums.contains(val);
    }
}