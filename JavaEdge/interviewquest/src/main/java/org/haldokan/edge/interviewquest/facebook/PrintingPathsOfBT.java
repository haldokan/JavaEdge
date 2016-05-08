package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * My solution to a Facebook interview question
 * <p>
 * Print all paths of a binary tree from root to leaf.
 * <p>
 * Later, extend the solution to work with graphs, careful attention to cycles which you should print as paths as well
 * (without printing visited nodes twice).
 * <p>
 * Created by haytham.aldokanji on 5/7/16.
 */
public class PrintingPathsOfBT {

    public static void main(String[] args) {
        PrintingPathsOfBT driver = new PrintingPathsOfBT();
        Node tree = driver.makeTree();
        driver.printPaths(tree);
    }

    public void printPaths(Node tree) {
        doPrintPath(tree, new ArrayDeque<>());
    }

    private void doPrintPath(Node node, Deque<Node> stack) {
        if (node == null) {
            return;
        }
        stack.push(node);

        doPrintPath(node.left, stack);
        doPrintPath(node.right, stack);

        if (node.leaf()) {
            printPath(stack);
        }
        stack.pop();
    }

    private void printPath(Deque<Node> path) {
        for (Iterator<Node> it = path.descendingIterator(); it.hasNext(); ) {
            System.out.print(it.next() + "-");
        }
        System.out.println();
    }

    private Node makeTree() {
        Node n7 = new Node(7);
        Node n4 = new Node(4);
        Node n9 = new Node(9);
        Node n6 = new Node(6);
        Node n1 = new Node(1);
        Node n12 = new Node(12);
        Node n10 = new Node(10);
        n7.left = n4;
        n7.right = n9;
        n4.right = n6;
        n4.left = n1;
        n9.right = n12;
        n9.left = n10;

        return n7;
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;

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
