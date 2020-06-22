package org.haldokan.edge.interviewquest.google;

import java.util.Random;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question
 * The Question: 3_STAR
 * <p>
 * Suppose we use binary search tree to implement set, design an algorithm that we can use to get a random element from the
 * set, while maintaining all the other set operations have same complexities.
 * <p>
 * Created by haytham.aldokanji on 7/3/16.
 */
public class RandomElementInBST {
    private static Random rand = new Random();

    public static void main(String[] args) {
        RandomElementInBST driver = new RandomElementInBST();
        driver.testTreeSize();
        driver.testRandom();
    }

    public Node random(Node node) {
        int treeSize = treeSize(node);
        int randIndex = 1 + rand.nextInt(treeSize);
        return random(node, new int[]{randIndex}, new Node[1]);
    }

    private Node random(Node node, int[] k, Node[] arr) {
        if (node == null) {
            return null;
        }
        random(node.left, k, arr);
        k[0] -= 1;
        if (k[0] >= 0) {
            arr[0] = node;
            random(node.right, k, arr);
        }
        return arr[0];
    }

    private int treeSize(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + treeSize(node.left) + treeSize(node.right);
    }

    private Node makeTree() {
        Node root = new Node(7);
        Node n2 = new Node(4);
        Node n3 = new Node(9);
        Node n4 = new Node(6);
        Node n5 = new Node(1);
        Node n6 = new Node(12);
        Node n7 = new Node(10);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        return root;
    }

    private void testTreeSize() {
        Node tree = makeTree();
        assertThat(treeSize(tree), is(7));
    }

    private void testRandom() {
        Node tree = makeTree();
        IntStream.range(0, 100).forEach(i -> {
            System.out.printf("%s, ", random(tree));
            if (i % 10 == 0) {
                System.out.printf("%n");
            }
        });
    }

    private static class Node {
        private int id;
        private Node left;
        private Node right;

        public Node(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
