package org.haldokan.edge.interviewquest.google;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - basically for each guard construct an n-arry tree (at most 4 children per
 * node) and do BFS while increasing the distance from the root (guard). Including child nodes in the BFS depends on whether they are
 * 'block', 'guard', or 'path'; and the child distance (computed in the same or a different guard BFS) compared to the new distance.
 *
 * The Question: 5_STAR
 *
 * Imagine a museum floor that looks like this:
 * <p>
 * .#.G.
 * ..#..
 * G....
 * ..#..
 * <p>
 * G == Museum Guard
 * # == obstruction/impassable obstacle
 * . == empty space
 * <p>
 * Write a piece of code that will find the nearest guard for each open floor space. Diagonal moves are not allowed.
 * The output should convey this information:
 * <p>
 * 2#1G1
 * 12#12
 * G1223
 * 12#34
 * <p>
 * You may choose how you want to receive the input and output. For example, you may use a 2-d array, as depicted here,
 * or you may use a list of points with features, if you deem that easier to work with, as long as the same information
 * is conveyed.
 * <p>
 * Created by haytham.aldokanji on 5/21/16.
 */
public class ClosestGuardOnMuseumFloor {
    private static final char GUARD = 'G';
    private static final char BLOCK = '#';
    private static final char PATH = '.';
    private static final int[] INDEX_OFFSETS = new int[]{-1, 1};

    public static void main(String[] args) {
        ClosestGuardOnMuseumFloor driver = new ClosestGuardOnMuseumFloor();

        driver.testGetGuards();
        driver.testSetupSolutionMatrix();
        driver.testGetNeighbors();
        driver.testDistanceToGuard();
    }

    public String[][] distanceToGuard(char[][] floor) {
        int[][] guards = getGuards(floor);
        // we assume that the floor matrix is read-only so we construct a solution matrix
        String[][] solution = setupSolutionMatrix(floor);

        Deque<int[]> evalQueue = new ArrayDeque<>();
        for (int[] guard : guards) {
            evalQueue.add(guard);

            while (!evalQueue.isEmpty()) {
                int[] spot = evalQueue.pop();
                int currentSpotDistance = spotDistance(spot, solution);
                int[][] neighbors = getNeighbors(spot, floor);

                int[][] actualNeighbors = Arrays.stream(neighbors).filter(neighbor -> {
                    String potentialDistance = solution[neighbor[0]][neighbor[1]];
                    return potentialDistance.equals(String.valueOf(PATH))
                            || Integer.parseInt(potentialDistance) > currentSpotDistance + 1;
                }).toArray(int[][]::new);

                Arrays.stream(actualNeighbors).forEach(neighbor -> {
                    int childDistance = currentSpotDistance + 1;
                    solution[neighbor[0]][neighbor[1]] = String.valueOf(childDistance);
                    evalQueue.add(neighbor);
                });
            }
        }
        return solution;
    }

    private int[][] getGuards(char[][] floor) {
        List<int[]> guards = new ArrayList<>();

        for (int i = 0; i < floor.length; i++) {
            char[] row = floor[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] == GUARD) {
                    guards.add(new int[]{i, j});
                }
            }
        }
        return guards.toArray(new int[guards.size()][]);
    }

    private String[][] setupSolutionMatrix(char[][] floor) {
        // can't be char[][] since distance can be 2-digit number
        String[][] solution = new String[floor.length][];

        for (int i = 0; i < floor.length; i++) {
            char[] row = floor[i];
            String[] rowToString = new String[row.length];

            for (int j = 0; j < rowToString.length; j++) {
                rowToString[j] = String.valueOf(row[j]);
            }
            solution[i] = rowToString;
        }
        return solution;
    }

    private int[][] getNeighbors(int[] spot, char[][] floor) {
        int numRows = floor.length;
        int numCols = floor[0].length;

        List<int[]> neighbors = new ArrayList<>();
        for (int offset : INDEX_OFFSETS) {
            neighbors.add(new int[]{spot[0] + offset, spot[1]});
            neighbors.add(new int[]{spot[0], spot[1] + offset});
        }

        return neighbors.stream().filter(neighbor -> {
            int row = neighbor[0];
            int col = neighbor[1];
            return row >= 0 && row < numRows && col >= 0 && col < numCols && floor[row][col] == PATH;
        }).toArray(int[][]::new);
    }

    private int spotDistance(int[] spot, String[][] solution) {
        String potentialDistance = solution[spot[0]][spot[1]];
        if (potentialDistance.equals(String.valueOf(GUARD))) {
            return 0;
        }
        return Integer.parseInt(solution[spot[0]][spot[1]]);
    }

    private char[][] makeFloor() {
        return new char[][]{
                new char[]{PATH, BLOCK, PATH, GUARD, PATH},
                new char[]{PATH, PATH, BLOCK, PATH, PATH},
                new char[]{GUARD, PATH, PATH, PATH, PATH},
                new char[]{PATH, PATH, BLOCK, PATH, PATH}
        };
    }

    private void testGetGuards() {
        char[][] floor = makeFloor();
        int[][] guards = getGuards(floor);

        assertThat(guards.length, is(2));
        assertThat(guards[0], is(new int[]{0, 3}));
        assertThat(guards[1], is(new int[]{2, 0}));
    }

    private void testSetupSolutionMatrix() {
        char[][] floor = makeFloor();
        String[][] solution = setupSolutionMatrix(floor);
        for (int i = 0; i < floor.length; i++) {
            char[] row = floor[i];
            for (int j = 0; j < row.length; j++) {
                assertThat(String.valueOf(floor[i][j]), is(solution[i][j]));
            }
        }
    }

    private void testGetNeighbors() {
        char[][] floor = makeFloor();

        int[][] neighbors = getNeighbors(new int[]{2, 1}, floor);
        assertThat(neighbors.length, is(3));
        assertThat(Arrays.asList(neighbors), containsInAnyOrder(new int[]{1, 1}, new int[]{2, 2}, new int[]{3, 1}));

        neighbors = getNeighbors(new int[]{2, 0}, floor);
        assertThat(neighbors.length, is(3));
        assertThat(Arrays.asList(neighbors), containsInAnyOrder(new int[]{1, 0}, new int[]{2, 1}, new int[]{3, 0}));

        neighbors = getNeighbors(new int[]{0, 2}, floor);
        assertThat(neighbors.length, is(0));
    }

    private void testDistanceToGuard() {
        char[][] floor = makeFloor();
        String[][] expected = new String[][]{
                new String[]{"2", "#", "1", "G", "1"},
                new String[]{"1", "2", "#", "1", "2"},
                new String[]{"G", "1", "2", "2", "3"},
                new String[]{"1", "2", "#", "3", "4"},
        };

        String[][] solution = distanceToGuard(floor);
        Arrays.stream(solution).forEach(e -> System.out.println(Arrays.toString(e)));
        assertThat(solution, is(expected));
    }
}
