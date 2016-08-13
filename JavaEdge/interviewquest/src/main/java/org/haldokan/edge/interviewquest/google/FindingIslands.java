package org.haldokan.edge.interviewquest.google;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - loop thru the matrix doing BFS while marking visited islands
 * <p>
 * The Question: 4_STAR
 * <p>
 * Given a map N x N, 2-D array
 * 0 - sea
 * X - land
 * <p>
 * Count the islands. Land is connected by one of the 4-Neighbor connections, i.e.: above, down, left and right.
 * The map below should have 6 islands
 * <p>
 * X0000X0000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * X0000XXXX0000000000X00000
 * 00000000X000000000XXX0000
 * 000X0000X0000000000000000
 * 000XXXX0X0000000000000000
 * 000000XXX0000000000000000
 * 000000000000000000000X000
 * 0000000000000000000X0XX00
 * 0000000000000000000XXX000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * <p>
 * Created by haytham.aldokanji on 04/29/16.
 */
public class FindingIslands {
    private static final int[] INDEX_OFFSETS = new int[]{-1, 1};
    private static final char ISLAND = 'X';
    private static final char VISITED = 'V';

    public static void main(String[] args) {
        FindingIslands driver = new FindingIslands();

        char[][] topography = driver.makeTopography();

        int numOfIslands = driver.findNumberOfIslands(topography);
        driver.printTopography(topography);

        assertThat(numOfIslands, is(6));
    }

    public int findNumberOfIslands(char[][] topography) {
        int numberOfIslands = 0;

        for (int i = 0; i < topography.length; i++) {
            char[] row = topography[i];

            for (int j = 0; j < row.length; j++) {
                char chr = row[j];

                if (chr == ISLAND) {
                    numberOfIslands++;
                    Deque<Land> evalDeque = new ArrayDeque<>();
                    evalDeque.add(Land.create(i, j));

                    while (!evalDeque.isEmpty()) {
                        Land land = evalDeque.remove();
                        topography[land.x][land.y] = VISITED;

                        Land[] neighbors = getNeighbors(land, topography);
                        Arrays.stream(neighbors).forEach(evalDeque::add);
                    }
                }
            }
        }
        return numberOfIslands;
    }

    private Land[] getNeighbors(Land land, char[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<Land> neighbors = new ArrayList<>();
        for (int offset : INDEX_OFFSETS) {
            neighbors.add(new Land(land.x + offset, land.y));
            neighbors.add(new Land(land.x, land.y + offset));
        }

        return neighbors.stream().filter(neighbor -> {
            int row = neighbor.x;
            int col = neighbor.y;
            return row >= 0 &&
                    row < numRows &&
                    col >= 0 && col < numCols &&
                    matrix[row][col] == ISLAND;
        }).toArray(Land[]::new);
    }

    private void printTopography(char[][] topography) {
        Arrays.stream(topography).map(Arrays::toString).forEach(System.out::println);
    }

    private char[][] makeTopography() {
        int size = 25;

        char[][] topography = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(topography[i], '0');
        }
        topography[0][0] = 'X';
        topography[0][5] = 'X';
        topography[3][0] = 'X';
        topography[3][5] = 'X';
        topography[3][19] = 'X';
        topography[3][6] = 'X';
        topography[3][7] = 'X';
        topography[3][8] = 'X';
        topography[4][8] = 'X';
        topography[5][8] = 'X';
        topography[6][8] = 'X';
        topography[7][6] = 'X';
        topography[7][8] = 'X';
        topography[4][18] = 'X';
        topography[4][19] = 'X';
        topography[4][20] = 'X';
        topography[5][3] = 'X';
        topography[5][3] = 'X';
        topography[6][3] = 'X';
        topography[6][4] = 'X';
        topography[6][5] = 'X';
        topography[6][6] = 'X';
        topography[7][7] = 'X';
        topography[9][19] = 'X';
        topography[8][21] = 'X';
        topography[9][21] = 'X';
        topography[9][22] = 'X';
        topography[10][21] = 'X';
        topography[10][20] = 'X';
        topography[10][19] = 'X';

        return topography;
    }

    private static class Land {
        private final int x, y;

        public Land(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Land create(int x, int y) {
            return new Land(x, y);
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }
    }
}
