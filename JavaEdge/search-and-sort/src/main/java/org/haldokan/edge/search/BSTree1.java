package org.haldokan.edge.search;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class BSTree1<E extends Comparable<E>> {
    private Node<E> tree = null;

    public void inorder(Node<E> tree) {
	if (tree == null)
	    return;
	inorder(tree.left);
	System.out.println(tree.value);
	inorder(tree.right);
    }

    public void preorder(Node<E> tree) {
	if (tree == null)
	    return;
	System.out.println(tree.getValue());
	preorder(tree.getLeft());
	preorder(tree.getRight());
    }

    public void postorder(Node<E> tree) {
	if (tree == null)
	    return;
	postorder(tree.getLeft());
	postorder(tree.getRight());
	System.out.println(tree.getValue());
    }

    public int numOfNodes(Node<E> tree) {
	if (tree == null)
	    return 0;
	return 1 + numOfNodes(tree.left) + numOfNodes(tree.right);
    }

    public int numOfNodesGT(Node<E> tree, E e) {
	if (tree == null)
	    return 0;
	int incr = 0;
	if (tree.value.compareTo(e) > 0) {
	    incr = 1;
	    System.out.println(tree);
	}
	return incr + numOfNodesGT(tree.left, e) + numOfNodesGT(tree.right, e);
    }

    public void add(Node<E> n) {
	if (tree == null)
	    tree = n;
    }

    // We want the size and possibly the tree that have all its nodes in a
    // range [a, b]
    public Deque<Node<E>> rangePath(Node<E> t, E a, E b) {
	Map<Node<E>, Node<E>> parent = new LinkedHashMap<>();
	if (t != null) {
	    parent.put(t, null);
	    doRangePath(t, parent);
	}
	return getLargestRangePath(t, a, b, parent);
    }

    public void doRangePath(Node<E> t, Map<Node<E>, Node<E>> parent) {
	if (t == null)
	    return;
	doRangePath(t.left, parent);
	if (t.left != null)
	    parent.put(t.left, t);
	doRangePath(t.right, parent);
	if (t.right != null)
	    parent.put(t.right, t);
    }

    private Deque<Node<E>> getLargestRangePath(Node<E> node, E a, E b, Map<Node<E>, Node<E>> parent) {
	Deque<Node<E>> maxRangePath = new LinkedList<>();
	int maxSize = 0;
	Node<E> startNode = null;

	for (Node<E> n : parent.keySet()) {
	    int size = getRangePathLen(n, a, b, parent);
	    if (size > maxSize) {
		maxSize = size;
		startNode = n;
	    }
	}
	// System.out.println("maxsize/startnode: " + maxSize + "/" +
	// startNode);
	getRangePath(startNode, a, b, maxRangePath, parent);
	return maxRangePath;
    }

    public int getRangePathLen(Node<E> node, E a, E b, Map<Node<E>, Node<E>> parent) {
	if (node == null)
	    return 0;
	int incr = 0;
	if (node.value.compareTo(a) >= 0 && node.value.compareTo(b) <= 0) {
	    // System.out.println(node);
	    incr = 1;
	}
	return incr + getRangePathLen(parent.get(node), a, b, parent);
    }

    public void getRangePath(Node<E> node, E a, E b, Deque<Node<E>> deck, Map<Node<E>, Node<E>> parent) {
	if (node == null)
	    return;
	if (node.value.compareTo(a) >= 0 && node.value.compareTo(b) <= 0) {
	    deck.addFirst(node);
	    getRangePath(parent.get(node), a, b, deck, parent);
	}
    }

    public static class Node<E> {
	private final E value;
	private final String name;
	private Node<E> left;
	private Node<E> right;

	public Node(String name, E v) {
	    this.name = name;
	    this.value = v;
	}

	public Node<E> getLeft() {
	    return left;
	}

	public void setLeft(Node<E> left) {
	    this.left = left;
	}

	public Node<E> getRight() {
	    return right;
	}

	public void setRight(Node<E> right) {
	    this.right = right;
	}

	public E getValue() {
	    return value;
	}

	public String getName() {
	    return name;
	}

	@Override
	public String toString() {
	    return name + "(" + value + ")";
	}
    }
}
