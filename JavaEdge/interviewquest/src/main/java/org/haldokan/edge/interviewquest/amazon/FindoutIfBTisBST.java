package org.haldokan.edge.interviewquest.amazon;

/**
 * My colleague's solution to an Amazon interview question
 * <p>
 * Implement a function that checks if the given binary tree is binary search tree(BST).
 * Use tree operations to solve this. do not try solving by pre-order traversal of the tree and then checking
 * if the array is sorted.
 * instead, traverse the tree for checking if it is BST.
 */
public class FindoutIfBTisBST {
    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
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

        System.out.println(isBST(root));
    }

    public static void test2() {
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

        System.out.println(isBST(root));
    }

    private static boolean isBST(Node tree) {
        if (tree == null) {
            return true;
        }
        if (tree.left != null && tree.left.val > tree.val) {
            return false;
        }
        if (tree.right != null && tree.right.val < tree.val) {
            return false;
        }
        return isBST(tree.left) && isBST(tree.right);
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
