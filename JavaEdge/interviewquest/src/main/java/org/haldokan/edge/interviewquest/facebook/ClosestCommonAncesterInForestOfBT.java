package org.haldokan.edge.interviewquest.facebook;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I made the question harder by requiring that the nodes be found first
 * based on their ids (while the question method signature suggests that pointers to the nodes are available)
 * The Question: 4_STAR
 * Find the closest common ancestor in a tree forest.
 * <p>
 * class Node {
 * Node* parent; // == null for root of tree
 * Node* left;
 * Node* right;
 * }
 * <p>
 * Node* tree_forest[]; // array of pointers which points to roots of each tree respectively
 * <p>
 * Node* closest_common_ancestor(Node* n1, Node* n2) {
 * // your solution
 * }
 * Example:
 * <p>
 * |    a     |   j
 * |   / \    |  /
 * |  b   c   | h
 * | /   / \  |
 * |d   e   f |
 * for e and d CCA is a
 * for e and f CCA is c
 * for e and c CCA is c
 * for h and d CCA is null
 * <p>
 * Constrains: O(1) additional memory
 * <p>
 * Created by haytham.aldokanji on 5/13/16.
 */
public class ClosestCommonAncesterInForestOfBT {

    public static void main(String[] args) {
        ClosestCommonAncesterInForestOfBT driver = new ClosestCommonAncesterInForestOfBT();

        driver.testFindNode();
        driver.testCommonAncesterInTree();
        driver.testCommonAncesterInForest();
    }

    public Optional<Node> findCommonAncester(Node[] forest, int val1, int val2) {
        for (Node tree : forest) {
            Node ancester = findCommonAncester(tree, val1, val2);
            if (ancester != null) {
                return Optional.of(ancester);
            }
        }
        return Optional.empty();
    }

    private Node findCommonAncester(Node tree, int val1, int val2) {
        Node node1 = findNode(tree, val1);
        Node node2 = findNode(tree, val2);

        if (node1 == null || node2 == null) {
            return null;
        }

        while (node1 != null) {
            Node node = findNode(node1, val2);
            if (node != null) {
                return node1;
            }
            node1 = node1.parent;
        }
        return null;
    }

    private Node findNode(Node tree, int val) {
        if (tree == null) {
            return null;
        }
        if (tree.val == val) {
            return tree;
        }
        Node node = findNode(tree.left, val);
        if (node == null) {
            node = findNode(tree.right, val);
        }
        return node;
    }

    private Node makeTree1() {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        Node n8 = new Node(8);

        n1.left = n2;
        n1.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n3.left = n7;
        n7.left = n8;

        n7.parent = n3;
        n6.parent = n3;
        n5.parent = n2;
        n4.parent = n2;
        n3.parent = n1;
        n2.parent = n1;
        n8.parent = n7;
        n1.parent = null;

        return n1;
    }

    private Node makeTree2() {
        Node n11 = new Node(11);
        Node n12 = new Node(12);
        Node n13 = new Node(13);

        n11.left = n12;
        n11.right = n13;

        n13.parent = n11;
        n12.parent = n11;
        n11.parent = null;

        return n11;
    }

    private void testFindNode() {
        Node tree = makeTree1();
        for (int i = 1; i <= 7; i++) {
            assertThat(findNode(tree, i).val, is(i));
        }
    }

    private void testCommonAncesterInTree() {
        Node tree = makeTree1();
        assertThat(findCommonAncester(tree, 6, 7).val, is(3));
        assertThat(findCommonAncester(tree, 2, 3).val, is(1));
        assertThat(findCommonAncester(tree, 4, 5).val, is(2));
        assertThat(findCommonAncester(tree, 6, 4).val, is(1));
        assertThat(findCommonAncester(tree, 6, 2).val, is(1));
        assertThat(findCommonAncester(tree, 6, 8).val, is(3));
        assertThat(findCommonAncester(tree, 777, 333), nullValue());
    }

    private void testCommonAncesterInForest() {
        Node tree1 = makeTree1();
        Node tree2 = makeTree2();
        Node[] forest = new Node[]{tree1, tree2};

        assertThat(findCommonAncester(forest, 6, 7).get().val, is(3));
        assertThat(findCommonAncester(forest, 2, 3).get().val, is(1));

        assertThat(findCommonAncester(forest, 12, 13).get().val, is(11));

        assertThat(findCommonAncester(forest, 6, 12), is(Optional.empty()));
        assertThat(findCommonAncester(forest, 3, 13), is(Optional.empty()));
        assertThat(findCommonAncester(forest, 2, 12), is(Optional.empty()));
        assertThat(findCommonAncester(forest, 7, 13), is(Optional.empty()));
        assertThat(findCommonAncester(forest, 777, 333), is(Optional.empty()));
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;
        private Node parent;

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
