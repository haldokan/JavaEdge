package org.haldokan.edge.interviewquest.google;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 * Given a BST with unique values find in a given tree a value closest to a given value X.
 *
 * @author haldokan
 */
public class ClosestValueInBST {

    public static void main(String[] args) {
        Node<Integer> root = new Node<>(8);
        Node<Integer> n2 = new Node<>(4);
        Node<Integer> n3 = new Node<>(11);
        Node<Integer> n4 = new Node<>(6);
        Node<Integer> n5 = new Node<>(1);
        Node<Integer> n6 = new Node<>(16);
        Node<Integer> n7 = new Node<>(13);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        ClosestValueInBST driver = new ClosestValueInBST();
        System.out.println(driver.closest(root, 2));
        System.out.println(driver.closest(root, 5));
        System.out.println(driver.closest(root, 3));
        System.out.println(driver.closest(root, 0));

        System.out.println(driver.closest(root, 7));
        System.out.println(driver.closest(root, 12));
        System.out.println(driver.closest(root, 15));
        System.out.println(driver.closest(root, 20));
    }

    public int closest(Node<Integer> root, int val) {
        if (root == null)
            throw new NullPointerException("Null input");
        return doClosest(root, val, root.value);
    }

    private int doClosest(Node<Integer> node, int val, int closest) {
        if (node == null)
            return closest;
        int diff = Math.abs(node.value - val);
        int closestCandidate = closest;
        if (diff <= Math.abs(val - closest))
            closestCandidate = node.value;
        if (val <= node.value)
            return doClosest(node.left, val, closestCandidate);
        else
            return doClosest(node.right, val, closestCandidate);
    }

    private static class Node<E extends Comparable<E>> {
        private final E value;
        private Node<E> left;
        private Node<E> right;

        public Node(E v) {
            this.value = v;
        }

        @Override
        public String toString() {
            return "(" + value + ")";
        }
    }
}
