package org.haldokan.edge.dynamicprog;

import java.util.Arrays;

/**
 * This algo is actually belongs in "Backtracking" and is not dynamic programming. Should be removed to an appropriate
 * folder
 * <p>
 * Solution is a port from the C implementation presented by Steven S. Skiena in his Book Algorithm Design Manual
 *
 * @author haldokan
 */
public class AllSubSetsInSet {

    public static void main(String[] args) {
        AllSubSetsInSet allSubSets = new AllSubSetsInSet();
        int input = 3;
        int a[] = new int[input + 1];
        allSubSets.backtrack(a, 0, input);
    }

    public void backtrack(int[] a, int k, int input) {
        int[] c = new int[2];
        int nc = 0;

        if (k == input) {
            printSolution(a, k);
        } else {
            k = k + 1;
            nc = candidates(a, k, input, c);
            for (int i = 0; i < nc; i++) {
                a[k] = c[i];
                // makeMove(a, k, input)
                System.out.println("before k/input/a: " + k + "/" + input + "/" + Arrays.toString(a));
                backtrack(a, k, input);
                System.out.println("after k/input/a: " + k + "/" + input + "/" + Arrays.toString(a));
                // unmakeMove(a, k, input)
                // if (finished) return;
            }
        }
    }

    private int candidates(int[] a, int k, int n, int[] c) {
        c[0] = 1;
        c[1] = 0;
        return 2;
    }

    private void printSolution(int[] a, int k) {
        System.out.print("{");
        for (int i = 1; i <= k; i++)
            if (a[i] == 1)
                System.out.print(i + ",");
        System.out.println("}");
    }
}