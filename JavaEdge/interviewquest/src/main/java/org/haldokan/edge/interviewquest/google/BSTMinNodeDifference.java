package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - a bit lame since while doing the inorder traversal I maintain the
 * previous node in an array of size 1 to avoid null values for 'previous' node when the recursive calls are popped off
 * the call stack. There must be a better solution
 * <p>
 * The Question: 3_STAR
 * Given an input BST, find the minimum value difference between any two nodes in the tree.
 * <p>
 * e.g:
 * ....10
 * 5 16
 * ........12 20
 * answer: 2 (it happens between nodes 12 and 10)
 * <p>
 * describe the test cases you would use here?
 * <p>
 * Created by haytham.aldokanji on 5/15/16.
 */
public class BSTMinNodeDifference {
    public static void main(String[] args) {
        BSTMinNodeDifference driver = new BSTMinNodeDifference();

        Node tree = driver.makeTree();
        // min difference must be less than the max value in the bst (better than using Integer.MAX_VALUE)
        int maxVal = driver.getMaxNode(tree).val;
        int minDiff = driver.minDiff(tree, new Node[1], maxVal);
        System.out.println(minDiff);
        assertThat(minDiff, is(1));
    }

    public int minDiff(Node tree, Node[] previousNode, int minDiff) {
        if (tree == null) {
            return 0;
        }
        minDiff(tree.left, previousNode, minDiff);

        int updatedMinDif = minDiff;
        if (previousNode[0] != null) {
            int diff = Math.abs(tree.val - previousNode[0].val);
            if (diff < updatedMinDif) {
                updatedMinDif = diff;
            }
        }
        previousNode[0] = tree;
        minDiff(tree.right, previousNode, updatedMinDif);
        return updatedMinDif;
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
