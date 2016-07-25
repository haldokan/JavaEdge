package org.haldokan.edge.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * My implementation of an interval tree based on descriptions of the algorithm found online - interval tree enables finding
 * interval intersection in O(log n) where n is the number of intervals. It has space complexity of O(n)
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 7/25/16.
 */
public class IntervalTree<T extends Comparable<T>> {
    private Node<T> node;

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        IntervalTree<Integer> driver = new IntervalTree<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Interval<Integer> interval = new Interval<>(1000 + random.nextInt(1000), 2000 + random.nextInt(1000));
            driver.insert(interval);
        }
        driver.inOrder(driver.node);
        System.out.printf("%s%n", "----------");
        driver.preOrder(driver.node);

        List<Node<Integer>> overlapping = new ArrayList<>();
        driver.findOverlapping(driver.node, new Interval<>(1800, 1835), overlapping);
        System.out.printf("overlapping: %s%n", overlapping);

        overlapping = new ArrayList<>();
        driver.findOverlapping(driver.node, new Interval<>(11, 35), overlapping);
        System.out.printf("overlapping: %s%n", overlapping);
    }

    private void insert(Interval<T> interval) {
        if (node == null) {
            interval.max = interval.end;
            node = new Node<>(interval);
        } else {
            insert(node, interval);
        }
    }

    private Node<T> insert(Node<T> node, Interval<T> interval) {
        if (node == null) {
            return new Node<>(interval);
        }

        if (interval.start.compareTo(node.interval.start) < 0) {
            node.left = insert(node.left, interval);
        } else {
            node.right = insert(node.right, interval);
        }
        if (node.interval.max.compareTo(interval.max) < 0) {
            node.interval.max = interval.max;
        }
        return node;
    }

    private void findOverlapping(Node<T> node, Interval<T> interval, List<Node<T>> overlapping) {
        if (node == null) {
            return;
        }
        if (interval.start.compareTo(node.interval.end) <= 0 && node.interval.start.compareTo(interval.end) <= 0) {
            overlapping.add(node);
        }
        if (node.left != null && node.left.interval.max.compareTo(interval.start) >= 0) {
            findOverlapping(node.left, interval, overlapping);
        }
        findOverlapping(node.right, interval, overlapping);
    }

    private void inOrder(Node<T> node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.printf("%s%n", node);
        inOrder(node.right);
    }

    private void preOrder(Node<T> node) {
        if (node == null) {
            return;
        }
        System.out.printf("%s%n", node);
        preOrder(node.left);
        preOrder(node.right);
    }

    private static class Interval<T extends Comparable<T>> {
        private final T start, end;
        private T max;

        public Interval(T start, T end) {
            this.start = start;
            this.end = end;
            this.max = end;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ", " + max + ")";
        }
    }

    private static class Node<T extends Comparable<T>> {
        private final Interval<T> interval;
        private Node<T> left, right;

        public Node(Interval<T> interval) {
            this.interval = interval;
        }

        @Override
        public String toString() {
            return interval.toString();
        }
    }
}