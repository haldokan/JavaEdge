package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * My solution to a Facebook interview question - solution is inspired by the {1,...,n} permutations which I implemented
 * in java (ConstructingPermutation) based on a C implementation in Algorithm Design Manual by Steven Skiena.
 * <p>
 * Write Program for String Permutations using most efficient algorithm. Can you solve problem in O(n) time?
 */
public class StringPermutations {

    public static void main(String[] args) {
        StringPermutations permutations = new StringPermutations();
        int dataLen = 3;
        //data has an empty slot at the end to simplify the algorithm
        String[] dataAtStart = new String[dataLen + 1];
        dataAtStart[0] = "A";
        dataAtStart[1] = "B";
        dataAtStart[2] = "C";

        String[] permutatingData = new String[dataAtStart.length];

        permutations.backtrack(dataAtStart, permutatingData, 0);
    }

    public void backtrack(String[] dataAtStart, String[] permutatingData, int k) {
        String[] candidates = new String[permutatingData.length];
        int numCandidates = 0;
        int dataLen = permutatingData.length - 1;

        if (k == dataLen) {
            printSolution(permutatingData, k);
        } else {
            k = k + 1;
            numCandidates = candidates(dataAtStart, permutatingData, k, candidates);
            System.out.println(Arrays.toString(permutatingData));
            for (int i = 0; i < numCandidates; i++) {
                permutatingData[k] = candidates[i];
//                System.out.println(k + "/" + Arrays.toString(candidates));
                // makeMove(a, k, input)
                backtrack(dataAtStart, permutatingData, k);
                // unmakeMove(a, k, input)
                // if (finished) return;
            }
        }
    }


    private int candidates(String[] dataAtStart, String[] permutatingData, int k, String[] candidates) {
        Set<String> inPermutation = new HashSet<>();

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

    private void printSolution(String[] permutatingData, int k) {
        System.out.print("{");
        for (int i = 1; i <= k; i++) {
            System.out.print(permutatingData[i] + ",");
        }
        System.out.println("}");
    }

}
