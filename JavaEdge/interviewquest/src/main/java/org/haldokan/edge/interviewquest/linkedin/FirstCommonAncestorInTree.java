package org.haldokan.edge.interviewquest.linkedin;


/**
 * My solution to a Linkedin interview question.
 * The Question: 3_STAR
 * Given two nodes of a tree,
 * method should return the deepest common ancestor of those nodes.
 * <p>
 * A
 * / \
 * B  C
 * / \ \
 * D  E K
 * / \
 * G  F
 * <p>
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
        System.out.println("(d, f)-> " + fca.commonAncestor1(d, f)); // b
        System.out.println("(e, f)-> " + fca.commonAncestor1(e, f)); // b
        System.out.println("(e, g)-> " + fca.commonAncestor1(e, g)); // b
        System.out.println("(e, d)-> " + fca.commonAncestor1(e, d)); // b
        System.out.println("(c, g)-> " + fca.commonAncestor1(c, g)); // root
        System.out.println("(b, f)-> " + fca.commonAncestor1(b, f)); // root
        System.out.println("(c, k)-> " + fca.commonAncestor1(c, k)); // root
        System.out.println("(g, k)-> " + fca.commonAncestor1(g, k)); // root
        
        System.out.println("(d, f)-> " + fca.commonAncestor2(d, f)); // b
        System.out.println("(e, f)-> " + fca.commonAncestor2(e, f)); // b
        System.out.println("(e, g)-> " + fca.commonAncestor2(e, g)); // b
        System.out.println("(e, d)-> " + fca.commonAncestor2(e, d)); // b
        System.out.println("(c, g)-> " + fca.commonAncestor2(c, g)); // root
        System.out.println("(b, f)-> " + fca.commonAncestor2(b, f)); // root
        System.out.println("(c, k)-> " + fca.commonAncestor2(c, k)); // root
        System.out.println("(g, k)-> " + fca.commonAncestor2(g, k)); // root
    }
    
    public Node commonAncestor1(Node n1, Node n2) {
        if (n1 == null || n2 == null)
            return null;
        Node n = n1.p;
        while (n != null) {
            if (isAncestor(n, n2)) {
                return n;
            }
            n = n.p;
        }
        throw new IllegalStateException("Ancestor must have been found since root is the ultimate ancestor");
    }

    public boolean isAncestor(Node n1, Node n2) {
        Node n = n2.p;
        while (n != null) {
            if (n == n1) {
                return true;
            }
            n = n.p;
        }
        return false;
    }
    
    // this is a bit more interesting way to do it
    private Node commonAncestor2(Node node1, Node node2) {
        if (node1.p == null) {
            return node1;
        }
        if (node2.p ==  null) {
            return node2;
        }
        if (node1.p == node2.p) {
            return node1.p;
        }
        Node ancestor1 = commonAncestor2(node1.p, node2);
        Node ancestor2 = commonAncestor2(node1, node2.p);
        if (ancestor1.p != null) {
            return ancestor1;
        }
        return ancestor2;
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
