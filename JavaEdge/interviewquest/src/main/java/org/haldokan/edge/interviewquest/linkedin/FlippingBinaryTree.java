package org.haldokan.edge.interviewquest.linkedin;

import java.util.Deque;
import java.util.LinkedList;

/**
 * My solution to a Linkedin interview question
 * 
 * Given a binary tree where all the right nodes are leaf nodes, flip it upside down and turn it into a tree with left leaf nodes.
 *
 * for example, turn these:
 *
 *        1                   1
 *       / \                 / \
 *      2   3               2   3
 *     / \
 *    4   5
 *   / \
 *  6   7
 *
 * into these:
 *
 *        1               1
 *       /               /
 *      2---3           2---3
 *     /
 *    4---5
 *   /
 *  6---7
 *
 * where 6 is the new root node for the left tree, and 2 for the right tree.
 * oriented correctly:
 *
 *     6                     2
 *    / \                   / \
 *   7   4                 3   1
 *       / \
 *       5   2
 *          / \
 *          3   1
 * 
 *  @author haldokan          
 */
public class FlippingBinaryTree {
    public static void main(String[] args) {
	Node<Integer> n1 = new Node<>(1);
	Node<Integer> n2 = new Node<>(2);
	Node<Integer> n3 = new Node<>(3);
	Node<Integer> n4 = new Node<>(4);
	Node<Integer> n5 = new Node<>(5);
	Node<Integer> n6 = new Node<>(6);
	Node<Integer> n7 = new Node<>(7);
	
	n1.left = n2;
	n1.right = n3;
	n2.left = n4;
	n2.right = n5;
	n4.left = n6;
	n4.right = n7;
	
	FlippingBinaryTree fbt = new FlippingBinaryTree();
	Node<Integer> root = fbt.flip(n1);
	fbt.walkTree(root);
    }
    
    private Node<Integer> flip(Node<Integer> node) {
	Deque<Node<Integer>> deck = new LinkedList<>();
	doFlip(node, deck);
	Node<Integer> root = linkNodes(deck);
	return root;
    }
    
    private void doFlip(Node<Integer> node, Deque<Node<Integer>> deck) {
	if (node == null)
	    return;
	doFlip(node.left, deck);
	Node<Integer> right = node.right;
	if (right != null) {
	    node.right = null;
	    if (node.left != null) {
		node.left.left = right;
		node.left = null;
	    } else {
		node.left = right;
	    }
	} else {
	    node.left = null;
	}
	deck.addLast(node);
    }
    
    private Node<Integer> linkNodes(Deque<Node<Integer>> deck) {
	Node<Integer> root = deck.pop();
	Node<Integer> curr = root;
	while (!deck.isEmpty()) {
	    curr.right = deck.pop();
	    curr = curr.right;
	}
	return root;
    }
    
    private void walkTree(Node<Integer> n) {
	if (n == null)
	    return;
	System.out.println(n);
	walkTree(n.left);
	walkTree(n.right);
    }
    
    private static class Node<E> {
 	private E id;
 	private Node<E> left;
 	private Node<E> right;

 	public Node(E id) {
 	    this.id = id;
 	}

 	@Override
 	public String toString() {
 	    return String.valueOf(id);
 	}
     }   
}
