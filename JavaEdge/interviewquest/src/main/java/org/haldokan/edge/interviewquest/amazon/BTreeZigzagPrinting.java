package org.haldokan.edge.interviewquest.amazon;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * My solution to an Amazon interview question - tricky! Wasted time trying to use queue & stack together. What is needed
 * is 2 stacks and reversing the order of adding child nodes to them.
 * The Question: 5_STAR
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

        List<Node> zigzag = driver.zigzag(tree);
        System.out.println(zigzag);
        assertThat(zigzag.stream()
                        .map(n -> n.val)
                        .collect(Collectors.toList()),
                is(Arrays.asList(1, 2, 3, 6, 7, 4, 5, 8, 13, 9, 10, 12, 11, 14, 19, 18, 17, 16, 15)));

    }

    public List<Node> zigzag(Node tree) {
        Deque<Node> leftToRightStack = new ArrayDeque<>();
        Deque<Node> rightToLeftStack = new ArrayDeque<>();
        List<Node> zigzag = new ArrayList<>();

        leftToRightStack.push(tree);

        BiConsumer<Deque<Node>, Node> stackFunc = Deque::push;

        while (!leftToRightStack.isEmpty() || !rightToLeftStack.isEmpty()) {
            while (!leftToRightStack.isEmpty()) {
                Node node = leftToRightStack.pop();
                zigzag.add(node);

                // note hot the order of right/left nodes is swapped b/w the two while loops
                addNode(stackFunc, rightToLeftStack, node.right);
                addNode(stackFunc, rightToLeftStack, node.left);
            }

            while (!rightToLeftStack.isEmpty()) {
                Node node = rightToLeftStack.pop();
                zigzag.add(node);

                addNode(stackFunc, leftToRightStack, node.left);
                addNode(stackFunc, leftToRightStack, node.right);
            }

        }
        return zigzag;
    }

    private void addNode(BiConsumer<Deque<Node>, Node> nodeAdder,
                         Deque<Node> deck,
                         Node node) {
        if (node == null) {
            return;
        }
        nodeAdder.accept(deck, node);
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
