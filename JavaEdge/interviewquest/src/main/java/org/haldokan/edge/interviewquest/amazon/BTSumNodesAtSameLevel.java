package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

/**
 * My solution to an Amazon interview question
 * <p>
 * In a Binary tree calculate and return array with a sum of every level.
 * <p>
 * For example,
 * 1
 * 2 3
 * 4 5 1 2
 * <p>
 * Output should be [1, 5, 12].
 */
public class BTSumNodesAtSameLevel {
    public static void main(String[] args) {
        Node root = new Node(7);
        Node n2 = new Node(4);
        Node n3 = new Node(9);
        Node n4 = new Node(6);
        Node n5 = new Node(1);
        Node n6 = new Node(12);
        Node n7 = new Node(10);
        root.left = n2;
        root.right = n3;
        n2.right = n4;
        n2.left = n5;
        n3.right = n6;
        n3.left = n7;

        System.out.println(Arrays.toString(levelsSum(root)));
    }

    public static Integer[] levelsSum(Node tree) {
        Deque<Node> nodes = new ArrayDeque<>();
        Map<Integer, Integer> levelSums = new LinkedHashMap<>();
        Map<Integer, Integer> nodeLevels = new HashMap<>();

        nodes.add(tree);

        nodeLevels.put(tree.val, 0);

        while (!nodes.isEmpty()) {
            Node node = nodes.removeFirst();
            int level = nodeLevels.get(node.val);

            addToLevel(levelSums, node.val, level);

            Node left = node.left;
            Node right = node.right;

            if (left != null) {
                nodes.add(left);
                nodeLevels.put(left.val, level + 1);
            }
            if (right != null) {
                nodes.add(right);
                nodeLevels.put(right.val, level + 1);
            }
        }
//        System.out.println(nodeLevels);
        return levelSums.values().toArray(new Integer[levelSums.size()]);
    }

    private static void addToLevel(Map<Integer, Integer> map, Integer val, Integer level) {
        map.compute(level, (k, v) -> (v == null ? val : v + val));
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int val) {
            this.val = val;
        }

        public boolean leaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }

}
