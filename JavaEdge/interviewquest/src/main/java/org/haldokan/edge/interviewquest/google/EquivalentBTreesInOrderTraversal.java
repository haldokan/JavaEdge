package org.haldokan.edge.interviewquest.google;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * My solution to a Google interview question. It short-circuits traversing the trees at the first non-equal nodes.
 * I think it can be resolved w/o using an instance variable 'equivalent' but this would be more involved
 *
 * The Question: 4_STAR
 *
 * Given two binary trees ( not BST) , return true if both of them have same inorder else return false.
 * Eg.
 * <p>
 * <p>
 * B
 * /   \
 * A      C
 * <p>
 * A
 * \
 * B
 * \
 * C
 * Both of the trees have same inorder ( A-B-C) hence function will return true
 * <p>
 * P.S.
 * Please note, we can write inorder method call it once for first tree and then second tree, and finally compare both inorder.
 * <p>
 * We want to do inorder on both trees in parallel, if there is mismatch between inorder nodes of both trees, we can stop the traversal and return false
 * <p>
 * Created by haytham.aldokanji on 9/28/15.
 */
public class EquivalentBTreesInOrderTraversal {
    private volatile boolean equivalent = true;
    private BlockingQueue<Node> qu = new SynchronousQueue<>();

    public static void main(String[] args) throws Exception {
        Node n1 = new Node("A");
        Node n2 = new Node("B");
        Node n3 = new Node("C");
        Node n4 = new Node("D");
        Node n5 = new Node("E");
        Node x = new Node("X");

        // adding the x node make the 2 trees not equivalent
        n1.left = x;
        n1.right = n2;
        n2.right = n4;
        n4.right = n3;
        n3.right = n5;

        Node n_1 = new Node("A");
        Node n_2 = new Node("B");
        Node n_3 = new Node("C");
        Node n_4 = new Node("D");
        Node n_5 = new Node("E");

        n_2.left = n_1;
        n_2.right = n_3;
        n_3.left = n_4;
        n_3.right = n_5;

        EquivalentBTreesInOrderTraversal driver = new EquivalentBTreesInOrderTraversal();
        Thread t1 = new Thread(() -> driver.traverseTree1(n1, driver.qu));
        t1.start();
        Thread t2 = new Thread(() -> driver.traverseTree2(n_2, driver.qu));
        t2.start();

        t1.join();
        t2.join();

        System.out.println(driver.equivalent);
    }

    public void traverseTree1(Node n, BlockingQueue<Node> q) {
        if (n == null || !equivalent)
            return;
        traverseTree1(n.left, q);
        try {
            System.out.println(n.val.toLowerCase());
            q.offer(n, 10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        traverseTree1(n.right, q);
    }

    public void traverseTree2(Node n, BlockingQueue<Node> q) {
        if (n == null || !equivalent)
            return;
        traverseTree2(n.left, q);
        try {
            Node n2 = q.poll(10, TimeUnit.MILLISECONDS);
            if (n2 != null) {
                System.out.println(n.val);
                if (!n.val.equals(n2.val)) {
                    equivalent = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        traverseTree2(n.right, q);
    }

    private static class Node {
        private String val;
        private Node left;
        private Node right;

        public Node(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }
}
