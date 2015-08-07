package org.haldokan.edge.interviewquest.google;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * My solution to a Google interview question.
 * 
 * Examine the other more robust solution I provide for this problem in BinaryTreeIteratorWithSynchQu.java.
 * 
 * Given a binary tree, implement an iterator that will iterate through its elements.
 * 
 * @author haldokan
 *
 */
public class BinaryTreeIteratorWithLinkedBlockingQu<E> implements
	Iterable<BinaryTreeIteratorWithLinkedBlockingQu.Node<E>> {
    private Node<E> root;

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

	BinaryTreeIteratorWithLinkedBlockingQu<Integer> driver = new BinaryTreeIteratorWithLinkedBlockingQu<>(root);
	for (Iterator<Node<Integer>> it = driver.iterator(); it.hasNext();) {
	    Node<Integer> node = it.next();
	    System.out.println(node);
	}
    }

    public BinaryTreeIteratorWithLinkedBlockingQu(Node<E> root) {
	this.root = root;
    }

    private void inorder(Node<E> tree, BlockingQueue<Node<E>> iteratorQu) {
	if (tree == null)
	    return;
	inorder(tree.left, iteratorQu);
	iteratorQu.offer(tree);
	inorder(tree.right, iteratorQu);
    }

    @Override
    public Iterator<BinaryTreeIteratorWithLinkedBlockingQu.Node<E>> iterator() {
	// I think it can be done using SychronousQueue but it requires more work
	// BlockingQueue<Node<E>> iteratorStep = new SynchronousQueue<>();
	BlockingQueue<Node<E>> iteratorQu = new LinkedBlockingDeque<>();
	// start a new thread so we can be traversing the tree and iterating at the same time: more space and time
	// efficient. Note that there is a flaw in this sln when iteration is faster than the tree traversal:
	// iterator may find the queue empty while the traversing thread is trying to get the next node. I think this
	// can
	// happen in a large or deep tree; hence my original attempt was to use a SychronousQueue. Of course the other
	// solution is to start the traversal in the same thread calling the iterator but that is less efficient time &
	// space: time because it is sequential and space because it adds treeSize * 64 bits on a 64-bit JVM.
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		inorder(root, iteratorQu);
	    }
	}).start();
	return new IteratorInternal<E>(iteratorQu);
    }

    private static class IteratorInternal<E> implements Iterator<BinaryTreeIteratorWithLinkedBlockingQu.Node<E>> {
	private final BlockingQueue<Node<E>> iteratorQu;

	public IteratorInternal(BlockingQueue<Node<E>> iteratorQu) {
	    this.iteratorQu = iteratorQu;
	}

	@Override
	public boolean hasNext() {
	    return !iteratorQu.isEmpty();
	}

	@Override
	public Node<E> next() {
	    try {
		return iteratorQu.take();
	    } catch (InterruptedException e) {
		throw new RuntimeException(e);
	    }
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
