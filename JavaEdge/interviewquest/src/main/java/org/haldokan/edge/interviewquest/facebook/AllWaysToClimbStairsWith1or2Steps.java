package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * My solution to a Facebook interview question - piggy-back on backtracking for permutation while using
 * the pruning technique to cut off the permutations not summing to number of steps. Permutations algorithms complexity is
 * n! (oh boy!). I will try to provide a solution based on dynamic programming which normally reduces complexity to O(n3).
 *
 * The Question: 3_STAR
 *
 * Implement stairs(N) that (collect the solutions in a list) prints all the ways to climb up a N-step-stairs
 * where one can either take a single step or double step.
 * We'll use 1 to represent a single step, and 2 to represent a double step.
 * <p>
 * stairs(3)
 * 111
 * 12
 * 21
 * Created by haytham.aldokanji on 5/14/16.
 */
public class AllWaysToClimbStairsWith1or2Steps {
    private final Set<String> combinations = new HashSet<>();

    public static void main(String[] args) {

        AllWaysToClimbStairsWith1or2Steps permutations = new AllWaysToClimbStairsWith1or2Steps();
        int numOfSteps = 7;
        int dataLen = numOfSteps * 2;
        //data has an empty slot at the end to simplify the algorithm
        String[] dataAtStart = new String[dataLen + 1];
        dataAtStart[0] = "1";
        dataAtStart[1] = "1";
        dataAtStart[2] = "1";
        dataAtStart[3] = "2";
        dataAtStart[4] = "2";
        dataAtStart[5] = "2";
        dataAtStart[6] = "1";
        dataAtStart[7] = "2";
        dataAtStart[8] = "1";
        dataAtStart[9] = "2";
        dataAtStart[10] = "1";
        dataAtStart[11] = "2";
        dataAtStart[12] = "1";
        dataAtStart[13] = "2";

        permutations.backtrack(dataAtStart);
        permutations.combinations.stream().forEach(System.out::println);
    }

    public void backtrack(String[] dataAtStart) {
        String[] permutatingData = new String[dataAtStart.length];
        // make data unique so we are able to piggy back on the machinery of the backtracking algorithm for permutations
        for (int i = 0; i < dataAtStart.length - 1; i++) {
            dataAtStart[i] += "-" + i;
        }
        backtrack(dataAtStart, permutatingData, 0);
    }

    private void backtrack(String[] dataAtStart, String[] permutatingData, int solutionCurrentLen) {
        String[] candidates = new String[permutatingData.length];
        int numCandidates;
        int maxDataLen = (permutatingData.length - 1) / 2;
        int minDataLen = maxDataLen / 2;

        boolean added = false;
        if (solutionCurrentLen >= minDataLen && solutionCurrentLen <= maxDataLen) {
            added = addIfSolution(permutatingData, solutionCurrentLen);
        }
        if (!added) {
            solutionCurrentLen = solutionCurrentLen + 1;
            numCandidates = candidates(dataAtStart, permutatingData, solutionCurrentLen, candidates);
            for (int i = 0; i < numCandidates; i++) {
                permutatingData[solutionCurrentLen] = candidates[i];
                backtrack(dataAtStart, permutatingData, solutionCurrentLen);
            }
        }
    }


    private int candidates(String[] dataAtStart, String[] permutatingData, int solutionCurrentLen, String[] candidates) {
        Set<String> inPermutation = new HashSet<>();

        inPermutation.addAll(Arrays.asList(permutatingData).subList(0, solutionCurrentLen));

        int numCandidates = 0;
        for (String item : dataAtStart) {
            if (!inPermutation.contains(item)) {
                candidates[numCandidates++] = item;
            }
        }
        return numCandidates;
    }

    private boolean addIfSolution(String[] permutatingData, int solutionLen) {
        int numSteps = (permutatingData.length - 1) / 2;
        String[] solution = new String[solutionLen];
        for (int i = 1; i <= solutionLen; i++) {
            String item = permutatingData[i];
            solution[i - 1] = item.substring(0, item.indexOf("-"));
        }
        int sumOfStepsTaken = Arrays.stream(solution).collect(Collectors.summingInt(Integer::valueOf));
        if (sumOfStepsTaken == numSteps) {
            String solutionAsString = Arrays.stream(solution).collect(Collectors.joining(","));
            combinations.add(solutionAsString);
            return true;
        }
        return sumOfStepsTaken > numSteps;
    }
}
