package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - Using BFS find and swap the subtrees
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
public class SwapNodesInBinTree2 {
    public static void main(String[] args) {
        SwapNodesInBinTree2 driver = new SwapNodesInBinTree2();
        Node tree = driver.makeTree();
        driver.print(tree);

        System.out.println();
        driver.swap(tree, 6, 9);
        driver.print(tree);
    }

    void print(Node tree) {
        if (tree == null) {
            return;
        }
        System.out.printf("%s, ", tree);
        print(tree.left);
        print(tree.right);
    }
    void swap(Node tree, int node1, int node2) {
        Deque<Node> deck = new ArrayDeque<>();
        deck.addLast(tree);
        Map<Node, Node> parents = new HashMap<>();

        parents.put(tree, null);
        List<Node> swapParents = new ArrayList<>();

        while (!deck.isEmpty() && swapParents.size() <= 4) {
            Node current = deck.removeFirst();
            if (current.id == node1 || current.id == node2) {
                swapParents.add(parents.get(current));
                swapParents.add(current);
            }
            if (current.left != null) {
                deck.addLast((current.left));
                parents.put(current.left, current);
            }
            if (current.right != null) {
                deck.addLast(current.right);
                parents.put(current.right, current);
            }
        }
        // this could be made more concise by extracting to a method
        Node parent1 = swapParents.get(0);
        if (parent1.left.equals(swapParents.get(1))) {
            parent1.left = swapParents.get(3);
        } else {
            parent1.right = swapParents.get(3);
        }

        Node parent2 = swapParents.get(2);
        if (parent2.left.equals(swapParents.get(3))) {
            parent2.left = swapParents.get(1);
        } else {
            parent2.right = swapParents.get(1);
        }
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
        private final int id;
        private Node left, right;

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
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
