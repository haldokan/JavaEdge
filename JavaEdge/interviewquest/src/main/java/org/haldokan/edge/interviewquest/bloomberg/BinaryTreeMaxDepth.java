package org.haldokan.edge.interviewquest.bloomberg;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question - BFS while keeping track of level height. Time and space is O(n) where
 * n is the number of nodes in the tree. I also include a much more elegant recursive solution I found on line.
 * <p>
 * The Question: 3_STAR
 * <p>
 * Write a maxDepth function to find maximum depth of a binary tree. What is the time and space complexity of your function.
 * class Tree
 * {
 * public Tree left;
 * public Tree right;
 * }
 * <p>
 * int maxDepth(Tree head) {}
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class BinaryTreeMaxDepth {

    public static void main(String[] args) {
        BinaryTreeMaxDepth driver = new BinaryTreeMaxDepth();
        assertThat(driver.treeMaxHeightBFS(driver.makeTree()), is(6));
        assertThat(driver.treeMaxHeightDFS(driver.makeTree()), is(6));
    }

    public int treeMaxHeightBFS(Node tree) {
        Map<Integer, Integer> nodeHeight = new HashMap<>();
        Deque<Node> evalDeck = new ArrayDeque<>();

        evalDeck.add(tree);
        int maxHeight = 1;
        nodeHeight.put(tree.id, maxHeight);

        while (!evalDeck.isEmpty()) {
            Node parent = evalDeck.remove();
            int parentHeight = nodeHeight.get(parent.id);

            if (parentHeight > maxHeight) {
                maxHeight = parentHeight;
            }

            Node[] children = new Node[]{parent.left, parent.right};

            for (Node child : children) {
                if (child != null) {
                    evalDeck.add(child);
                    int childHeight = parentHeight + 1;
                    nodeHeight.put(child.id, childHeight);

                    if (childHeight > maxHeight) {
                        maxHeight = childHeight;
                    }
                }
            }
        }
        return maxHeight;
    }

    // Elegant! I should've thought of it b4 I went to the more clunky solution of BFS
    // is this actually DFS?
    public int treeMaxHeightDFS(Node head) {
        return head == null ? 0 : Math.max(treeMaxHeightDFS(head.left), treeMaxHeightDFS(head.right)) + 1;
    }

    // yet another variation that's easier to understand than the one above
    public int maxDepth(Node tree, int depth) {
        if (tree == null) {
            return depth;
        }
        return Math.max(maxDepth(tree.left, depth + 1), maxDepth(tree.right, depth + 1));
    }

    private Node makeTree() {
        /**
         *                7
         *          4           9
         *       1    6     10      12
         *          2   3      5
         *                  11
         *                     13
         */
        Node n7 = new Node(7);
        Node n4 = new Node(4);
        Node n9 = new Node(9);
        Node n6 = new Node(6);
        Node n1 = new Node(1);
        Node n12 = new Node(12);
        Node n10 = new Node(10);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n5 = new Node(5);
        Node n11 = new Node(11);
        Node n13 = new Node(13);

        n7.left = n4;
        n7.right = n9;
        n4.right = n6;
        n4.left = n1;
        n9.right = n12;
        n9.left = n10;
        n6.left = n2;
        n6.right = n3;
        n10.right = n5;
        n5.left = n11;
        n11.right = n13;

        return n7;
    }

    private static class Node {
        private int id;
        private Node left;
        private Node right;

        public Node(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
