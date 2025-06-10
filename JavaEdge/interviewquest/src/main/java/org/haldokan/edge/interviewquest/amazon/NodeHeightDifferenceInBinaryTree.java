package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to an Amazon interview question - Easy kill! BFS while keeping track of nodes level in a map
 * The Question: 4_STAR
 * <p>
 * Find the height difference between two nodes in a binary tree.
 * <p>
 * Created by haytham.aldokanji on 7/31/16.
 */

public class NodeHeightDifferenceInBinaryTree {

    public static void main(String[] args) {
        NodeHeightDifferenceInBinaryTree driver = new NodeHeightDifferenceInBinaryTree();
        driver.test();
    }

    public int heightDifference(Node tree, int nodeId1, int nodeId2) {
        Deque<Node> evalDeck = new ArrayDeque<>();
        Map<Node, Integer> nodeLevel = new HashMap<>();

        evalDeck.add(tree);
        nodeLevel.put(tree, 1);

        int level1 = 0;
        int level2 = 0;

        while (!evalDeck.isEmpty()) {
            Node currentNode = evalDeck.pop();

            int currentNodeLevel = nodeLevel.get(currentNode);
            if (currentNode.id == nodeId1) {
                level1 = currentNodeLevel;
            } else if (currentNode.id == nodeId2) {
                level2 = currentNodeLevel;
            }
            // short-circuit BFS once we found the 2 nodes we're looking for
            if (level1 * level2 != 0) {
                return Math.abs(level1 - level2);
            }

            Node leftChild = currentNode.left;
            if (leftChild != null) {
                evalDeck.add(leftChild);
                nodeLevel.put(leftChild, currentNodeLevel + 1);
            }
            Node rightChild = currentNode.right;
            if (rightChild != null) {
                evalDeck.add(rightChild);
                nodeLevel.put(rightChild, currentNodeLevel + 1);
            }
        }
        return -1; // indicating that the nodes were not found
    }

    private void test() {
        Node tree = makeTree();
        int heightDiff = heightDifference(tree, 2, 17);
        assertThat(heightDiff, is(3));

        heightDiff = heightDifference(tree, 1, 6);
        assertThat(heightDiff, is(2));

        heightDiff = heightDifference(tree, 12, 15);
        assertThat(heightDiff, is(1));

        heightDiff = heightDifference(tree, 5, 7);
        assertThat(heightDiff, is(0));
    }

    private Node makeTree() {
        /*
                 1
          2              3
      5      4       7        6
    8     13   9   10 12     11  14
        15     16          17   18 19
         */
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        Node n8 = new Node(8);
        Node n9 = new Node(9);
        Node n10 = new Node(10);
        Node n11 = new Node(11);
        Node n12 = new Node(12);
        Node n13 = new Node(13);
        Node n14 = new Node(14);
        Node n15 = new Node(15);
        Node n16 = new Node(16);
        Node n17 = new Node(17);
        Node n18 = new Node(18);
        Node n19 = new Node(19);

        n1.left = n2;
        n1.right = n3;
        n2.left = n5;
        n2.right = n4;
        n3.left = n7;
        n3.right = n6;
        n5.left = n8;
        n4.right = n9;
        n4.left = n13;
        n7.left = n10;
        n7.right = n12;
        n6.left = n11;
        n6.right = n14;
        n13.left = n15;
        n9.right = n16;
        n11.left = n17;
        n14.left = n18;
        n14.right = n19;

        return n1;
    }

    private static class Node {
        private int id;
        private Node left;
        private Node right;

        public Node(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return id == node.id;

        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
