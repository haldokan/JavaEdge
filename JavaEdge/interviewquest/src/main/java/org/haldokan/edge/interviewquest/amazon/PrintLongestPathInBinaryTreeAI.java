package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.List;

/**
 * Print the longest path from root to leaf in a Binary tree (Basically the nodes that lie on the height path).
 * 06/24/2025
 */
public class PrintLongestPathInBinaryTreeAI {
    private List<Integer> longestPath = new ArrayList<>();

    public List<Integer> getLongestRootToLeafPath(TreeNode root) {
        List<Integer> currentPath = new ArrayList<>();
        dfs(root, currentPath);
        return longestPath;
    }

    private void dfs(TreeNode node, List<Integer> currentPath) {
        if (node == null) return;
        currentPath.add(node.val);

        // If it's a leaf node, check if the current path is longer than the max
        if (node.left == null && node.right == null) {
            System.out.println("currentPath: " + currentPath);
            if (currentPath.size() > longestPath.size()) {
                longestPath = new ArrayList<>(currentPath);
                System.out.println("longestPath: " + longestPath);
            }
        } else {
            // Continue DFS
            dfs(node.left, currentPath);
            dfs(node.right, currentPath);
        }

        // Backtrack
        var rmvd = currentPath.removeLast();
        System.out.println("rmvd: " + rmvd);
    }

    // Sample usage
    public static void main(String[] args) {
    /*
                1
              /   \
            2       3
          /   \       \
        4      5        6
       /      / \      / \
      7      8   9    10  11
     /                  \
    12                   13
                        /
                      14
    */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);

        root.left.left.left = new TreeNode(7);
        root.left.left.left.left = new TreeNode(12);

        root.left.right.left = new TreeNode(8);
        root.left.right.right = new TreeNode(9);

        root.right.right.left = new TreeNode(10);
        root.right.right.right = new TreeNode(11);
        root.right.right.left.right = new TreeNode(13);
        root.right.right.left.right.left = new TreeNode(14);

        PrintLongestPathInBinaryTreeAI solution = new PrintLongestPathInBinaryTreeAI();
        List<Integer> path = solution.getLongestRootToLeafPath(root);
        System.out.println("Longest Root-to-Leaf Path: " + path); // Longest Root-to-Leaf Path: [1, 3, 6, 10, 13, 14]
    }

    public static final class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int value) {
            this.val = value;
            this.left = this.right = null;
        }
    }
}
