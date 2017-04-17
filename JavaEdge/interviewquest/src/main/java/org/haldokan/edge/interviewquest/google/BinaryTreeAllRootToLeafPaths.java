package org.haldokan.edge.interviewquest.google;

import java.util.*;

/**
 * My solution to a Google interview question - Solved it using BFS with node ancestry map.
 * I wonder if there is a recursion-based solution
 *
 * The Question: 4_STAR
 *
 * Given a binary tree & the following TreeNode definition, return all root-to-leaf paths.
 * <p>
 * Definition of TreeNode:
 * <p>
 * public class TreeNode {
 * public int val;
 * public TreeNode left, right;
 * public TreeNode(int val) {
 * this.val = val;
 * this.left = this.right = null;
 * }
 * }
 * EXAMPLE
 * <p>
 * Given the following binary tree:
 * 1
 * /   \
 * 2     3
 * \
 * 5
 * <p>
 * All root-to-leaf paths are:
 * [ "1->2->5", "1->3" ]
 * From Lint Code - http://www.lintcode.com/en/problem/binary-tree-paths/
 * <p>
 * Created by haldokanji on 12/7/16.
 */
public class BinaryTreeAllRootToLeafPaths {

    public static void main(String[] args) {
        BinaryTreeAllRootToLeafPaths driver = new BinaryTreeAllRootToLeafPaths();
        System.out.println(driver.allPaths(driver.makeTree()));
    }

    private List<List<Integer>> allPaths(BinaryTreeAllRootToLeafPaths.Node tree) {
        Map<Integer, Integer> ancestry = new HashMap<>();
        Deque<BinaryTreeAllRootToLeafPaths.Node> evalDeck = new ArrayDeque<>();
        List<List<Integer>> allPaths = new ArrayList<>();

        evalDeck.add(tree);
        ancestry.put(tree.id, null);

        while (!evalDeck.isEmpty()) {
            BinaryTreeAllRootToLeafPaths.Node parent = evalDeck.remove();
            BinaryTreeAllRootToLeafPaths.Node[] children = new BinaryTreeAllRootToLeafPaths.Node[]{parent.left, parent.right};

            if (children[0] == null && children[1] == null) {
                allPaths.add(makePath(ancestry, parent.id));
            } else {
                for (BinaryTreeAllRootToLeafPaths.Node child : children) {
                    if (child != null) {
                        evalDeck.add(child);
                        ancestry.put(child.id, parent.id);
                    }
                }
            }
        }
        return allPaths;
    }

    private List<Integer> makePath(Map<Integer, Integer> ancestry, Integer nodeId) {
        List<Integer> path = new LinkedList<>();
        path.add(0, nodeId);
        Integer parent = ancestry.get(nodeId);

        while (parent != null) {
            path.add(0, parent);
            parent = ancestry.get(parent);
        }
        return path;
    }

    private BinaryTreeAllRootToLeafPaths.Node makeTree() {
        /**
         *                7
         *          4           9
         *       1    6     10      12
         *          2   3      5
         *                  11
         *                     13
         */
        BinaryTreeAllRootToLeafPaths.Node n7 = new BinaryTreeAllRootToLeafPaths.Node(7);
        BinaryTreeAllRootToLeafPaths.Node n4 = new BinaryTreeAllRootToLeafPaths.Node(4);
        BinaryTreeAllRootToLeafPaths.Node n9 = new BinaryTreeAllRootToLeafPaths.Node(9);
        BinaryTreeAllRootToLeafPaths.Node n6 = new BinaryTreeAllRootToLeafPaths.Node(6);
        BinaryTreeAllRootToLeafPaths.Node n1 = new BinaryTreeAllRootToLeafPaths.Node(1);
        BinaryTreeAllRootToLeafPaths.Node n12 = new BinaryTreeAllRootToLeafPaths.Node(12);
        BinaryTreeAllRootToLeafPaths.Node n10 = new BinaryTreeAllRootToLeafPaths.Node(10);
        BinaryTreeAllRootToLeafPaths.Node n2 = new BinaryTreeAllRootToLeafPaths.Node(2);
        BinaryTreeAllRootToLeafPaths.Node n3 = new BinaryTreeAllRootToLeafPaths.Node(3);
        BinaryTreeAllRootToLeafPaths.Node n5 = new BinaryTreeAllRootToLeafPaths.Node(5);
        BinaryTreeAllRootToLeafPaths.Node n11 = new BinaryTreeAllRootToLeafPaths.Node(11);
        BinaryTreeAllRootToLeafPaths.Node n13 = new BinaryTreeAllRootToLeafPaths.Node(13);

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

    private static class Node {
        private int id;
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
