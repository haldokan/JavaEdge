package org.haldokan.edge.interviewquest.google;

import java.util.*;
import java.util.stream.Collectors;

/**
 * My solution to a Google interview question - It finds and marks all such regions (not just one)
 * The Question: 5_STAR
 * <p>
 * given a 2D matrix M, is filled either using X or O, you need to mark the region which is filled
 * by O and surrounded by X and fill it with Z.
 * <p>
 * example 1:
 * <p>
 * X X X X X
 * X X O O X
 * X X O O X
 * O X X X X
 * <p>
 * Answer :
 * <p>
 * X X X X X
 * X X X X X
 * X X X X X
 * O X X X X
 * <p>
 * example 2:
 * <p>
 * X X X X X
 * X X O O X
 * X X O O O
 * O X X X X
 * <p>
 * answer 2:
 * X X X X X
 * X X O O X
 * X X O O O
 * O X X X X
 * Created by haytham.aldokanji on 7/14/16.
 */
public class MarkingFullyEnclosedRegionsInMatrix {

    public static void main(String[] args) {
        MarkingFullyEnclosedRegionsInMatrix driver = new MarkingFullyEnclosedRegionsInMatrix();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
    }

    public void mark(char[][] matrix) {
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            char[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                char chr = row[j];
                if (chr == 'O') {
                    Coordinate coordinate = createCoordinate(i, j, matrix.length, row.length);
                    attacheToRegions(coordinate, regions);
                }
            }
        }
        regions.stream()
                .filter(region -> !region.borderRegion)
                .forEach(region -> region.coordinates.forEach(coordinate -> matrix[coordinate.row][coordinate.col] = 'Z'));
    }

    private void attacheToRegions(Coordinate coordinate, List<Region> regions) {
        List<Region> recipientRegions = new ArrayList<>();

        for (Iterator<Region> iterator = regions.iterator(); iterator.hasNext(); ) {
            Region region = iterator.next();
            if (region.expandsRegion(coordinate)) {
                iterator.remove();
                region.add(coordinate);
                recipientRegions.add(region);
            }
        }
        if (recipientRegions.isEmpty()) {
            Region newRegion = new Region();
            newRegion.add(coordinate);
            regions.add(newRegion);
        } else {
            Region mergedRegion = recipientRegions.remove(0);
            recipientRegions.forEach(mergedRegion::merge);
            regions.add(mergedRegion);
        }
    }

    private Coordinate createCoordinate(int row, int col, int numRows, int numCols) {
        boolean borderCoord = row == 0
                || row == numRows - 1
                || col == 0
                || col == numCols - 1;
        return new Coordinate(row, col, borderCoord);
    }

    private void test1() {
        char[][] matrix = new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'O', 'X'},
                {'X', 'X', 'O', 'O', 'O', 'X', 'X', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'O', 'O', 'O', 'O', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'O', 'O', 'O', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'O', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        };
        mark(matrix);
        printMatrix(matrix);
    }

    private void test2() {
        char[][] matrix = new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'O', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'O', 'X'},
                {'X', 'X', 'O', 'O', 'O', 'X', 'X', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'O', 'O', 'O', 'O', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'O', 'O', 'O', 'O', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'O', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        };
        mark(matrix);
        printMatrix(matrix);
    }

    private void test3() {
        char[][] matrix = new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'X'},
                {'X', 'O', 'O', 'O', 'X', 'X', 'X', 'O', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'O', 'X', 'O', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'O', 'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        };
        mark(matrix);
        printMatrix(matrix);
    }

    private void test4() {
        char[][] matrix = new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'X'},
                {'X', 'O', 'O', 'O', 'X', 'X', 'X', 'O', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'O', 'X', 'O', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'O', 'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'O'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        };
        mark(matrix);
        printMatrix(matrix);
    }

    private void test5() {
        char[][] matrix = new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'O', 'X', 'X', 'X', 'O', 'X', 'X'},
                {'X', 'X', 'X', 'O', 'X', 'X', 'X', 'O', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        };
        mark(matrix);
        printMatrix(matrix);
    }

    private void printMatrix(char[][] matrix) {
        for (char[] row : matrix) {
            System.out.printf("%s%n", Arrays.toString(row));
        }
        System.out.printf("%s%n", "------------------------------------");
    }

    private static class Region {
        private final Set<Coordinate> coordinates = new HashSet<>();
        private boolean borderRegion;

        public Region merge(Region other) {
            if (other.borderRegion) {
                borderRegion = true;
            }
            coordinates.addAll(other.coordinates.stream().collect(Collectors.toList()));
            return this;
        }

        public void add(Coordinate coordinate) {
            coordinates.add(coordinate);
            if (!borderRegion) {
                borderRegion = coordinate.borderCoordinate;
            }
        }

        public boolean contains(Coordinate coordinate) {
            return coordinates.contains(coordinate);
        }

        public boolean expandsRegion(Coordinate newCoord) {
            for (Coordinate coordinate : coordinates) {
                if (coordinate.isNeighbor(newCoord)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "Region{" +
                    "coordinates=" + coordinates +
                    ", borderRegion=" + borderRegion +
                    '}';
        }
    }

    private static class Coordinate {
        private final int row, col;
        private final boolean borderCoordinate;

        public Coordinate(int row, int col, boolean borderCoordinate) {
            this.row = row;
            this.col = col;
            this.borderCoordinate = borderCoordinate;
        }

        public boolean isNeighbor(Coordinate other) {
            return row == other.row && Math.abs(col - other.col) == 1
                    || col == other.col && Math.abs(row - other.row) == 1
                    || Math.abs(row - other.row) == 1 && Math.abs(col - other.col) == 1;
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
