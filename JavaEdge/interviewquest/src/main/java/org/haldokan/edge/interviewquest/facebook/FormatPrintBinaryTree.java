package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * My solution to a Facebook interview question - No formatting apart from breaking the line after printing each level
 * Printing a well formatted tree requires doing some geometry I think
 * The Question: 3_STAR
 * Print a Binary Tree such that it looks like a tree (with new lines and indentation,
 * the way we see it in algorithms books).
 * <p>
 * Created by haytham.aldokanji on 5/11/16.
 */
public class FormatPrintBinaryTree {
    public static void main(String[] args) {
        FormatPrintBinaryTree driver = new FormatPrintBinaryTree();
        Node tree = driver.makeTree();
        driver.formatPrintTree(tree);
    }

    public void formatPrintTree(Node tree) {
        Map<Node, Integer> nodeLevel = new IdentityHashMap<>();

        Deque<Node> evalQueue = new ArrayDeque<>();
        evalQueue.add(tree);
        nodeLevel.put(tree, 0);

        Multimap<Integer, Node> nodesByLevel = ArrayListMultimap.create();
        while (!evalQueue.isEmpty()) {
            Node node = evalQueue.removeFirst();
            nodesByLevel.put(nodeLevel.get(node), node);

            if (node.left != null) {
                evalQueue.add(node.left);
                nodeLevel.put(node.left, nodeLevel.get(node) + 1);
            }
            if (node.right != null) {
                evalQueue.add(node.right);
                nodeLevel.put(node.right, nodeLevel.get(node) + 1);
            }
        }
        formatPrint(nodesByLevel);
    }

    private void formatPrint(Multimap<Integer, Node> nodesByLevel) {
        nodesByLevel.keySet()
                .stream()
                .sorted()
                .forEach(v -> System.out.println(nodesByLevel.get(v)));
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
