package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 * <p>
 * In a binary tree, find and print the path with smallest weight.
 * <p>
 * Criteria: the tree contains integer values in the nodes. It may not be balanced tree. Weight is calculated by sum
 * of values in the nodes in that path. Write code that returns the path as well as the minweight.
 */
public class BTMinWeightPath {

    public Node minWeightPath(Node tree) {
        if (tree == null) {
            throw new IllegalArgumentException("tree is nul");
        }

        Node minPath = new Node(0);
        doMinWeightPath(tree, minPath);

        return minPath;
    }

    private void doMinWeightPath(Node tree, Node minPath) {
        if (tree == null) {
            return;
        }
        doMinWeightPath(tree.left, minPath);

        doMinWeightPath(tree.right, minPath);
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
