package org.haldokan.edge.interviewquest.facebook;

/**
 * @PENDING it can be solved with iteration but trying to find a solution using recursion
 * <p>
 * Find the k'th largest element in a binary search tree. Write code for
 * <p>
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
//        System.out.println(driver.kthElement(n1, 0));
//        System.out.println(driver.kthElement(n1, 1));
//        System.out.println(driver.kthElement(n1, 2));
//        System.out.println(driver.kthElement(n1, 3));
//        System.out.println(driver.kthElement(n1, 4));
//        System.out.println(driver.kthElement(n1, 5));
        System.out.println(driver.kthElement(n1, 6));
    }


    public Node kthElement(Node node, int k) {
        return doKthElement(node, k, 0);
    }

    private Node doKthElement(Node node, int k, int count) {
        return null;
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
