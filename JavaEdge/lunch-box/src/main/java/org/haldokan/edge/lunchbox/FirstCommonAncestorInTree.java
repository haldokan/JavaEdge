package org.haldokan.edge.lunchbox;



/**
* My solution to a Linkedin interview question. 
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
	// construct the tree shown in the class comment
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
	System.out.println("(d, f)-> " + fca.commonAncestor(d, f)); // b
	System.out.println("(e, f)-> " + fca.commonAncestor(e, f)); // b
	System.out.println("(e, g)-> " + fca.commonAncestor(e, g)); // b
	System.out.println("(e, d)-> " + fca.commonAncestor(e, d)); // b
	System.out.println("(c, g)-> " + fca.commonAncestor(c, g)); // root
	System.out.println("(b, f)-> " + fca.commonAncestor(b, f)); // root
	System.out.println("(c, k)-> " + fca.commonAncestor(c, k)); // root
	System.out.println("(g, k)-> " + fca.commonAncestor(g, k)); // root
    }
    
    public Node commonAncestor(Node n1, Node n2) {
	if (n1 == null || n2 == null)
	    return null;
	Node n = n1.p;
	while (n != null) {
	    if (isAncestor(n, n2))
		return n;
	    n = n.p;
	}
	throw new IllegalStateException("Ancestor must have been found since root is the ultimate ancestor");
    }
    
    public boolean isAncestor(Node n1, Node n2) {
	Node n = n2.p;
	while (n != null) {
	    if (n == n1)
		return true;
	    n = n.p;
	}
	return false;
    }

    private static class Node {
	private Node p;
	private Node l;
	private Node r;
	private String name;

	public Node(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
	    return name;
	}
    }
}
