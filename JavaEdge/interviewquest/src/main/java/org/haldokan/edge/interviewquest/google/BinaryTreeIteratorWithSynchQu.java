package org.haldokan.edge.interviewquest.google;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * My solution to a Google interview question.
 * <p>
 * Given a binary tree, implement an iterator that will iterate through its elements.
 *
 * @author haldokan
 */
public class BinaryTreeIteratorWithSynchQu<E> implements Iterable<BinaryTreeIteratorWithSynchQu.Node<E>> {
    private Node<E> root;

    public BinaryTreeIteratorWithSynchQu(Node<E> root) {
        this.root = root;
    }

    public static void main(String[] args) {
        Node<Integer> root = new Node<>(7);
        Node<Integer> n2 = new Node<>(4);
        Node<Integer> n3 = new Node<>(9);
        Node<Integer> n4 = new Node<>(6);
        Node<Integer> n5 = new Node<>(1);
        Node<Integer> n6 = new Node<>(12);
        Node<Integer> n7 = new Node<>(10);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n6.left = n7;

        System.out.println("Typical iteration with next called after hasNext");
        BinaryTreeIteratorWithSynchQu<Integer> driver = new BinaryTreeIteratorWithSynchQu<>(root);
        for (Iterator<Node<Integer>> it = driver.iterator(); it.hasNext(); ) {
            Node<Integer> node = it.next();
            System.out.println(node);
        }
        System.out.println("Calling hasNext multiple times should have no effect on advancing the iterator");
        for (Iterator<Node<Integer>> it = driver.iterator(); it.hasNext(); ) {
            for (int i = 0; i < 5; i++)
                it.hasNext();
            Node<Integer> node = it.next();
            System.out.println(node);
        }

        System.out.println("Calling next w/o hasNext should work and throws exception at the end of iteration");
        Iterator<Node<Integer>> it = driver.iterator();
        for (; ; ) {
            try {
                Node<Integer> node = it.next();
                System.out.println(node);
            } catch (NoSuchElementException e) {
                System.out.println("Expected exception");
                e.printStackTrace();
                break;
            }
        }

    }

    private void inorder(Node<E> tree, BlockingQueue<Node<E>> iteratorQu) {
        if (tree == null)
            return;
        inorder(tree.left, iteratorQu);
        for (; ; ) {
            try {
                // 'put' on SynchronousQueue waits for consumer threads to become available (unlike 'offer')
                iteratorQu.put(tree);
                break;
            } catch (InterruptedException e) {
                // ignore and go back to blocking on queue
            }
        }
        inorder(tree.right, iteratorQu);
    }

    @Override
    public Iterator<BinaryTreeIteratorWithSynchQu.Node<E>> iterator() {
        BlockingQueue<Node<E>> iteratorQu = new SynchronousQueue<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                inorder(root, iteratorQu);
            }
        }).start();
        return new IteratorInternal<E>(iteratorQu);
    }

    private static class IteratorInternal<E> implements Iterator<BinaryTreeIteratorWithSynchQu.Node<E>> {
        private final BlockingQueue<Node<E>> iteratorQu;
        private Node<E> nextNode;

        public IteratorInternal(BlockingQueue<Node<E>> iteratorQu) {
            this.iteratorQu = iteratorQu;
        }

        @Override
        public boolean hasNext() {
            if (nextNode == null) {
                for (; ; ) {
                    try {
                        // the timeout is needed to give the thread that traverses the tree to start and put nodes on
                        // the synch queue
                        nextNode = iteratorQu.poll(10, TimeUnit.MILLISECONDS);
                        break;
                    } catch (InterruptedException e) {
                        // ignore and go back to blocking on the queue
                    }
                }
                return nextNode != null;
            }
            return true;
        }

        @Override
        public Node<E> next() {
            if (nextNode == null) {
                if (!hasNext())
                    throw new NoSuchElementException("No more elements in iteration");
            }
            Node<E> next = nextNode;
            nextNode = null;
            return next;
        }
    }

    public static class Node<E> {
        private final E value;
        private Node<E> left;
        private Node<E> right;

        public Node(E v) {
            this.value = v;
        }

        @Override
        public String toString() {
            return "(" + value + ")";
        }
    }
}
