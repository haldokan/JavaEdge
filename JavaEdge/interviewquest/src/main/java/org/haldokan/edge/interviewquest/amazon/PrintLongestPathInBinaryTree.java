package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

/**
 * My solution to an Amazon interview question - solution can be optimized to get the longest path during the traversal
 * instead of keeping the paths for all leaves
 * 
 * used BFS (would be interesting to do it using DFS)
 *
 * The Question: 3.5-STAR
 *
 * Print the longest path from root to leaf in a Binary tree (Basically the nodes that lie on the height path).
 */
public class PrintLongestPathInBinaryTree {
    public static void main(String[] args) {
        PrintLongestPathInBinaryTree driver = new PrintLongestPathInBinaryTree();
        System.out.println(driver.longestPath(driver.makeTree()));
        System.out.println(driver.longestPath(new Node(7)));
    }

    public List<Node> longestPath(Node tree) {
        if (tree == null) {
            throw new IllegalArgumentException("Null tree");
        }
        Map<Node, Node> ancestry = new HashMap<>();
        LinkedList<Node> queue = new LinkedList<>();

        queue.add(tree);
        ancestry.put(tree, null);

        List<Node> leaves = new ArrayList<>();

        while (!queue.isEmpty()) {
            Node current = queue.removeFirst();
            if (current.left == null && current.right == null) {
                leaves.add(current);
            }
            if (current.left != null) {
                queue.addLast(current.left);
                ancestry.put(current.left, current);
            }
            if (current.right != null) {
                queue.addLast(current.right);
                ancestry.put(current.right, current);
            }
        }
        return getLongestPath(ancestry, leaves);
    }

    private List<Node> getLongestPath(Map<Node, Node> ancestry, List<Node> leaves) {
        Node deepestLeaf = null;
        int longest = 0;

        for (Node node : leaves) {
            Node parent = node;
            int pathLen = 0;

            while (parent != null) {
                pathLen++;
                parent = ancestry.get(parent);
            }
            if (pathLen > longest) {
                longest = pathLen;
                deepestLeaf = node;
            }
        }

        LinkedList<Node> longestPath = new LinkedList<>();
        Node parent = deepestLeaf;
        while (parent != null) {
            longestPath.addFirst(parent);
            parent = ancestry.get(parent);
        }
        return longestPath;
    }

    private Node makeTree() {
        /**
         *                7
         *          4           9
         *       1    6     10      12
         *          2   3      5
         *                  11
         *                     13
         */
        Node n7 = new Node(7);
        Node n4 = new Node(4);
        Node n9 = new Node(9);
        Node n6 = new Node(6);
        Node n1 = new Node(1);
        Node n12 = new Node(12);
        Node n10 = new Node(10);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n5 = new Node(5);
        Node n11 = new Node(11);
        Node n13 = new Node(13);

        n7.left = n4;
        n7.right = n9;
        n4.right = n6;
        n4.left = n1;
        n9.right = n12;
        n9.left = n10;
        n6.left = n2;
        n6.right = n3;
        n10.right = n5;
        n5.left = n11;
        n11.right = n13;

        return n7;
    }

    public static final class Node {
        private final int id;
        private Node left;
        private Node right;

        public Node(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
