package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * My solution to an Amazon interview question - done using iteration + recursion w/o extra space or auxiliary data structures,
 * and it supports non-rectangular mazes
 * <p>
 * The Question: 5_STAR
 * Suppose you are given a puzzle that is represented as a matrix with 0s and 1s, where a 0 indicates you’re allowed to
 * move into that position and 1 means you’re not allowed to move in that position. Write a function that given a start
 * position and an end position, returns a boolean value indicating if there exists a path from start to end.
 * you are only allowed to move up, left, right and down. Diagonal movement is not allowed.
 * <p>
 * Example
 * Input
 * 0 0 1 1 1
 * 0 1 0 0 0
 * 0 1 1 1 0
 * 0 0 0 0 1
 * <p>
 * start: 0,0
 * end: 1,2
 * Output - false
 * <p>
 * start: 3,3
 * end: 0,1
 * Output - true
 */
public class MazeJointsConnectedness {

    public static void main(String[] args) {
        MazeJointsConnectedness driver = new MazeJointsConnectedness();
        driver.test1();
        driver.test2();
        driver.test3();
    }

    private void test1() {
        int[][] maze = new int[][]{
                {0, 0, 1, 1, 1},
                {0, 1, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 1}
        };
        System.out.println("false->" + connected(maze, new int[]{0, 0}, new int[]{1, 2}));
        resetMaze(maze);
        System.out.println("true->" + connected(maze, new int[]{3, 3}, new int[]{0, 1}));
    }

    // maze doesn't have to be rectangular
    private void test2() {
        int[][] maze = new int[][]{
                {0, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 0},
                {0, 0, 0, 1}
        };
        System.out.println("true->" + connected(maze, new int[]{0, 0}, new int[]{1, 2}));
        resetMaze(maze);
        System.out.println("true->" + connected(maze, new int[]{3, 3}, new int[]{0, 1}));
        resetMaze(maze);
        System.out.println("false->" + connected(maze, new int[]{3, 3}, new int[]{0, 3}));
    }

    private void test3() {
        int[][] maze = new int[][]{
            {0, 0, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 0, 1},
            {0, 0, 0, 0, 0}
        };
        System.out.println("true->" + connected(maze, new int[]{0, 4}, new int[]{0, 1}));
    }

    public List<int[]> getNeighbors(int[][] maze, int[] joint) {
        List<int[]> neighbors = new ArrayList<>();

        int row = joint[0];
        int col = joint[1];

        // maze does not have to be rectangular
        int numColsForRow = maze[row].length;
        int numRows = maze.length;
        // check for previous row len is to support non-rectangular maze
        if (row > 0 && maze[row - 1].length > col) {
            neighbors.add(new int[]{row - 1, col});
        }
        // check for next row len is to support non-rectangular maze
        if (row < numRows - 1 && maze[row + 1].length > col) {
            neighbors.add(new int[]{row + 1, col});
        }
        if (col > 0) {
            neighbors.add(new int[]{row, col - 1});
        }
        if (col < numColsForRow - 1) {
            neighbors.add(new int[]{row, col + 1});
        }
        return neighbors;
    }

    public boolean connected(int[][] maze, int[] joint1, int[] joint2) {
//        System.out.println(joint1[0] + "," + joint1[1]);
        int row = joint1[0];
        int col = joint1[1];
        // mark joint1 as visited. If the cell contains 1 leave it as is but if it contains 0 change it to 2;
        maze[row][col] = (maze[row][col] ^ 0X1) + 1;

        List<int[]> joint1Neighbors = getNeighbors(maze, joint1);

        if (Arrays.equals(joint1, joint2)) {
            return true;
        } else if (joint1Neighbors.stream().filter(j -> maze[j[0]][j[1]] == 0).toArray().length == 0) {
            return false;
        }
        boolean connected = false;
        for (int[] j : joint1Neighbors) {
            if (maze[j[0]][j[1]] == 0) {
                connected |= connected(maze, j, joint2);
            }
        }
        return connected;
    }

    private void resetMaze(int[][] maze) {
        Arrays.stream(maze).forEach(arr -> {
            for (int i = 0; i < arr.length; i++) {
                // if cell is 0 or 1 leave as is, but if it is 2 make it 0;
                arr[i] = arr[i] & 0X1;
            }
        });
    }
}
