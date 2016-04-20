package org.haldokan.edge.dynamicprog;

/**
 * Constructing permutations of {1,....,n}. I ported the C solution given by Steven Skiena in his book Algorithm Design Manual
 * to Java. The solution uses back tracking.
 */
public class ConstructingPermutations {

    public static void main(String[] args) {
        ConstructingPermutations permutations = new ConstructingPermutations();
        int dataLen = 3;
        //data has an empty slot at the end to simplify the algorithm
        int data[] = new int[dataLen + 1];
        permutations.backtrack(data, 0);
    }

    public void backtrack(int[] data, int k) {
        int[] candidates = new int[data.length];
        int numCandidates = 0;
        int dataLen = data.length - 1;

        if (k == dataLen) {
            printSolution(data, k);
        } else {
            k = k + 1;
            numCandidates = candidates(data, k, candidates);
            for (int i = 0; i < numCandidates; i++) {
                data[k] = candidates[i];
                // makeMove(a, k, input)
//                System.out.println("before k/input/a: " + k + "/" + input + "/" + Arrays.toString(a));
                backtrack(data, k);
//                System.out.println("after k/input/a: " + k + "/" + input + "/" + Arrays.toString(a));
                // unmakeMove(a, k, input)
                // if (finished) return;
            }
        }
    }


    private int candidates(int[] data, int k, int[] candidates) {
        boolean[] inPermutation = new boolean[data.length];

        for (int i = 1; i < data.length; i++) {
            inPermutation[i] = false;
        }
        for (int i = 0; i < k; i++) {
            inPermutation[data[i]] = true;
        }

        int numCandidates = 0;
        for (int i = 1; i < data.length; i++) {
            if (!inPermutation[i]) {
                candidates[numCandidates++] = i;
            }
        }
        return numCandidates;
    }

    private void printSolution(int[] data, int k) {
        System.out.print("{");
        for (int i = 1; i <= k; i++) {
            System.out.print(data[i] + ",");
        }
        System.out.println("}");
    }
}
