package org.haldokan.edge.search;

import java.util.HashSet;
import java.util.Set;

import org.haldokan.edge.search.BSTree1;
import static org.haldokan.edge.search.BSTree1.Node;

import org.junit.Test;

public class BSTreeTest {
    private Node<Integer> n1 = new Node<>("n1", 7);
    private Node<Integer> n2 = new Node<>("n2", 4);
    private Node<Integer> n3 = new Node<>("n3", 9);
    private Node<Integer> n4 = new Node<>("n4", 6);
    private Node<Integer> n5 = new Node<>("n5", 1);
    private Node<Integer> n6 = new Node<>("n6", 12);
    private Node<Integer> n7 = new Node<>("n7", 10);

    @Test
    public void testTraverse() {
	n1.setLeft(n2);
	n1.setRight(n3);
	n2.setRight(n4);
	n2.setLeft(n5);

	BSTree1<Integer> tree = new BSTree1<>();
	tree.itrav(n1);
	System.out.println();
	tree.prtrav(n1);
	System.out.println();
	tree.potrav(n1);
    }

    @Test
    public void testLargestRangePath() {
	n1.setLeft(n2);
	n1.setRight(n3);
	n2.setRight(n4);
	n2.setLeft(n5);
	n3.setRight(n6);
	n6.setLeft(n7);
	doTestLargestRangePath(n1, 0, 15);
	doTestLargestRangePath(n1, 0, 6);
	doTestLargestRangePath(n1, 9, 12);
	doTestLargestRangePath(n1, 6, 9);
	doTestLargestRangePath(n1, 6, 12);
    }

    private void doTestLargestRangePath(Node<Integer> n, int a, int b) {
	BSTree1<Integer> tree = new BSTree1<>();
	System.out.println("largest range path [" + a + " - " + b + "]: " + tree.rangePath(n1, a, b));
    }

    @Test
    public void testNumOfNodes() {
	n1.setLeft(n2);
	n1.setRight(n3);
	n2.setRight(n4);
	n2.setLeft(n5);
	n3.setRight(n6);
	n6.setLeft(n7);

	BSTree1<Integer> tree = new BSTree1<>();
	System.out.println("num of nodes: " + tree.numOfNodes(n1));
    }

    @Test
    public void testNumOfNodesGT() {
	n1.setLeft(n2);
	n1.setRight(n3);
	n2.setRight(n4);
	n2.setLeft(n5);
	n3.setRight(n6);
	n6.setLeft(n7);

	BSTree1<Integer> tree = new BSTree1<>();

	int num = 4;
	System.out.println("num of nodes GT(" + num + "): " + tree.numOfNodesGT(n1, num));
    }

    @Test
    public void tmp() {
	Set<Node<Integer>> s = new HashSet<>();
	s.add(n1);
	s.add(n2);
	s.add(n3);
	Set<Node<Integer>> s1 = new HashSet<>(s);

	System.out.println(s);
	System.out.println(s1);

	s.remove(n1);
	System.out.println(s);
	System.out.println(s1);
    }
}
