package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Google interview question - leverage in order traversal to build an array of length 2 that at any time
 * holds the last 2 sorted elements. Quit recursion once the node is found. Runtime is O(n) and space O(1).
 *
 *  Note: examine my other solution NextLargestNumberInBST2 which is more elegant and runs in log(n)
 *
 * The Question: 4-STAR
 *
 * For a given node in a binary search tree find the next largest number in search tree.
 */
public class NextLargestNumberInBST {
    public static void main(String[] args) {
        NextLargestNumberInBST driver = new NextLargestNumberInBST();
        driver.test();
    }

    void test() {
        int[] values = new int[]{7, 4, 9, 6, 1, 12, 10};
        for (int value : values) {
            List<Integer> numbers = new ArrayList<>(2);
            int nextLargest = inOrder(makeTree(), value, numbers);
            System.out.printf("%d -> %d%n", value, nextLargest);
        }
    }

    Integer inOrder(Node node, int val, List<Integer> numbers) {
        if (node == null) {
            return null;
        }
        inOrder(node.left, val, numbers);
        if (node.val <= val) {
            if (numbers.size() < 2) {
                numbers.add(node.val);
            } else {
                numbers.set(0, numbers.get(1));
                numbers.set(1, node.val);
            }
        }
        // no more work after finding the value
        if (numbers.get(numbers.size() - 1) != val) {
            inOrder(node.right, val, numbers);
        }
        return numbers.get(Math.max(0, numbers.size() - 2));
    }

    private Node makeTree() {
        Node root = new Node(7);
        Node n2 = new Node(4);
        Node n3 = new Node(9);
        Node n4 = new Node(6);
        Node n5 = new Node(1);
        Node n6 = new Node(12);
        Node n7 = new Node(10);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        return root;
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}
