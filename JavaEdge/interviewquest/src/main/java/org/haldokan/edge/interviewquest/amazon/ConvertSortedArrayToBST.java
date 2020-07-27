package org.haldokan.edge.interviewquest.amazon;

/**
 * Someone's solution to an Amazon interview question - very smart and makes me feel like shit because I did not figure it out
 *
 * The Question: 5-STAR
 *
 * convert a sorted array to balanced BST
 */
public class ConvertSortedArrayToBST {
    public static void main(String[] args) {
        int[] arr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Node root = convert(arr, 0, arr.length);
        inOrder(root);
    }

    static Node convert(int[] arr, int start, int end) {
        if(start >= end) {
            return null;
        }
        int mid = (start + end) / 2;
        Node newNode = new Node(arr[mid]);
        newNode.left = convert(arr, start, mid);
        newNode.right = convert(arr, mid + 1, end);
        return newNode;
    }

    static void inOrder(Node tree) {
        if (tree == null) {
            return;
        }
        inOrder(tree.left);
        System.out.println(tree.val);
        inOrder(tree.right);
    }

    static class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.format("n->%d%n", val);
        }
    }
}
