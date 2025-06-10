package org.haldokan.edge.interviewquest.amazon;

import org.haldokan.edge.interviewquest.google.BSTMinNodeDifference;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * My solution to an Amazon interview question - Used BFS. With DFS I am not sure how to keep track of distance
 * <p>
 * The Question: 3.5-STAR
 * <p>
 * Given a binary tree, find the closest LEAF node to the target.
 * 07/22/20 - touched 06/08/25
 */
public class BinaryTreeClosestLeafToTarget {
    public static void main(String[] args) {
        Node root = makeTree();
        Node found = find1(root, 1);
        System.out.println(found);

//        Node target = findTarget(root, 1);
//        Node found2 = find2(target, 0).node();
//        System.out.println(found2);
    }

    private static Node find1(Node root, int target) {
        Deque<Node> deck = new ArrayDeque<>();
        var targetNode = findTarget(root, target);

        deck.add(targetNode);
        Map<Node, Integer> map = new HashMap<>();
        while (!deck.isEmpty()) {
            Node current = deck.remove(); // Same as removeFirst()
            if (current.left == null && current.right == null) {
                return current;
            }
            if (current.left != null) {
                deck.add(current.left);
            }
            if (current.right != null) {
                deck.add(current.right);
            }
        }
        return null;
    }

    // Suggested by AI (AI also rightly notes the BFS is better than DFS)
    private static NodeWrap find2(Node node, int depth) {
        if (node == null) {
            return new NodeWrap(null, Integer.MAX_VALUE);
        }
        if (node.left == null && node.right == null) {
            System.out.println("leaf: " + node);
            return new NodeWrap(node, depth);
        }
        System.out.println("node: " + node);
        var foundLeft = find2(node.left, depth + 1);
        var foundRight = find2(node.right, depth + 1);
        System.out.println(foundLeft);
        System.out.println(foundRight);
        return foundLeft.depth <= foundRight.depth ? foundLeft : foundRight;
    }

    private static Node findTarget(Node node, int target) {
        if (node == null) {
            return null;
        }
        if (node.val == target) {
            return node;
        }
        var found = findTarget(node.left, target);
        if (found != null) {
            return found;
        }
        return findTarget(node.right, target);
    }

    static private Node makeTree() {
        //          1
        //         / \
        //        2   3
        //       / \    \
        //      5   4    6
        //               /
        //              7
        Node root = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        return root;
    }

    private record NodeWrap(Node node, int depth) {}

    private static class Node {
        private final int val;
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
