package org.haldokan.edge.interviewquest.google;

import java.util.*;

/**
 * My solution to a Google interview question - another implementation that returns true/false using a different way to insure
 * that 2 nodes match the search
 *
 * The Question: 4-STAR
 *
 * Given an arbitrary tree starting at “root” where each node contains a pair of values (x, y),
 * write a boolean function find(Node root, int x, int y) that returns true iff
 * 1- x is equal to a value "x" of any node n1 in the tree
 * 2- and y is equal to a value "y" of any node n2 in the tree
 * 3- and both n1 and n2 are at the same level in the tree
 * <p>
 * boolean find(Node root, int x, int y)
 * <p>
 * Example:
 * <p>
 * (1,120)
 * / | \
 * / | \
 * / | \
 * (5,15) (30,70) (80,110)
 * / | \ |
 * / | \ |
 * / | \ |
 * (35, 40) (45,50) (55, 65) (90, 100)
 * <p>
 * boo == true
 * find(root, 45, 100) == true
 * find(root, 30, 100) == false
 * find(root, 30, 70) == true
 * find(root, 70, 30) == false
 * Created by haytham.aldokanji on 7/6/20.
 */
public class BTEqualityAtSameLevel2 {
    private static final Node a = new Node(1, 2);
    private static final Node b = new Node(5, 15);
    private static final Node c = new Node(30, 70);
    private static final Node d = new Node(80, 110);
    private static final Node e = new Node(35, 40);
    private static final Node f = new Node(45, 50);
    private static final Node g = new Node(55, 65);
    private static final Node h = new Node(90, 100);
    private static final Node i = new Node(35, 120);
    private static final Node j = new Node(210, 310);
    private static final Node k = new Node(210, 310);

    public static void main(String[] args) {
        BTEqualityAtSameLevel2 driver = new BTEqualityAtSameLevel2();
        Node tree = driver.makeTree();
        System.out.println(driver.found(tree, 5, 110));
        System.out.println(driver.found(tree, 90, 40));
        System.out.println(driver.found(tree, 45, 120));
        System.out.println(driver.found(tree, 35, 40));
        System.out.println(driver.found(tree, 5, 15));
        System.out.println(driver.found(tree, 210, 310));
    }

    boolean found(Node tree, int x, int y) {
        Map<Node, Integer> nodeLevel = new HashMap<>();
        List<Node> queue = new LinkedList<>();

        nodeLevel.put(tree, 0);
        queue.add(tree);

        int time = 0;
        List<Integer> xTimes = new ArrayList<>();
        List<Integer> yTimes = new ArrayList<>();

        int level = 0;
        while (!queue.isEmpty()) {
            time++;
            Node node = queue.remove(0);
            int currentLevel = nodeLevel.get(node);
            if (currentLevel != level) {
                level = currentLevel;
                time = 0;
                xTimes.clear();;
                yTimes.clear();
            }

            if (node.x == x) {
                xTimes.add(time);
            }
            if (node.y == y) {
                yTimes.add(time);
            }
            // bit tricky to figure this out
            if (!(xTimes.isEmpty() || yTimes.isEmpty())) {
                if (!xTimes.get(xTimes.size() - 1).equals(yTimes.get(yTimes.size() - 1))) {
                    return true;
                } else if (xTimes.size() > 1 && yTimes.size() > 1) {
                    return true;
                }
            }

            for (Node child : node.children) {
                queue.add(child);
                nodeLevel.put(child, nodeLevel.get(node) + 1);
            }
        }
        return false;
    }


    private Node makeTree() {
        a.add(b);
        a.add(c);
        a.add(d);

        c.add(e);
        c.add(f);
        c.add(g);

        d.add(h);
        d.add(i);

        e.add(j);
        g.add(k);
        return a;
    }

    static class Node {
        int x, y;
        List<Node> children;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.children = new ArrayList<>();
        }

        public void add(Node val) {
            children.add(val);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return x == node.x && y == node.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
