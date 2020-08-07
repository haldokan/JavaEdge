package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - could not make it work passing k by value so I passed by reference. I think
 * there is no way to solve this question passing k by value. I leveraged in-order reverse traversal (node.right then node.left)
 *
 * Look at org.haldokan.edge.interviewquest.google.BSTreeKthElement for another solution using iteration instead of recursion
 *
 * The Question: 3.5_STAR
 *
 * Find the kth largest element in a binary search tree. Write code for
 * struct Node {
 * int val;
 * struct Node *left;
 * struct Node *right;
 * } Node;
 * <p>
 * Node * kth_largest(Node *root, unsigned int k);
 *
 * 08/06/20
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
        assertThat(driver.kthElement(root, new int[]{1}), is(n6));
        assertThat(driver.kthElement(root, new int[]{2}), is(n7));
        assertThat(driver.kthElement(root, new int[]{3}), is(n3));
        assertThat(driver.kthElement(root, new int[]{4}), is(root));
        assertThat(driver.kthElement(root, new int[]{5}), is(n4));
        assertThat(driver.kthElement(root, new int[]{6}), is(n2));
        assertThat(driver.kthElement(root, new int[]{7}), is(n5));
    }

    Node kthElement(Node node, int[] k) {
        if (node == null) {
            return null;
        }
        Node node1 = kthElement(node.right, k);
        if (node1 != null) return node1; // so it returns w/o traversing right as recursion calls return

        k[0] = k[0] - 1;
        System.out.printf("%s, k: %d%n", node, k[0]);
        return k[0] == 0 ? node : kthElement(node.left, k);
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
            return String.format("node->%d", val);
        }
    }
}
