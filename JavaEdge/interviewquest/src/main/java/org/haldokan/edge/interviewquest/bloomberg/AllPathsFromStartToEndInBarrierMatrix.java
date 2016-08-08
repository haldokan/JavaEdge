package org.haldokan.edge.interviewquest.bloomberg;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question - DFS while updating the path as recursion goes down and up the matrix.
 * Every time we hit the end coordinate we copy the path to the paths collection.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Given a MxN grid, like :
 * 111     111
 * 111 or  001
 * 111     001
 * Write a function to return all possible paths from start (0, 0) to destination (M-1, N-1). Allowed moves: right,
 * down, and diagonal down. Value 1 indicates moves is possible, 0 indicates move not possible.
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class AllPathsFromStartToEndInBarrierMatrix {
    private final Map<String, Coordinate> coordinateCache = new HashMap<>();

    public static void main(String[] args) {
        AllPathsFromStartToEndInBarrierMatrix driver = new AllPathsFromStartToEndInBarrierMatrix();
        driver.test();
    }

    public void allPaths(int[][] matrix,
                         Coordinate coordinate,
                         List<Coordinate> path,
                         List<List<Coordinate>> paths) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        path.add(coordinate);
        List<Coordinate> adjacentCoords = getAdjacent(matrix, coordinate);

        for (Coordinate adjacentCoord : adjacentCoords) {
            allPaths(matrix, adjacentCoord, path, paths);
        }

        if (coordinate.equals(getCoordinate(numRows - 1, numCols - 1))) {
            List<Coordinate> newPath = path.stream().collect(Collectors.toList());
            // same path occur more than once - I don't see how to prevent that. We cannot mark coordinates as visited
            // because we need to use the same coordinate in different paths.
            if (!paths.contains(newPath)) {
                paths.add(newPath);
            }
        }
        // at this point the coordinate is processed (all its children processed). Because of the way we maintain the
        // path the current coordinate is the last coordinate in the path
        path.remove(path.size() - 1);
    }

    private List<Coordinate> getAdjacent(int[][] matrix, Coordinate coordinate) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coordinate> adjacentCoords = new ArrayList<>();
        Coordinate adjacentCoord;

        int nextRow = coordinate.getRow() + 1;
        if (nextRow < numRows && matrix[nextRow][coordinate.getCol()] != 0) {
            adjacentCoord = createCoordinate(nextRow, coordinate.getCol());
            adjacentCoords.add(adjacentCoord);
        }

        int nextCol = coordinate.getCol() + 1;
        if (nextCol < numCols && matrix[coordinate.getRow()][nextCol] != 0) {
            adjacentCoord = createCoordinate(coordinate.getRow(), nextCol);
            adjacentCoords.add(adjacentCoord);
        }

        if (nextRow < numRows && nextCol < numCols && matrix[nextRow][nextCol] != 0) {
            adjacentCoord = createCoordinate(nextRow, nextCol);
            adjacentCoords.add(adjacentCoord);
        }

        return adjacentCoords;
    }

    private Coordinate createCoordinate(int row, int col) {
        String key = String.valueOf(row) + String.valueOf(col);
        return coordinateCache.computeIfAbsent(key, v -> new Coordinate(row, col));
    }

    private Coordinate getCoordinate(int row, int col) {
        return coordinateCache.get(String.valueOf(row) + String.valueOf(col));
    }

    private void test() {
        int[][] matrix = new int[][]{
                {1, 1, 1, 1},
                {0, 1, 1, 1},
                {0, 0, 0, 1},
        };
        List<List<Coordinate>> paths = new ArrayList<>();

        allPaths(
                matrix,
                createCoordinate(0, 0),
                new ArrayList<>(),
                paths);

        paths.stream().forEach(System.out::println);
        assertThat(paths.size(), is(10));

        List<Coordinate> path1 = Lists.newArrayList(
                createCoordinate(0, 0),
                createCoordinate(0, 1),
                createCoordinate(1, 1),
                createCoordinate(1, 2),
                createCoordinate(1, 3),
                createCoordinate(2, 3)
        );
        assertThat(paths.contains(path1), is(true));

        List<Coordinate> path2 = Lists.newArrayList(
                createCoordinate(0, 0),
                createCoordinate(0, 1),
                createCoordinate(1, 1),
                createCoordinate(1, 2),
                createCoordinate(2, 3)
        );
        assertThat(paths.contains(path2), is(true));

        List<Coordinate> path3 = Lists.newArrayList(
                createCoordinate(0, 0),
                createCoordinate(0, 1),
                createCoordinate(0, 2),
                createCoordinate(1, 2),
                createCoordinate(1, 3),
                createCoordinate(2, 3)
        );
        assertThat(paths.contains(path3), is(true));

        List<Coordinate> path4 = Lists.newArrayList(
                createCoordinate(0, 0),
                createCoordinate(0, 1),
                createCoordinate(0, 2),
                createCoordinate(1, 2),
                createCoordinate(2, 3)
        );
        assertThat(paths.contains(path4), is(true));

        // assert the rest of the path ...
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

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }
}
