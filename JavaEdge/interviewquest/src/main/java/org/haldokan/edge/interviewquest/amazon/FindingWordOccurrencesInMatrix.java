package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - the question is under specified. My solution assumes that the word
 * occurrences don't overlap. todo: support overlapping words.
 * The Question: 3_STAR
 * <p>
 * Suppose you have a 2 dimensional Array and you have a String say"Amazon"inside the Array such that the individual
 * characters can be present from Left to Right,Right to Left,Top to down and down to up.
 * <p>
 * I will explain with example:
 * <p>
 * char[][]a={
 * {B,B,A,B,B,N},
 * {B,B,M,B,B,O},
 * {B,B,A,B,B,Z},
 * {N,O,Z,B,B,A},
 * {B,B,B,B,B,M},
 * {B,B,B,B,B,A}
 * };
 * <p>
 * The above Array has two Amazon Strings.You need to return the count of such strings.
 * <p>
 * Created by haytham.aldokanji on 7/23/16.
 */

public class FindingWordOccurrencesInMatrix {
    private static final int[] INDEX_OFFSETS = new int[]{-1, 1};

    public static void main(String[] args) {
        FindingWordOccurrencesInMatrix driver = new FindingWordOccurrencesInMatrix();
        driver.test1();
        driver.test2();
    }

    public List<Coordinate[]> countWordOccurrences(char[][] matrix, char[] word) {
        Deque<Coordinate> visitedDeck = new ArrayDeque<>();
        List<Coordinate[]> occurrences = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            char[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                boolean[][] visitedMatrix = new boolean[matrix.length][matrix[0].length];
                if (matrix[coordinate.row][coordinate.col] == word[0]) {
                    dfs(matrix, visitedMatrix, coordinate, word, visitedDeck, 1, occurrences);
                }
            }
        }
        return occurrences;
    }

    private void dfs(char[][] matrix,
                     boolean[][] visitedMatrix,
                     Coordinate coordinate,
                     char[] word,
                     Deque<Coordinate> visited,
                     int index,
                     List<Coordinate[]> occurrences) {
        visitedMatrix[coordinate.row][coordinate.col] = true;
        visited.addFirst(coordinate);

        if (index == word.length) {
            int i = 0;
            Coordinate[] occurrence = new Coordinate[word.length];
            while (!visited.isEmpty()) {
                Coordinate visitedCoord = visited.remove();
                occurrence[i++] = visitedCoord;
            }
            occurrences.add(occurrence);
            return;
        }
        Coordinate[] neighbors = getNeighbors(coordinate, matrix, visitedMatrix, word[index]);
        if (neighbors.length == 0) {
            while (!visited.isEmpty()) {
                Coordinate visitedCoord = visited.remove();
                visitedMatrix[visitedCoord.row][visitedCoord.col] = false;
            }
        }
        for (Coordinate neighbor : neighbors) {
            dfs(matrix, visitedMatrix, neighbor, word, visited, index + 1, occurrences);
        }
    }

    private Coordinate[] getNeighbors(Coordinate coordinate,
                                      char[][] matrix,
                                      boolean[][] visitedMatrix,
                                      char nextLetter) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Coordinate> neighbors = new ArrayList<>();
        for (int offset : INDEX_OFFSETS) {
            neighbors.add(new Coordinate(coordinate.row + offset, coordinate.col));
            neighbors.add(new Coordinate(coordinate.row, coordinate.col + offset));
        }

        return neighbors.stream().filter(neighbor -> {
            int row = neighbor.row;
            int col = neighbor.col;
            return row >= 0
                    && row < numRows
                    && col >= 0
                    && col < numCols
                    && !visitedMatrix[row][col]
                    && matrix[row][col] == nextLetter;
        }).toArray(Coordinate[]::new);
    }

    private void test1() {
        char[][] matrix = {
                {'B', 'B', 'A', 'B', 'B', 'N'},
                {'B', 'B', 'M', 'B', 'B', 'O'},
                {'B', 'B', 'A', 'B', 'B', 'Z'},
                {'N', 'O', 'Z', 'B', 'B', 'A'},
                {'B', 'B', 'B', 'B', 'B', 'M'},
                {'B', 'B', 'B', 'B', 'B', 'A'}
        };
        List<Coordinate[]> occurrences = countWordOccurrences(matrix, new char[]{'A', 'M', 'A', 'Z', 'O', 'N'});
        occurrences.stream().forEach(occurrence -> System.out.printf("%s%n", Arrays.toString(occurrence)));
        System.out.printf("%s%n", "--------------");
        assertThat(occurrences.size(), is(2));
    }

    private void test2() {
        char[][] matrix = {
                {'A', 'M', 'A', 'Z', 'O', 'N'},
                {'N', 'B', 'M', 'B', 'B', 'O'},
                {'O', 'Z', 'A', 'Z', 'O', 'Z'},
                {'N', 'O', 'Z', 'B', 'B', 'A'},
                {'B', 'B', 'B', 'B', 'B', 'M'},
                {'B', 'B', 'B', 'B', 'B', 'A'}
        };
        List<Coordinate[]> occurrences = countWordOccurrences(matrix, new char[]{'A', 'M', 'A', 'Z', 'O', 'N'});
        occurrences.stream().forEach(occurrence -> System.out.printf("%s%n", Arrays.toString(occurrence)));
        System.out.printf("%s%n", "--------------");
        assertThat(occurrences.size(), is(3));
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
