package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * My solution to a Facebook interview question. I used permutation with a set for excluding triplets that permutate to the
 * same triplet. There may be a better solution than using permutation but I cannot find it.
 * <p>
 * Given an array write a function to print all triplets in the array which sum to 0.
 * e.g:
 * Input:
 * Array = [-1, -3, 5, 4]
 * output:
 * -1, -3, 4
 * Created by haytham.aldokanji on 4/23/16.
 */
public class ArrayTripletsAddingToZero {
    private static final int TRIPLET = 3;
    private Set<Triplet> foundTriplets = new HashSet<>();

    public static void main(String[] args) {
        ArrayTripletsAddingToZero permutations = new ArrayTripletsAddingToZero();
        int dataLen = 5;
        //data has an empty slot at the end to simplify the algorithm
        int[] dataAtStart = new int[dataLen + 1];
        dataAtStart[0] = -1;
        dataAtStart[1] = -3;
        dataAtStart[2] = 5;
        dataAtStart[3] = 4;
        dataAtStart[4] = -9;

        int[] permutatingData = new int[dataAtStart.length];

        permutations.backtrack(dataAtStart, permutatingData, 0);
    }

    public void backtrack(int[] dataAtStart, int[] permutatingData, int k) {
        int[] candidates = new int[permutatingData.length];
        int numCandidates = 0;

        if (isSolution(permutatingData, k)) {
            printSolution(permutatingData);
        } else {
            k = k + 1;
            numCandidates = candidates(dataAtStart, permutatingData, k, candidates);
            for (int i = 0; i < numCandidates; i++) {
                permutatingData[k] = candidates[i];
                backtrack(dataAtStart, permutatingData, k);
            }
        }
    }

    private int candidates(int[] dataAtStart, int[] permutatingData, int k, int[] candidates) {
        Set<Integer> inPermutation = new HashSet<>();

        for (int i = 0; i < k; i++) {
            inPermutation.add(permutatingData[i]);
        }

        int numCandidates = 0;
        for (int i = 0; i < dataAtStart.length; i++) {
            if (!inPermutation.contains(dataAtStart[i])) {
                candidates[numCandidates++] = dataAtStart[i];
            }
        }
        return numCandidates;
    }

    private boolean isSolution(int[] permutatingData, int k) {
        if (k == TRIPLET) {
            int[] tripletArr = Arrays.stream(permutatingData)
                    .skip(1)
                    .limit(TRIPLET)
                    .toArray();

            Triplet triplet = new Triplet(tripletArr);
            boolean isSolution = Arrays.stream(tripletArr).sum() == 0 &&
                    !foundTriplets.contains(triplet);
            foundTriplets.add(triplet);

            return isSolution;
        }
        return false;
    }

    private void printSolution(int[] permutatingData) {
        System.out.print("{");
        for (int i = 1; i <= TRIPLET; i++) {
            System.out.print(permutatingData[i] + ",");
        }
        System.out.println("}");
    }

    private static class Triplet {
        Set<Integer> nums = new TreeSet<>();

        public Triplet(int[] nums) {
            Arrays.stream(nums).forEach(this.nums::add);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Triplet triplet = (Triplet) o;

            return !(nums != null ? !nums.equals(triplet.nums) : triplet.nums != null);

        }

        @Override
        public int hashCode() {
            return nums != null ? nums.hashCode() : 0;
        }
    }
}
