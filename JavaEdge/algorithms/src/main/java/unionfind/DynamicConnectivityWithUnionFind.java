package unionfind;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * My implementation of dynamic connectivity (or percolation) is based on the algorithm described in
 * Algorithms: 24-part Lecture Series by Professor Robert Sedgewick.
 *
 * The Question: 4.5-STAR
 *
 * The questions is if we start with an N X N grid of black squares, and assuming that only white squares allow horizontal
 * and vertical movements, what percentage of the squares should be flipped to white in orders to percolate from
 * the top row of the grid to the bottom row? This is called the Site Vacancy Probability: SVP
 *
 * There is not a mathematical solution to this problem and only a Monte Carlo simulation like the one implemented here can
 * give answer for large grids. Evidence from simulations (including the one here) is that there is a sharp threshold for
 * SVP of about 0.593. SVPs less than that will render the grid (or network) almost always impervious (cannot percolate
 * from top to bottom row). SVPs greater than the threshold will make it almost always porous.
 *
 * I employ the UnionFind Algorithm (also described in the same lecture series) to help calculating the percolation status
 * as the simulation runs. To avoid having to check if any square on the top row can connect to any square on the bottom
 * row which makes the algorithm runs in N^2 I employ the clever trick suggested by Prof. Sedgewick: add 2 virtual
 * squares and connect the first to the top row, and the second to the bottom row. Doing that we can run the simulation
 * using the UnionFind algorithm until the 2 virtual squares are connected, meaning the grid percolates.
 *
 * 03/23/21
 */
public class DynamicConnectivityWithUnionFind {
    private static final Random rand = new Random();

    private final int[][] grid; // grid of black (0) and white (1) squares
    private final int[] flatGrid; // the UnionFind array representation of the grid
    private final UnionFind3 unionFind;
    private final int[] randomSquares; // helps select a *new* random square at each iteration of the simulation
    private int numRemainingSquares; // helps with the random selection of squares

    public DynamicConnectivityWithUnionFind(int gridDimension) {
        this.grid = new int[gridDimension][gridDimension];
        // we add 2 extra slots for the 2 virtual squares
        this.flatGrid = IntStream.range(0, gridDimension * gridDimension + 2).toArray();
        this.randomSquares = IntStream.range(0, gridDimension * gridDimension).toArray();
        this.numRemainingSquares = this.randomSquares.length;
        this.unionFind = new UnionFind3(flatGrid);
        // connect the top row to the 1st virtual square and the bottom row to the 2nd virtual square
        connectToStartAndDest();
    }

    public double runSimulation() {
        // while the 2 virtual squares are not connected
        while (!unionFind.connected(flatGrid[flatGrid.length - 1], flatGrid[flatGrid.length - 2])) {
            int square = randomSquare();
            // convert square number to square coordinates on the grid
            int[] squarePos = new int[]{square / grid.length, square % grid.length};
            grid[squarePos[0]][squarePos[1]] = 1; // switch grid to white (1)
            List<int[]> neighbors = neighbors(squarePos);
            // connect the randomly flipped square to all its white (1) neighbors
            for (int[] neighbor : neighbors) {
                int x = neighbor[0];
                int y = neighbor[1];
                int neighborSquare = x * grid.length + y; // convert square coordinates to square number
                unionFind.connect(square, neighborSquare);
            }
        }
        // calculate the SVP at the end of the simulation
        long numConnected = Arrays.stream(grid).flatMapToInt(Arrays::stream).filter(v -> v == 1).count();
        return (double) numConnected / (double) (grid.length * grid.length);
    }

    private void connectToStartAndDest() {
        for (int i = 0; i < grid.length; i++) {
            unionFind.connect(i, flatGrid.length - 2);
        }

        for (int i = flatGrid.length - 3; i >= flatGrid.length - grid.length - 2; i--) {
            unionFind.connect(i, flatGrid[flatGrid.length - 1]);
        }
    }

    // my way to insuring that we in-situ randomly select an available black square to flip
    private int randomSquare() {
        int squareIndex = rand.nextInt(numRemainingSquares);
        int square = randomSquares[squareIndex];
        // swap the selected square with the square at *effective* end of the array
        randomSquares[squareIndex] = randomSquares[numRemainingSquares - 1];
        randomSquares[numRemainingSquares - 1] = square;
        // reduce the *effective* end of the array
        numRemainingSquares -= 1;

        return square;
    }

    // get square white neighbors
    private List<int[]> neighbors(int[] pos) {
        int x = pos[0];
        int y = pos[1];

        List<int[]> neighbors = new ArrayList<>();
        if (x - 1 >= 0 && grid[x - 1][y] == 1) {
            neighbors.add(new int[]{x - 1, y});
        }
        if (x + 1 < grid.length && grid[x + 1][y] == 1) {
            neighbors.add(new int[]{x + 1, y});
        }
        if (y - 1 >= 0 && grid[x][y - 1] == 1) {
            neighbors.add(new int[]{x, y - 1});

        }
        if (y + 1 < grid[0].length && grid[x][y + 1] == 1) {
            neighbors.add(new int[]{x, y + 1});

        }
        return neighbors;
    }

    private void printGrid() {
        for (int[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static final class Tester {
        @Test
        public void testIt() {
            //cut: class under test
            DynamicConnectivityWithUnionFind cut = new DynamicConnectivityWithUnionFind(400);
            // this yields an SVP ~= 0.59469375 (very close to the one shown by the prof. 0.592746 based on bigger simulations)
            System.out.println("connectivity ratio = " + cut.runSimulation());
//            cut.printGrid();
        }
    }
}
