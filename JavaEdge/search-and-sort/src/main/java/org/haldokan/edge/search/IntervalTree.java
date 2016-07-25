package org.haldokan.edge.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * My implementation of an interval tree based on descriptions of the algorithm found online
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 7/25/16.
 */
public class IntervalTree {
    private Node node;

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        IntervalTree driver = new IntervalTree();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Interval interval = new Interval(1000 + random.nextInt(1000), 2000 + random.nextInt(1000));
            driver.insert(interval);
        }
        driver.inOrder(driver.node);
        System.out.printf("%s%n", "----------");
        driver.preOrder(driver.node);

        List<Node> overlapping = new ArrayList<>();
        driver.findOverlapping(driver.node, new Interval(1800, 1835), overlapping);
        System.out.printf("overlapping: %s%n", overlapping);

        overlapping = new ArrayList<>();
        driver.findOverlapping(driver.node, new Interval(11, 35), overlapping);
        System.out.printf("overlapping: %s%n", overlapping);
    }

    private void insert(Interval interval) {
        if (node == null) {
            interval.max = interval.end;
            node = new Node(interval);
        } else {
            insert(node, interval);
        }
    }

    private Node insert(Node node, Interval interval) {
        if (node == null) {
            return new Node(interval);
        }

        if (interval.start < node.interval.start) {
            node.left = insert(node.left, interval);
        } else {
            node.right = insert(node.right, interval);
        }
        node.interval.max = Math.max(node.interval.max, interval.max);
        return node;
    }

    private void findOverlapping(Node node, Interval interval, List<Node> overlapping) {
        if (node == null) {
            return;
        }
        if (interval.start <= node.interval.end && node.interval.start <= interval.end) {
            overlapping.add(node);
        }
        if (node.left != null && node.left.interval.max >= interval.start) {
            findOverlapping(node.left, interval, overlapping);
        }
        findOverlapping(node.right, interval, overlapping);
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.printf("%s%n", node);
        inOrder(node.right);
    }

    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.printf("%s%n", node);
        preOrder(node.left);
        preOrder(node.right);
    }

    private static class Interval {
        private final int start, end;
        private int max;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
            this.max = end;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ", " + max + ")";
        }
    }

    private static class Node {
        private final Interval interval;
        private Node left, right;

        public Node(Interval interval) {
            this.interval = interval;
        }

        @Override
        public String toString() {
            return interval.toString();
        }
    }
}