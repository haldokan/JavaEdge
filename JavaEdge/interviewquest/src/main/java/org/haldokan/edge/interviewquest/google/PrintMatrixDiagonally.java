package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * My solution to a Google interview question - trick is to spot that that problem can be thought of as a tree that is
 * printed using BFS.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Give a M x N matrix, print it out diagonally.
 * <p>
 * Example:
 * 1 2 3 4 5
 * 6 7 8 9 1
 * 5 4 3 2 9
 * 9 8 7 6 5
 * <p>
 * print:
 * 1
 * 2 6
 * 3 7 5
 * 4 8 4 9
 * 5 9 3 8
 * 1 2 7
 * 9 6
 * 5
 * <p>
 * Created by haytham.aldokanji  on 6/25/16.
 */
public class PrintMatrixDiagonally {

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 1},
                {5, 4, 3, 2, 9},
                {9, 8, 7, 6, 5}
        };

        PrintMatrixDiagonally driver = new PrintMatrixDiagonally();
        driver.print(matrix);
    }

    public void print(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        Deque<int[]> evalDeck = new ArrayDeque<>();
        evalDeck.add(new int[]{0, 0});

        while (!evalDeck.isEmpty()) {
            int[] coords = evalDeck.remove();
            int row = coords[0];
            int col = coords[1];

            System.out.printf("%d, ", matrix[row][col]);
            if (row == numRows - 1 || col == 0) {
                System.out.printf("%n");
            }

            if (col + 1 < numCols) {
                evalDeck.add(new int[]{row, col + 1});
            }
            if (col == 0 && row + 1 < numRows) {
                evalDeck.add(new int[]{row + 1, col});
            }
        }
    }
}
