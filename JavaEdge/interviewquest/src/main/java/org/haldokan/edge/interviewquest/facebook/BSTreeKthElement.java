package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - Look at org.haldokan.edge.interviewquest.google.BSTreeKthElement for
 * another solution using iteration instead of recursion
 * <p>
 * Find the k'th largest element in a binary search tree. Write code for
 * struct Node {
 * int val;
 * struct Node *left;
 * struct Node *right;
 * } Node;
 * <p>
 * Node * kth_largest(Node *root, unsigned int k);
 * Created by haytham.aldokanji on 4/28/16.
 */
public class BSTreeKthElement {

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
        n6.left = n7;

        BSTreeKthElement driver = new BSTreeKthElement();
        assertThat(driver.kthElement(root, 1), is(n5));
        assertThat(driver.kthElement(root, 2), is(n2));
        assertThat(driver.kthElement(root, 3), is(n4));
        assertThat(driver.kthElement(root, 4), is(root));
        assertThat(driver.kthElement(root, 5), is(n3));
        assertThat(driver.kthElement(root, 6), is(n7));
        assertThat(driver.kthElement(root, 7), is(n6));
    }


    public Node kthElement(Node node, int k) {
        return doKthElement(node, new int[]{k}, new Node[1]);
    }

    private Node doKthElement(Node node, int[] k, Node[] arr) {
        if (node == null) {
            return null;
        }

        doKthElement(node.left, k, arr);
        k[0] -= 1;
        if (k[0] >= 0) {
            arr[0] = node;
            doKthElement(node.right, k, arr);
        }
        return arr[0];
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
            return "{" + String.valueOf(val) + "}";
        }
    }
}
