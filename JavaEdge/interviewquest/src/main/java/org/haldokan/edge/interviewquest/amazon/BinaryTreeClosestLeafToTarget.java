package org.haldokan.edge.interviewquest.amazon;

import org.haldokan.edge.interviewquest.google.BSTMinNodeDifference;

/**
 * My solution to an Amazon interview question - This is really easy: do a BFS starting with the target node and the
 * first node w/o children is the closet node to target. I provide the code for finding the target node using recursion
 * since it is more interesting
 *
 * The Question: 3.5-STAR
 *
 * Given a binary tree, find the closest LEAF node to the target.
 * 07/22/20
 */
public class BinaryTreeClosestLeafToTarget {
    public static void main(String[] args) {
        Node root = makeTree();
        Node target = find(root, 9);
        System.out.println(target);
    }

    static Node find(Node node, int val) {
        if (node == null) {
            return null;
        }

        if (node.val == val) {
            return node;
        }

        Node node1 = find(node.left, val);
        if (node1 != null && node1.val == val) {
            return node1;
        }
        return find(node.right, val);
    }

    static private Node makeTree() {
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
