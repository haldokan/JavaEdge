package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * My solution to an Amazon interview question. Look at the other solution I provide using iteration and w/o the need
 * to have 'parent' in the node definition (BTMinWeightPathWithIteration).
 * The Question: 3_STAR
 * In a binary tree, find and print the path with smallest weight.
 * <p>
 * Criteria: the tree contains integer values in the nodes. It may not be balanced tree. Weight is calculated by sum
 * of values in the nodes in that path. Write code that returns the path as well as the minimum weight.
 */
public class BTMinWeightPathWithRecursion {

    public static void main(String[] args) {
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
        n3.left = n7;

        n7.parent = n3;
        n6.parent = n3;
        n5.parent = n2;
        n4.parent = n2;
        n3.parent = root;
        n2.parent = root;
        root.parent = null;

        System.out.println(minWeightPath(root, new ArrayDeque<>()));
    }

    public static Deque<Node> minWeightPath(Node tree, Deque<Node> path) {
        if (tree == null) {
            return path;
        }

        minWeightPath(tree.left, path);
        minWeightPath(tree.right, path);

        if (tree.leaf()) {
            Deque<Node> currPath = buildPath(tree);
            if (path.isEmpty() || pathWeight(currPath) < pathWeight(path)) {
                path.clear();
                path.addAll(currPath);
            }
        }
        return path;
    }

    private static Deque<Node> buildPath(Node node) {
        Deque<Node> deck = new ArrayDeque<>();

        Node currNode = node;
        while (currNode != null) {
            deck.push(currNode);
            currNode = currNode.parent;
        }
        return deck;
    }

    private static int pathWeight(Deque<Node> path) {
        int weight = 0;
        for (Node n : path) {
            weight += n.val;
        }
        return weight;
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;
        private Node parent;

        public Node(int val) {
            this.val = val;
        }

        public boolean leaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}
