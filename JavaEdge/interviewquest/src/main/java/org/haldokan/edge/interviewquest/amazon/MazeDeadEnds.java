package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * My solution to an Amazon interview question
 * <p>
 * Suppose you are given a puzzle that is represented as a matrix with 0s and 1s, where a 0 indicates you’re allowed to
 * move into that position and 1 means you’re not allowed to move in that position. Write a function that given a start
 * position and an end position, returns a boolean value indicating if there exists a path from start to end.
 * you are only allowed to move up, left, right and down. Diagonal movement is not allowed.
 * <p>
 * Example #1
 * Input
 * 0 0 1 0 1
 * 0 0 0 0 0
 * 0 1 1 1 1
 * 0 1 1 0 0
 * <p>
 * start: 4,1
 * end 0,3
 * <p>
 * Output - true
 * <p>
 * Example #2
 * Input
 * 0 0 1 1 1
 * 0 1 0 0 0
 * 1 1 1 1 1
 * 0 0 0 0 1
 * <p>
 * start: 0,0
 * end: 1,2
 * <p>
 * Output - false
 */
public class MazeDeadEnds {

    public static void main(String[] args) {
        MazeDeadEnds driver = new MazeDeadEnds();
        driver.test1();
    }

    private void test1() {
        /* 0 0 1
           0 1 0
        */
        Joint j00 = new Joint("j00", true);
        Joint j01 = new Joint("j01", true);
        Joint j02 = new Joint("j02", false);
        Joint j10 = new Joint("j10", true);
        Joint j11 = new Joint("j11", false);
        Joint j12 = new Joint("j12", true);

        j00.add(j01);
        j00.add(j10);

        j01.add(j00);
        j01.add(j11);
        j01.add(j02);

        j02.add(j01);
        j02.add(j12);

        j10.add(j00);
        j10.add(j11);

        j11.add(j10);
        j11.add(j01);

        j12.add(j11);
        j12.add(j02);

        System.out.println("connected->" + connected(j01, j10, new HashSet<>()));
        System.out.println("connected->" + connected(j10, j01, new HashSet<>()));
        System.out.println("connected->" + connected(j02, j10, new HashSet<>()));
        System.out.println("connected->" + connected(j10, j02, new HashSet<>()));
        System.out.println("connected->" + connected(j00, j12, new HashSet<>()));
        System.out.println("connected->" + connected(j12, j00, new HashSet<>()));
        System.out.println("connected->" + connected(j01, j11, new HashSet<>()));
        System.out.println("connected->" + connected(j11, j01, new HashSet<>()));

    }

    public boolean connected(Joint j1, Joint j2, Set<Joint> visited) {
        visited.add(j1);

        if (j1.equals(j2)) {
            return true;
        } else if (j1.neighbors.stream().filter(v -> !visited.contains(v)).toArray().length == 0) {
            return false;
        }
        boolean connected = false;
        for (Joint j : j1.neighbors()) {
            if (j.open && !visited.contains(j))
                connected |= connected(j, j2, visited);
        }
        return connected;
    }

    private static class Joint {
        private final String id;
        private final boolean open;
        List<Joint> neighbors = new ArrayList<>();

        public Joint(String id, boolean open) {
            this.id = id;
            this.open = open;
        }

        public void add(Joint neighbor) {
            neighbors.add(neighbor);
        }

        public List<Joint> neighbors() {
            return neighbors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Joint joint = (Joint) o;

            return !(id != null ? !id.equals(joint.id) : joint.id != null);

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
