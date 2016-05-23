package org.haldokan.edge.interviewquest.google.imageserver;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - inserting the nodes in pre-order traversal reconstructs the tree. Also
 * inserting the nodes in reverse post-order traversal reconstructs the tree. Only in-order traversal forgets
 * the structure of the tree. I present here reconstructing the tree based on each of pre- and post-order traversals.
 * <p>
 * You are given printouts from an algorithm which ran over a sorted binary tree. One printout is from an in-order run
 * and another from a pre-order run. Can you reconstruct the tree? If so, then write an algorithm.
 * <p>
 * Created by haytham.aldokanji on 5/22/16.
 */
public class ReconstructBSTFromInAndPreOrderTraversal {
    private Node regeneratedTree;

    public static void main(String[] args) {
        ReconstructBSTFromInAndPreOrderTraversal driver = new ReconstructBSTFromInAndPreOrderTraversal();

        driver.testRebuildTreeWithPreOrderTraversal();
        driver.testRebuildTreeWithPostOrderTraversal();
    }

    private void preorder(Node tree, Deque<Integer> evalQueue) {
        if (tree == null) {
            return;
        }
        evalQueue.add(tree.val);
        preorder(tree.left, evalQueue);
        preorder(tree.right, evalQueue);
    }

    private void postorder(Node tree, Deque<Integer> evalStack) {
        if (tree == null) {
            return;
        }
        postorder(tree.left, evalStack);
        postorder(tree.right, evalStack);
        evalStack.push(tree.val);
    }

    public void insert(int val) {
        if (regeneratedTree == null) {
            regeneratedTree = new Node(val);
        } else {
            doInsert(regeneratedTree, val);
        }
    }

    private void doInsert(Node node, int val) {
        if (node == null)
            return;
        if (val < node.val) {
            doInsert(node.left, val);
            if (node.left == null) {
                node.left = new Node(val);
            }
        } else {
            doInsert(node.right, val);
            if (node.right == null) {
                node.right = new Node(val);
            }
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
        n7.left = n4;
        n7.right = n9;
        n4.right = n6;
        n4.left = n1;
        n9.right = n12;
        n12.left = n10;

        return n7;
    }

    private void testRebuildTreeWithPreOrderTraversal() {
        regeneratedTree = null;

        Node tree1 = makeTree();
        Deque<Integer> evalQueue = new ArrayDeque<>();
        Deque<Integer> copyQueue = new ArrayDeque<>();

        preorder(tree1, evalQueue);
        while (!evalQueue.isEmpty()) {
            int val = evalQueue.remove();
            insert(val);
            copyQueue.add(val);
        }
        // now pre-order the reconstructed tree and assert it has the same structure
        preorder(regeneratedTree, evalQueue);
        assertThat(evalQueue.toArray(), is(copyQueue.toArray()));
    }

    private void testRebuildTreeWithPostOrderTraversal() {
        regeneratedTree = null;

        Node tree1 = makeTree();
        Deque<Integer> evalStack = new ArrayDeque<>();
        Deque<Integer> copyStack = new ArrayDeque<>();

        postorder(tree1, evalStack);
        while (!evalStack.isEmpty()) {
            int val = evalStack.pop();
            insert(val);
            copyStack.add(val);
        }
        // now pre-order the reconstructed tree and assert it has the same structure
        postorder(regeneratedTree, evalStack);
        assertThat(evalStack.toArray(), is(copyStack.toArray()));
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
