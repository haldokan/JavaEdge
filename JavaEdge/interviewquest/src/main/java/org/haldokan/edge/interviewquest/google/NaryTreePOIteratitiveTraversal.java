package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * My solution to a Google interview question. The solution modifies the data structure in order to mark the processed nodes
 * Post order traversal for an N-ary tree iterative way. Instead of modifying the data structure I could have kept track
 * of process nodes using a hash map
 *
 * The Question: 3.5_STAR
 * Given,
 * struct Node {
 * int val;
 * vector<Node*> children;
 * };
 * <p>
 * Easier solution is to allow modifying the data structure. Harder solution is to disallow it. Do both.
 * Created by haytham.aldokanji on 9/16/15.
 */
public class NaryTreePOIteratitiveTraversal {

    public static void main(String[] args) {
        NaryTreePOIteratitiveTraversal driver = new NaryTreePOIteratitiveTraversal();
        Node f = new Node("F");
        Node b = new Node("B");
        Node a = new Node("A");
        Node d = new Node("D");
        Node c = new Node("C");
        Node e = new Node("E");
        Node g = new Node("G");
        Node i = new Node("I");
        Node h = new Node("H");

        f.add(b);
        f.add(g);
        b.add(a);
        b.add(d);
        d.add(c);
        d.add(e);
        g.add(i);
        i.add(h);
        // A, C, E, D, B, H, I, G, F
        driver.postorder(f);
    }

    public void postorder(Node tree) {
        if (tree == null) {
            throw new NullPointerException("Null input");
        }

        Deque<Node> stack = new LinkedList<>();
        stack.push(tree);
        while (!stack.isEmpty()) {
            Node node = stack.peek();
            if (node.canProcess()) {
                node = stack.pop();
                node.processed = true;
                System.out.print(node.val + ", ");
            } else {
                for (int i = node.children.size() - 1; i >= 0; i--) {
                    stack.push(node.children.get(i));
                }
            }
        }
        System.out.println();
    }

    private static class Node {
        String val;
        List<Node> children;
        boolean processed;

        public Node(String val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public void add(Node val) {
            children.add(val);
        }

        public boolean canProcess() {
            boolean canProcessed = true;
            for (Node n : children)
                canProcessed = canProcessed && n.processed;
            return canProcessed;
        }
    }
}
