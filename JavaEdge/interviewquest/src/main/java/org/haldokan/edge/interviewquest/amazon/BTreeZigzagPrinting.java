package org.haldokan.edge.interviewquest.amazon;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * My solution to an Amazon interview question
 * <p>
 * TODO: NOT working still. Need to fix how directions and levels are reset
 * <p>
 * Print a binary tree in zig zag way... that is:
 * ......a.........
 * ....b....c.......
 * ..d...e...f...g..
 * .h.i.j.k.l.m.n.o.
 * <p>
 * should be printed as a-c-b-d-e-f-g-o-n-m-l-k-j-i-h
 * Created by haytham.aldokanji on 4/25/16.
 */
public class BTreeZigzagPrinting {

    public static void main(String[] args) {
        BTreeZigzagPrinting driver = new BTreeZigzagPrinting();
        Node tree = driver.makeTree();
        System.out.println(driver.zigzag(tree));
    }

    public List<Node> zigzag(Node tree) {
        Deque<Node> queue = new ArrayDeque<>();
        Deque<Node> stack = new ArrayDeque<>();
        List<Node> zigzag = new ArrayList<>();
        Map<Integer, Integer> levelByNode = new HashMap<>();

        queue.add(tree);
        levelByNode.put(tree.val, 1);

        BiConsumer<Deque<Node>, Node> stackFunc = Deque::add;
        BiConsumer<Deque<Node>, Node> queueFunc = Deque::add;

        while (!queue.isEmpty() || !stack.isEmpty()) {
            while (!queue.isEmpty()) {
                Node node = queue.removeFirst();
                zigzag.add(node);

                addNode(stackFunc, stack, levelByNode, node.left, levelByNode.get(node.val) + 1);
                addNode(stackFunc, stack, levelByNode, node.right, levelByNode.get(node.val) + 1);
            }

            while (!stack.isEmpty()) {
                Node node = stack.removeFirst();
                zigzag.add(node);

                addNode(queueFunc, queue, levelByNode, node.left, levelByNode.get(node.val) + 1);
                addNode(queueFunc, queue, levelByNode, node.right, levelByNode.get(node.val) + 1);
            }

        }
        return zigzag;
    }

    private void addNode(BiConsumer<Deque<Node>, Node> nodeAdder,
                         Deque<Node> deck,
                         Map<Integer, Integer> levelByNode,
                         Node node,
                         int level) {
        if (node == null) {
            return;
        }
        nodeAdder.accept(deck, node);
        levelByNode.put(node.val, level);
    }

    private Node makeTree() {
        /*
                 1
          2             3
      5      4       7     6
    8          9   10    11  12
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

        n1.left = n2;
        n1.right = n3;
        n2.left = n5;
        n2.right = n4;
        n3.left = n7;
        n3.right = n6;
        n5.left = n8;
        n4.right = n9;
        n7.left = n10;
        n6.left = n11;
        n6.right = n12;

        return n1;
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
