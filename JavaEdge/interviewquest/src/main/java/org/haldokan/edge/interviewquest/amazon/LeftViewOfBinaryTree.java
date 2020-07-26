package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question - one liner, aint too bad!
 *
 * The Question 3.5-STAR
 *
 * AWS phone interview
 * Find the left view of binary tree
 *   1
 * /  \
 * 2  3
 * /\  \
 * 4 5  6
 * / /
 * 7 8
 * /
 * 9
 * return [1, 2, 4, 7, 9]
 */
public class LeftViewOfBinaryTree {
    public static void main(String[] args) {
        System.out.println(leftView(makeTree()));
    }

    static String leftView(Node node) {
        return node == null ? "" : String.format("%d, ", node.val).concat(leftView(node.left));
    }

    static Node makeTree() {
        Node root = new Node(0);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        root.left = n2;
        root.right = n3;
        n2.left = n4;
        n4.left = n5;
        n5.left = n6;
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
