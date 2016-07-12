package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * My solution to a Google interview question - here simplified to make coordinates integers so the rectangle can be
 * mapped to a matrix. I also ignore the sensor radius and reduce it to an obstacle coordinate in the matrix.
 * I provide another solution where the coordinates are doubles and sensors have radius in FindingPathAvoidingSensors2.
 * The Question: 4_STAR
 * <p>
 * Given a rectangle with top-left(a,b) and bottom-right(c,d) coordinates. Also given some coordinates (m,n) of sensors
 * inside the rectangle. All sensors can sense in a circular region of radius r about their centre (m,n).
 * Total N sensors are given. A player has to reach from left side of rectangle to its right side (i.e. he can start his
 * journey from any point whose x coordinate is a and y coordinate is b<=y<=d. He has to end his journey to any point
 * whose x coordinate is c and y coordinate is b<=y<=d).
 * <p>
 * Write an algorithm to find path (possibly shortest but not necessary) from start to end as described above.
 * <p>
 * Note: all coordinates are real numbers
 * <p>
 * <p>
 * (a,b)
 * X.......................................................|
 * |.......................................................E
 * |.......................................................|
 * |.......................................................|
 * S.......................................................|
 * |.......................................................X(c,d)
 * <p>
 * Edit: You have to avoid sensors.
 * Also you can move in any direction any time.
 * <p>
 * Created by haytham.aldokanji on 7/11/16.
 */
public class FindingPathAvoidingSensors1 {
    private static final int[] INDEX_OFFSETS = new int[]{-1, 1};
    private static final char SENSOR = 'S';
    private static final char PATH = 'P';
    private static final char VISITED = 'V';

    public static void main(String[] args) {
        FindingPathAvoidingSensors1 driver = new FindingPathAvoidingSensors1();
        driver.test1();
        driver.test2();
    }

    public List<Coordinate> getPath(char[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coordinate> path = new ArrayList<>();
        Deque<Coordinate> evalDeck = new ArrayDeque<>();

        boolean found = false;
        for (int row = 0; row < numRows && !found; row++) {
            if (matrix[row][0] != SENSOR) {
                evalDeck.addFirst(new Coordinate(row, 0));
                while (!evalDeck.isEmpty() && !found) {
                    Coordinate coordinate = evalDeck.removeFirst();
                    path.add(coordinate);
                    matrix[coordinate.row][coordinate.col] = VISITED;

                    if (coordinate.col == numCols - 1) {
                        found = true;
                    } else {
                        Coordinate[] neighbors = getNeighbors(coordinate, matrix);
                        if (neighbors.length == 0) {
                            path.remove(coordinate);
                        } else {
                            for (Coordinate neighbor : neighbors) {
                                evalDeck.addFirst(neighbor);
                            }
                        }
                    }
                }
            }
        }
        return path;
    }

    private Coordinate[] getNeighbors(Coordinate coordinate, char[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coordinate> neighbors = new ArrayList<>();
        for (int offset : INDEX_OFFSETS) {
            neighbors.add(new Coordinate(coordinate.row + offset, coordinate.col));
            neighbors.add(new Coordinate(coordinate.row, coordinate.col + offset));
        }

        // not the sort on row descending insures preference to moving from left to right
        return neighbors.stream().filter(neighbor -> {
            int row = neighbor.row;
            int col = neighbor.col;
            return row >= 0 && row < numRows && col >= 0 && col < numCols && matrix[row][col] == PATH;
        }).sorted((c1, c2) -> c2.col - c1.col).toArray(Coordinate[]::new);
    }

    private void test1() {
        char[][] matrix = new char[][]{
                {'P', 'S', 'P', 'S', 'S'},
                {'P', 'P', 'S', 'P', 'P'},
                {'P', 'S', 'P', 'P', 'P'},
                {'P', 'P', 'P', 'P', 'S'},
        };
        List<Coordinate> path = getPath(matrix);
        System.out.printf("%s%n", path);

    }

    private void test2() {
        char[][] matrix = new char[][]{
                {'P', 'S', 'S', 'P', 'P'},
                {'P', 'S', 'P', 'P', 'S'},
                {'P', 'S', 'P', 'S', 'S'},
                {'P', 'P', 'P', 'P', 'S'},
        };
        List<Coordinate> path = getPath(matrix);
        System.out.printf("%s%n", path);
    }

    private static final class Coordinate {
        private final int row, col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
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
