package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;
/**
 * My solution to a Google interview question - piggyback on in-order traversal while maintaining O(1) space complexity
 * Runtime complexity is O(n) and I don't think it can be made better.
 * <p>
 * The Question: 4_STAR
 * Given an input BST, find the minimum value difference between any two nodes in the tree.
 * <p>
 * e.g:
 * ....10
 * 5 16
 * ........12 20
 * answer: 2 (it happens between nodes 12 and 10)
 * <p>
 * <p>
 * 08/06/20
 */
public class BSTMinNodeDifference {
    public static void main(String[] args) {
        BSTMinNodeDifference driver = new BSTMinNodeDifference();

        Node tree = driver.makeTree();
        int maxVal = driver.getMaxNode(tree).val;
        System.out.println(driver.minDiff(tree, new ArrayList<>(2), maxVal));
    }

    int minDiff(Node node, List<Integer> numbers, int minDiff) {
        if (node == null) {
            return minDiff;
        }
        minDiff(node.left, numbers, minDiff);

        if (numbers.size() == 2) {
            numbers.set(0, numbers.get(1));
            numbers.set(1, node.val);
        } else {
            numbers.add(node.val);
        }

        int newMinDiff = numbers.size() == 2 ? numbers.get(1) - numbers.get(0) : minDiff;
        return Math.min(newMinDiff, minDiff(node.right, numbers, Math.min(minDiff, newMinDiff)));
    }

    private Node getMaxNode(Node tree) {
        if (tree == null) {
            return null;
        }
        if (tree.right == null) {
            return tree;
        }
        return getMaxNode(tree.right);

    }

    private Node makeTree() {
        Node root = new Node(8);
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
