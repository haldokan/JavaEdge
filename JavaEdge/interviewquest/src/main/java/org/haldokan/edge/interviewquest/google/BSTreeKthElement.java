package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * My solution to a Google interview question - to identify processed nodes I used an IdentityHashMap which is suitable for such
 * purpose (read docs). I wonder if it would be possible to solve this problem iteratively w/o using the 'processed' map
 * <p>
 * The Question: 3_STAR
 * You are given a binary search tree and a positive integer K. Return the K-th element of the tree.
 * No pre-processing or modifying of the tree is allowed.
 * Created by haytham.aldokanji on 9/30/15.
 */
public class BSTreeKthElement {
    public static void main(String[] args) {
        Node n1 = new Node(7);
        Node n2 = new Node(4);
        Node n3 = new Node(9);
        Node n4 = new Node(6);
        Node n5 = new Node(1);
        Node n6 = new Node(12);
        Node n7 = new Node(10);
        n1.left = n2;
        n1.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        BSTreeKthElement driver = new BSTreeKthElement();
        System.out.println(driver.getNode(n1, 0));
        System.out.println(driver.getNode(n1, 1));
        System.out.println(driver.getNode(n1, 2));
        System.out.println(driver.getNode(n1, 3));
        System.out.println(driver.getNode(n1, 4));
        System.out.println(driver.getNode(n1, 5));
        System.out.println(driver.getNode(n1, 6));
    }

    public Node getNode(Node n, int k) {
        Map<Node, Boolean> processed = new IdentityHashMap<>();

        if (n == null || k < 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        int counter = k;
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(n);
        Node current = null;
        while (!stack.isEmpty()) {
            current = stack.peekFirst();
            if (current.left == null || processed.containsKey(current.left)) {
                processed.put(stack.pop(), true);
                if (counter == 0)
                    break;
                if (current.right != null && !processed.containsKey(current.right)) {
                    stack.push(current.right);
                }
                counter--;
            } else if (processed.get(current.left) == null) {
                stack.push(current.left);
            }
        }
        return current;
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
