package org.haldokan.edge.dynamicprog;

/**
 * All subsets of {1,....,n}
 * This algo is actually belongs in "Backtracking" and is not dynamic programming. Should be removed to an appropriate
 * folder
 * The Question: 3_STAR
 * Solution is a port from the C implementation presented by Steven S. Skiena in his Book Algorithm Design Manual
 *
 * @author haldokan
 */
public class AllSubSetsInSet {

    public static void main(String[] args) {
        AllSubSetsInSet allSubSets = new AllSubSetsInSet();
        int dataLen = 3;
        // extra slot to simplify the solution
        int data[] = new int[dataLen + 1];
        allSubSets.backtrack(data, 0, dataLen);
    }

    public void backtrack(int[] combinations, int currentSolutionLen, int dataLen) {
        int[] c = new int[2];
        int numCandidates = 0;

        if (currentSolutionLen == dataLen) {
            printSolution(combinations, currentSolutionLen);
        } else {
            currentSolutionLen = currentSolutionLen + 1;
            numCandidates = candidates(combinations, currentSolutionLen, dataLen, c);
            for (int i = 0; i < numCandidates; i++) {
                combinations[currentSolutionLen] = c[i];
                // makeMove(a, k, input)
                backtrack(combinations, currentSolutionLen, dataLen);
                // unmakeMove(a, k, input)
            }
        }
    }

    private int candidates(int[] combinations, int currentSolutionLen, int dataLen, int[] candidates) {
        candidates[0] = 1;
        candidates[1] = 0;
        return 2;
    }

    private void printSolution(int[] combinations, int solutionLen) {
        System.out.print("{");
        for (int i = 1; i <= solutionLen; i++) {
            if (combinations[i] == 1) {
                System.out.print(i + ",");
            }
        }
        System.out.println("}");
    }
}