package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * My solution to an Amazon interview question. Look at the other solution I provide using recursion but with adding 'parent'
 * to the node definition.
 * <p>
 * In a binary tree, find and print the path with smallest weight.
 * <p>
 * Criteria: the tree contains integer values in the nodes. It may not be balanced tree. Weight is calculated by sum
 * of values in the nodes in that path. Write code that returns the path as well as the minweight.
 */
public class BTMinWeightPathWithIteration {

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

        System.out.println(minWeightPath(root));
    }

    public static Deque<Node> minWeightPath(Node tree) {
        Deque<Node> nodes = new ArrayDeque<>();
        Map<Node, Node> parents = new HashMap<>();

        nodes.add(tree);
        Deque<Node> minPath = null;

        while (!nodes.isEmpty()) {
            Node node = nodes.removeFirst();
            Node left = node.left;
            Node right = node.right;

            if (left != null) {
                nodes.add(left);
                parents.put(left, node);
            }
            if (right != null) {
                nodes.add(right);
                parents.put(right, node);
            }
            if (node.leaf()) {
                Deque<Node> path = path(node, parents);
                if (minPath == null || pathWeight(path) < pathWeight(minPath)) {
                    minPath = path;
                }
//                System.out.println(path);
            }
        }
        return minPath;
    }

    private static Deque<Node> path(Node leaf, Map<Node, Node> parents) {
        Deque<Node> path = new ArrayDeque<>();

        Node node = leaf;
        while (node != null) {
            path.push(node);
            node = parents.get(node);
        }
        return path;
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
