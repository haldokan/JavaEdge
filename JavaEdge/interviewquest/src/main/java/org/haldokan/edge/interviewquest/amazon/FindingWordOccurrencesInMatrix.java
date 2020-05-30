package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - I solved the question taking the more complex assumptions and returning
 * the word coordinates instead of just the count. The assumption I worked with is that the letters comprising the search
 * word can participate in all the word occurrences. I use DFS to find the word occurrences. As recursion probes the
 * matrix we keep track of visited coordinates and of the substrings that form part of multiple occurrences.
 * The Question: 5_STAR
 * <p>
 * Suppose you have a 2 dimensional Array and you have a String say "Amazon" inside the Array such that the individual
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
        driver.test3();
    }

    public List<Coordinate[]> listWordOccurrences(char[][] matrix, char[] word) {
        Deque<Coordinate> visitedDeck = new ArrayDeque<>();
        List<Coordinate[]> occurrences = new ArrayList<>();
        // 'dfs' insures that the matrix is returned to original state after each iteration of the inner loop below
        boolean[][] visitedMatrix = new boolean[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            char[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                Coordinate coordinate = new Coordinate(i, j);
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
            Coordinate[] occurrence = new Coordinate[word.length];
            for (int i = word.length - 1; i >= 0; i--) {
                Coordinate visitedCoord = visited.remove();
                occurrence[i] = visitedCoord;
            }
            // necessary to stack back the coordinate and let them removed as recursive calls return
            for (Coordinate coord : occurrence) {
                visited.addFirst(coord);
            }
            occurrences.add(occurrence);
            visitedMatrix[coordinate.row][coordinate.col] = false;
            return;
        }
        Coordinate[] neighbors = getNeighbors(coordinate, matrix, visitedMatrix, word[index]);
        for (Coordinate neighbor : neighbors) {
            dfs(matrix, visitedMatrix, neighbor, word, visited, index + 1, occurrences);
            visited.remove();
            visitedMatrix[coordinate.row][coordinate.col] = false;
        }
        visitedMatrix[coordinate.row][coordinate.col] = false;
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
        List<Coordinate[]> occurrences = listWordOccurrences(matrix, new char[]{'A', 'M', 'A', 'Z', 'O', 'N'});
        occurrences.forEach(occurrence -> System.out.printf("%s%n", Arrays.toString(occurrence)));
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
        List<Coordinate[]> occurrences = listWordOccurrences(matrix, new char[]{'A', 'M', 'A', 'Z', 'O', 'N'});
        occurrences.forEach(occurrence -> System.out.printf("%s%n", Arrays.toString(occurrence)));
        System.out.printf("%s%n", "--------------");
        assertThat(occurrences.size(), is(8));
    }

    private void test3() {
        char[][] matrix = {
                // palindrome
                {'r', 'a', 'c', 'e', 'c', 'a', 'r'},
                {'a', 'c', 'e', 'c', 'a', 'r', 'x'}
        };
        List<Coordinate[]> occurrences = listWordOccurrences(matrix, new char[]{'r', 'a', 'c', 'e', 'c', 'a', 'r'});
        occurrences.forEach(occurrence -> System.out.printf("%s%n", Arrays.toString(occurrence)));
        System.out.printf("%s%n", "--------------");
        assertThat(occurrences.size(), is(22));
    }

    private static final class Coordinate {
        private final int row, col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }
}
