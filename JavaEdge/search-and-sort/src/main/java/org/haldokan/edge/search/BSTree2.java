package org.haldokan.edge.search;

import java.util.Random;

public class BSTree2<E extends Comparable<E>> {
    private enum Order {
	IN, PR, PO
    };

    private Node<E> tree;

    public static void main(String[] args) {
	BSTree2<Integer> bt = new BSTree2<>();
	Random r = new Random();
	int num1 = 50;
	bt.insert(num1);

	for (int i = 0; i < 10; i++) {
	    bt.insert(r.nextInt(100));
	}
	int num2 = 70;
	bt.insert(num2);

	bt.walk(Order.IN);
	System.out.println("found: " + bt.find(bt.tree, num1));
	System.out.println("found: " + bt.find(bt.tree, num2));
	System.out.println("found: " + bt.find(bt.tree, 777));
	System.out.println("successor " + num1 + ": " + bt.successor(bt.find(bt.tree, num1)));
	// num2 is inserted at the end so it is a leaf. The successor method
	// does not find it. Fixit.
	System.out.println("successor " + num2 + ": " + bt.successor(bt.find(bt.tree, num2)));
    }

    public void walk(Order o) {
	doWalk(tree, o);
    }

    private void doWalk(Node<E> node, Order o) {
	if (node == null)
	    return;
	doWalk(node.l, o);
	System.out.println(node.e);
	doWalk(node.r, o);
    }

    public void insert(E e) {
	if (tree == null)
	    tree = new Node<E>(e);
	else
	    doInsert(tree, e);
    }

    private void doInsert(Node<E> node, E e) {
	if (node == null)
	    return;
	if (e.compareTo(node.e) < 0) {
	    doInsert(node.l, e);
	    if (node.l == null) {
		node.l = new Node<>(e);
	    }
	} else {
	    doInsert(node.r, e);
	    if (node.r == null) {
		node.r = new Node<>(e);
	    }
	}
    }

    public void remove(E e) {
	doRemove(tree, e);
    }

    private void doRemove(Node<E> node, E e) {
	if (node == null)
	    return;
	if (e.compareTo(node.e) < 0) {
	    doRemove(node.l, e);
	} else if (e.compareTo(node.e) > 0) {
	    doRemove(node.r, e);
	} else if (e.compareTo(node.e) == 0) {
	    // TODO: how to rmv? need parent ref?
	}
    }

    // if the node is a leaf the parent is the successor; this code does not do
    // that.
    private Node<E> successor(Node<E> node) {
	if (node == null)
	    return null;
	// if node is a leaf its parent is the successor
	if (node.l == null && node.r == null) {
	    // I think it is not possible to find the parent w/o reference to
	    // the parent in the each node.
	    // Node that we cann recur to the parent using in-order traversal
	    // because the node passed as param
	    // is not the root but the subtree originating in the node we are
	    // searching its successor: if node
	    // is a leaf we are passed a single node and thus no way to find the
	    // parent.
	    System.out.println("Bad luck; successor not going to be found!!!");
	}
	return doSuccessor(node.r);
    }

    private Node<E> doSuccessor(Node<E> node) {
	if (node == null)
	    return null;
	if (node.l == null)
	    return node;
	return doSuccessor(node.l);
    }

    private Node<E> find(Node<E> node, E e) {
	if (node == null)
	    return null;
	if (e.compareTo(node.e) == 0)
	    return node;
	if (e.compareTo(node.e) < 0)
	    return find(node.l, e);
	else
	    return find(node.r, e);
    }

    private static class Node<E extends Comparable<E>> {
	private Node<E> l, r;
	private E e;

	public Node(E e) {
	    this.e = e;
	}

	@Override
	public String toString() {
	    return "Node [e=" + e + "]";
	}
    }
}