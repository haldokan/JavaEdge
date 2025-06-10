package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - find the 2 nodes and their parents using recursion then swap them
 * The Question: 4_STAR
 * <p>
 * Swap 2 nodes in a Binary tree.(May or may not be a BST)
 * Swap the nodes NOT just their values.
 * ex:
 * 5
 * / \
 * 3  7
 * /\  \
 * 2 4  6
 * <p>
 * swap 7 and 3
 * <p>
 * 5
 * / \
 * 7   3
 * /  / \
 * 6  2 4
 * Created by haytham.aldokanji on 6/27/16.
 */
public class SwapNodesInBinTree {
    public static void main(String[] args) {
        SwapNodesInBinTree driver = new SwapNodesInBinTree();

        driver.testFindNode();
        driver.testSwap1();
        driver.testSwap2();
        driver.testSwap3();
    }

    public void swap(Node tree, int node1Val, int node2Val) {
        Node[] nodeAndParent1 = find(tree, tree, node1Val);
        Node[] nodeAndParent2 = find(tree, tree, node2Val);

        if (nodeAndParent1 != null && nodeAndParent2 != null) {
            Node node1 = nodeAndParent1[0];
            Node parent1 = nodeAndParent1[1];

            Node node2 = nodeAndParent2[0];
            Node parent2 = nodeAndParent2[1];

            if (parent1.equals(parent2)) {
                if (node1 == parent1.left) {
                    parent1.left = node2;
                    parent1.right = node1;
                } else {
                    parent1.left = node1;
                    parent1.right = node2;
                }
            } else {
                if (node1 == parent1.left) {
                    parent1.left = node2;
                } else {
                    parent1.right = node2;
                }
                if (node2 == parent2.left) {
                    parent2.left = node1;
                } else {
                    parent2.right = node1;
                }
            }
        }
    }

    private Node[] find(Node tree, Node parent, int val) {
        if (tree == null) {
            return null;
        }
        Node[] nodeAndParent;
        if (tree.id == val) {
            return new Node[]{tree, parent};
        }
        nodeAndParent = find(tree.left, tree, val);
        if (nodeAndParent == null) {
            nodeAndParent = find(tree.right, tree, val);
        }
        return nodeAndParent;
    }

    private Node makeTree() {
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

        n7.left = n4;
        n7.right = n9;
        n4.right = n6;
        n4.left = n1;
        n9.right = n12;
        n9.left = n10;
        n6.left = n2;
        n6.right = n3;
        n10.right = n5;

        return n7;
    }

    private void testFindNode() {
        Node tree = makeTree();
        Node[] nodeAndParent = find(tree, tree, 1);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(1));
        assertThat(nodeAndParent[1].id, is(4));

        nodeAndParent = find(tree, tree, 4);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(4));
        assertThat(nodeAndParent[1].id, is(7));

        nodeAndParent = find(tree, tree, 9);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(9));
        assertThat(nodeAndParent[1].id, is(7));

        nodeAndParent = find(tree, tree, 10);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(10));
        assertThat(nodeAndParent[1].id, is(9));

        nodeAndParent = find(tree, tree, 2);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(2));
        assertThat(nodeAndParent[1].id, is(6));

        nodeAndParent = find(tree, tree, 7);
        System.out.printf("%s%n", Arrays.toString(nodeAndParent));

        assertThat(nodeAndParent[0].id, is(7));
        assertThat(nodeAndParent[1].id, is(7));
    }

    private void testSwap1() {
        Node tree = makeTree();
        assertThat(tree.left.id, is(4));
        assertThat(tree.right.id, is(9));

        swap(tree, 4, 9);
        assertThat(tree.left.id, is(9));
        assertThat(tree.right.id, is(4));
    }

    private void testSwap2() {
        Node tree = makeTree();

        Node[] nodeAndParent1 = find(tree, tree, 6);
        assertThat(nodeAndParent1[1].id, is(4));

        Node[] nodeAndParent2 = find(tree, tree, 10);
        assertThat(nodeAndParent2[1].id, is(9));

        swap(tree, 6, 10);

        nodeAndParent1 = find(tree, tree, 6);
        assertThat(nodeAndParent1[1].id, is(9));

        nodeAndParent2 = find(tree, tree, 10);
        assertThat(nodeAndParent2[1].id, is(4));
    }

    private void testSwap3() {
        Node tree = makeTree();

        Node[] nodeAndParent1 = find(tree, tree, 5);
        assertThat(nodeAndParent1[1].id, is(10));

        Node[] nodeAndParent2 = find(tree, tree, 4);
        assertThat(nodeAndParent2[1].id, is(7));

        swap(tree, 5, 4);

        nodeAndParent1 = find(tree, tree, 5);
        assertThat(nodeAndParent1[1].id, is(7));

        nodeAndParent2 = find(tree, tree, 4);
        assertThat(nodeAndParent2[1].id, is(10));
    }

    private static class Node {
        private final int id;
        private Node left, right;

        public Node(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
