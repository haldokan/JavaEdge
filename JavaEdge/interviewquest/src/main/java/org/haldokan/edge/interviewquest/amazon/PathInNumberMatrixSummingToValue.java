package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * My solution to an Amazon interview question - shows understanding how the recursion stack works. Ending the recursion
 * by sticking a null to the end of the currentPath is a bit lame and there must be a better way but that's what I settled on in the interest of time
 *
 * The Question: 4-STAR
 *
 * Given a random MxN matrix and a positive integer, recursively Your program should then find a continuous path through
 * the matrix starting at position 0,0 that will sum to n. Your program should only move left (col -1), right(col +1),
 * up (row -1) and down (row+1) and can only use a position once in the sum. If there is such path in the matrix,
 * create the path in a separate matrix with the same size, and replacing the indices used with 1 and the rest 0.
 */
public class PathInNumberMatrixSummingToValue {
    private static final int[] INDEX_OFFSETS = new int[]{-1, 1};

    public static void main(String[] args) {
        PathInNumberMatrixSummingToValue driver = new PathInNumberMatrixSummingToValue();
        driver.test1();
    }

    void test1() {
        int[][] matrix = new int[][]{ new int[]{ 1, 2, 3}, new int[]{4, 5, 6}};
        System.out.println(findPath(matrix, 14)); // found
        System.out.println(findPath(matrix, 9)); // not found
    }

    List<Coord> findPath(int[][] matrix, int sum) {
        List<Coord> path = new ArrayList<>();
        Coord start = new Coord(0, 0);
        path.add(start);
        path(matrix, start, matrix[0][0], sum, path);
        if (!path.isEmpty()) {
            path.remove(path.size() - 1);
        }
        return path;
    }

    void path(int[][] matrix, Coord coord, int currentSum, int sum, List<Coord> currentPath) {
        if (currentSum == sum) {
            currentPath.add(null);
        }
        for (Coord neighbor : neighbors(matrix, coord, currentPath)) {
            int neighborValue = matrix[neighbor.x][neighbor.y];
            int nextSum = neighborValue + currentSum;
            if (nextSum <= sum && currentPath.get(currentPath.size() - 1) != null) {
                currentPath.add(neighbor);
                path(matrix, neighbor, nextSum, sum, currentPath);
            }
        }
        if (!currentPath.isEmpty()) {
            Coord last = currentPath.get(currentPath.size() - 1);
            if (last != null) {
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    private Coord[] neighbors(int[][] matrix, Coord coordinate, List<Coord> currentPath) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coord> neighbors = new ArrayList<>();
        for (int offset : INDEX_OFFSETS) {
            neighbors.add(new Coord(coordinate.x + offset, coordinate.y));
            neighbors.add(new Coord(coordinate.x, coordinate.y + offset));
        }

        return neighbors.stream().filter(neighbor -> {
            int row = neighbor.x;
            int col = neighbor.y;
            return row >= 0
                    && row < numRows
                    && col >= 0
                    && col < numCols
                    && !currentPath.contains(neighbor);
        }).toArray(Coord[]::new);
    }

    static class Coord {
        int x;
        int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x &&
                    y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }
}
