package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - used iteration BF traversal in both directions
 * The Question: 5_STAR
 * Given a normal binary tree, write a function to serialize the tree into a string representation
 * (returning the string), and also a function to deserialize a serialized string into the original binary tree.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class BTRoundTripSerializingToString {
    private static final String AT = "@";
    private static final String DASH = "-";
    private static final String NULL_NODE_VAL = " ";

    public static void main(String[] args) {
        BTRoundTripSerializingToString driver = new BTRoundTripSerializingToString();
        Node tree = driver.makeTree();
        // convert tree to string
        String treeInString = driver.fromTreeToString(tree);
        // convert the string back to tree
        Node treeFromString = driver.fromStringToTree(treeInString);
        // assert that the new tree is the same as the original
        driver.assertEqualTrees(tree, treeFromString);
    }

    public String fromTreeToString(Node tree) {
        Deque<Node> evalQueue = new ArrayDeque<>();
        evalQueue.add(tree);
        StringBuilder treeInString = new StringBuilder(1024);
        treeInString.append(tree.val).append(AT);

        while (!evalQueue.isEmpty()) {
            Node node = evalQueue.removeFirst();
            String leftChildVal = NULL_NODE_VAL;
            String rightChildVal = NULL_NODE_VAL;

            if (node.left != null) {
                evalQueue.add(node.left);
                leftChildVal = String.valueOf(node.left.val);
            }
            if (node.right != null) {
                evalQueue.add(node.right);
                rightChildVal = String.valueOf(node.right.val);
            }

            treeInString.append(leftChildVal)
                    .append(DASH)
                    .append(rightChildVal)
                    .append(AT);
        }
        return treeInString.toString();
    }

    public Node fromStringToTree(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        Deque<String> formattedNodesQueue = new ArrayDeque<>(Arrays.asList(input.split(AT)));

        Node root = makeNodes(formattedNodesQueue.remove())[0];
        Deque<Node> evalQueue = new ArrayDeque<>();
        evalQueue.add(root);

        while (!evalQueue.isEmpty()) {
            Node node = evalQueue.pop();
            Node[] children = makeNodes(formattedNodesQueue.remove());

            node.left = children[0];
            node.right = children[1];

            if (node.left != null) {
                evalQueue.add(node.left);
            }
            if (node.right != null) {
                evalQueue.add(node.right);
            }
        }
        return root;
    }

    private Node[] makeNodes(String subTree) {
        String[] nodeValues = subTree.split(DASH);
        Node[] nodes = new Node[nodeValues.length];

        int index = 0;
        for (String val : nodeValues) {
            nodes[index++] = val.equals(NULL_NODE_VAL) ? null : new Node(Integer.parseInt(val));

        }
        return nodes;
    }

    private void assertEqualTrees(Node tree1, Node tree2) {
        String tree1Str = treeToString(tree1);
        String tree2Str = treeToString(tree2);
        assertThat(tree1Str, is(tree2Str));
    }

    private String treeToString(Node tree) {
        if (tree == null) {
            return "";
        }
        return tree.val + treeToString(tree.left) + treeToString(tree.right);
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
