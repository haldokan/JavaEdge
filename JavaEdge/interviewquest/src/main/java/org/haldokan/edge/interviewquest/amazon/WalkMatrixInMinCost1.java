package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to an Amazon interview question - BFS and updating the accumulate cost of each coordinate with the minimum
 * cost of the different paths that pass thru the coordinate. Using Integer accumulate cost matrix with null initial values
 * plays nicely to mark the coordinates that have not yet traversed.
 * I solve this question again but instead return the shorted path along with the min cost for every coordinate in the
 * path: Look at WalkMatrixInMinCost2
 * <p>
 * The Question: 4_STAR
 * <p>
 * Given a matrix of positive integers, you have to reach from the top left corner to the bottom right in minimum cost.
 * You can only go one square right, down or diagonally right-down. Cost is the sum of squares you've covered.
 * Return the minimum cost.
 * e.g.
 * 4 5 6
 * 1 2 3
 * 0 1 2
 * <p>
 * Answer: 8 (4+1+0+1+2)
 * <p>
 * Created by haytham.aldokanji on 7/31/16.
 */
public class WalkMatrixInMinCost1 {
    // optimization so we don't re-create coordinates that are already traversed - rather get them from the cache
    // not strictly needed but in a very large matrix it can make be useful in cutting computation time
    private final Map<String, Coordinate> coordinateCache = new HashMap<>();

    public static void main(String[] args) {
        WalkMatrixInMinCost1 driver = new WalkMatrixInMinCost1();
        driver.test1();
        driver.test2();
    }

    public int minCost(int[][] matrix) {
        int numCols = matrix[0].length;
        Integer[][] accumulateCostMatrix = new Integer[matrix.length][numCols];
        Deque<Coordinate> evalDeck = new ArrayDeque<>();

        evalDeck.add(createCoordinate(0, 0));
        accumulateCostMatrix[0][0] = matrix[0][0];

        while (!evalDeck.isEmpty()) {
            Coordinate parent = evalDeck.pop();
            List<Coordinate> adjacentCoords = getAdjacent(matrix, parent);

            int parentCost = accumulateCostMatrix[parent.getRow()][parent.getCol()];
            for (Coordinate adjacentCoord : adjacentCoords) {
                int row = adjacentCoord.getRow();
                int col = adjacentCoord.getCol();

                Integer childCurrentCost = accumulateCostMatrix[row][col];
                Integer childNewCost = parentCost + matrix[row][col];

                if (childCurrentCost == null) {
                    accumulateCostMatrix[row][col] = childNewCost;
                    evalDeck.add(adjacentCoord);
                } else if (childNewCost < childCurrentCost) {
                    accumulateCostMatrix[row][col] = childNewCost;
                    evalDeck.add(adjacentCoord);
                }
            }
        }
        return accumulateCostMatrix[matrix.length - 1][numCols - 1];
    }

    private List<Coordinate> getAdjacent(int[][] matrix, Coordinate coordinate) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coordinate> adjacent = new ArrayList<>();

        int nextRow = coordinate.getRow() + 1;
        if (nextRow < numRows) {
            adjacent.add(createCoordinate(nextRow, coordinate.col));
        }

        int nextCol = coordinate.getCol() + 1;
        if (nextCol < numCols) {
            adjacent.add(createCoordinate(coordinate.getRow(), nextCol));
        }

        if (nextRow < numRows && nextCol < numCols) {
            adjacent.add(createCoordinate(nextRow, nextCol));
        }

        return adjacent;
    }

    private Coordinate createCoordinate(int row, int col) {
        String key = String.valueOf(row) + String.valueOf(col);
        return coordinateCache.computeIfAbsent(key, v -> new Coordinate(row, col));
    }

    private void test1() {
        int[][] matrix = new int[][]{
                {4, 5, 6},
                {1, 2, 3},
                {0, 1, 2}
        };
        assertThat(minCost(matrix), is(8));
    }

    private void test2() {
        int[][] matrix = new int[][]{
                {4, 1, 3, 1},
                {5, 7, 1, 4},
                {2, 1, 2, 6},
                {5, 1, 2, 1},
        };
        assertThat(minCost(matrix), is(9));
    }

    private static class Coordinate {
        private final int row, col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }
}
