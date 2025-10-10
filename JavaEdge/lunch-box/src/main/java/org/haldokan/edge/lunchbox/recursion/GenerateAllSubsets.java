package org.haldokan.edge.lunchbox.recursion;

/**
 * Generate All Subsets (Power Set)
 * Input: [1,2,3] → Output: All subsets
 * 👉 Backtracking begins: include/exclude decisions.
 */

import java.util.ArrayList;
import java.util.List;

public class GenerateAllSubsets {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();
        generateSubsets(0, nums, currentSubset, result);
        return result;
    }

    private void generateSubsets(int index, int[] nums, List<Integer> currentSubset, List<List<Integer>> result) {
        // Base case: We have processed all elements, so add the current subset to the result
        if (index == nums.length) {
            result.add(new ArrayList<>(currentSubset));
            return;
        }

        // 1. Recursive call with the current element INCLUDED
        currentSubset.add(nums[index]);
        generateSubsets(index + 1, nums, currentSubset, result);

        // 2. BACKTRACK: Remove the last element to explore the branch where it's NOT included
        currentSubset.removeLast();

        // 3. Recursive call with the current element EXCLUDED
        generateSubsets(index + 1, nums, currentSubset, result);
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        GenerateAllSubsets solution = new GenerateAllSubsets();
        List<List<Integer>> result = solution.subsets(nums);
        System.out.println(result); // [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]
    }
}