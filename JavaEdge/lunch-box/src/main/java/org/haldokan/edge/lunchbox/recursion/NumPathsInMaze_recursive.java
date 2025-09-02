package org.haldokan.edge.lunchbox.recursion;

import java.util.Arrays;

/**
 * 🧩 Problem Concept: Count Paths in a Maze
 * 🎯 Goal:
 * You're given a grid or matrix (typically m x n in size). You need to determine how many distinct paths exist from the
 * top-left corner of the grid to the bottom-right corner, by moving only in certain directions (usually right and down).
 *
 * 🧠 Rules & Constraints:
 * Allowed moves: Typically right and down only (not diagonal or back).
 * No obstacles: Basic version assumes an open grid.
 * Count only complete paths that reach the bottom-right corner.
 * The number of steps in each path equals (rows - 1) + (cols - 1).
 */
public class NumPathsInMaze_recursive {
    public static void main(String[] args) {
        System.out.println(count(3, 3)); // 6
    }

    public static int count(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        return doCount(rows - 1, cols - 1, matrix);
    }

    private static int doCount(int i, int j, int[][] matrix) {
        if (i == 0 || j == 0) {
            return 1;
        }
        // Memoization
        if (matrix[i][j] != 0) {
            return matrix[i][j];
        }
        return doCount(i, j - 1, matrix) + doCount(i - 1, j, matrix);
    }
}
