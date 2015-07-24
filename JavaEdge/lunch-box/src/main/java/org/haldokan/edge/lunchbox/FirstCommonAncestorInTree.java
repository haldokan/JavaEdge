package org.haldokan.edge.lunchbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
* My solution to a Linkedin interview question
*
* Given two nodes of a tree,
* method should return the deepest common ancestor of those nodes.
*
*  A
* / \
* B  C
* / \ \
* D  E K
* / \
* G  F
*
* commonAncestor(D, F) = B
* commonAncestor(E, G) = B
* commonAncestor(C, G) = A
*/

public class FirstCommonAncestorInTree {

    public static void main(String[] args) {
	Node root = new Node("root");
	Node b = new Node("b");
	Node c = new Node("c");
	Node d = new Node("d");
	Node e = new Node("e");
	Node f = new Node("f");
	Node g = new Node("g");
	Node k = new Node("k");

	root.l = b;
	root.r = c;
	b.l = d;
	b.r = e;
	c.l = k;
	d.l = g;
	d.r = f;

	b.p = root;
	c.p = root;
	d.p = b;
	e.p = b;
	g.p = d;
	f.p = d;
	k.p = c;

	FirstCommonAncestorInTree fca = new FirstCommonAncestorInTree();
//	System.out.println(fca.commonAncestor2(d, f)); // b
//	System.out.println(fca.commonAncestor2(e, g)); // b
//	System.out.println(fca.commonAncestor2(c, g)); // a
	
	fca.postOrder(root, e, g);
	System.out.println(fca.nodes);
	fca.nodes.clear();
//	fca.postOrder(root, e, g);
//	fca.nodes.clear();
//	fca.postOrder(root, c, g);
    }
    
    List<Node> nodes = new ArrayList<>();
    
    public void postOrder(Node n, Node n1, Node n2) {
	if (n == null)
	    return;
	postOrder(n.l, n1, n2);
	postOrder(n.r, n1, n2);
	
	if (nodes.contains(n1) && nodes.contains(n2)) {
	    System.out.println(n);
	    return;
	} else {
	    nodes.add(n);
	}
    }

    public Node commonAncestor3(Node n1, Node n2) {
	return null;
	
    }
    
    public Node commonAncestor(Node n1, Node n2) {
	if (n1.isRoot() || n2.isRoot())
	    return n1;

	if (n1.p == n2.p)
	    return n1.p;

	return commonAncestor(n1, n2.p);
    }

    public Node commonAncestor2(Node n1, Node n2) {
	if (n1.p == n2.p)
	    return n1.p;

	if (!n1.isRoot())
	    return commonAncestor(n1.p, n2);
	if (!n2.isRoot())
	    return commonAncestor(n1, n2.p);

	return commonAncestor(n1.p, n2.p);
    }

    private static class Node {
	private Node p;
	private Node l;
	private Node r;
	private String name;

	public Node(String name) {
	    this.name = name;
	}

	boolean isRoot() {
	    return p == null;
	}
	
	@Override
	public String toString() {
	    return name;
	}
    }
}
