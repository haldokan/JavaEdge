package org.haldokan.edge.interviewquest.google;

import java.util.*;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - I return the matching nodes which is more interesting than true/false
 * per the question
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
 * Created by haytham.aldokanji on 5/15/16.
 */
public class BTEqualityAtSameLevel {
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
        BTEqualityAtSameLevel driver = new BTEqualityAtSameLevel();
        driver.test();
    }

    public Optional<Node[]> find(Node tree, int x, int y) {
        Deque<Node> nodes = new ArrayDeque<>();
        Map<Node, Integer> nodeLevels = new HashMap<>();

        nodes.add(tree);
        nodeLevels.put(tree, 0);
        NodeMatcher nodeMatcher = new NodeMatcher(x, y);

        while (!nodes.isEmpty()) {
            Node node = nodes.remove();
            Optional<Node[]> match = nodeMatcher.match(node, nodeLevels.get(node));

            if (match.isPresent()) {
                return match;
            }
            int currentLevel = nodeLevels.get(node);
            for (Node child : node.children) {
                nodes.add(child);
                nodeLevels.put(child, currentLevel + 1);
            }
        }
        return Optional.empty();
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

    private void test() {
        Node tree = makeTree();

        Node[] matches = find(tree, 5, 110).get();
        System.out.println(Arrays.toString(matches));
        assertThat(matches, arrayContaining(b, d));

        matches = find(tree, 90, 40).get();
        System.out.println(Arrays.toString(matches));
        assertThat(matches, arrayContaining(e, h));

        matches = find(tree, 45, 120).get();
        System.out.println(Arrays.toString(matches));
        assertThat(matches, arrayContaining(f, i));

        matches = find(tree, 35, 40).get();
        System.out.println(Arrays.toString(matches));
        assertThat(matches, arrayContaining(e, i));

        matches = find(tree, 210, 310).get();
        System.out.println(Arrays.toString(matches));
        assertThat(matches, arrayContaining(j, k));

        Optional<Node[]> potentialMatches = find(tree, 35, 15);
        assertThat(potentialMatches.isPresent(), is(false));

        potentialMatches = find(tree, 5, 15);
        assertThat(potentialMatches.isPresent(), is(false));

        potentialMatches = find(tree, 5, 100);
        assertThat(potentialMatches.isPresent(), is(false));

        potentialMatches = find(tree, 5, 777);
        assertThat(potentialMatches.isPresent(), is(false));

        potentialMatches = find(tree, 999, 777);
        assertThat(potentialMatches.isPresent(), is(false));
    }

    private static class NodeMatcher {
        private final Node searchNode;
        private Node xNode, yNode, xyNode;
        private int currentLevel;

        public NodeMatcher(int x, int y) {
            this.searchNode = new Node(x, y);
            this.currentLevel = -1;
        }

        public Optional<Node[]> match(Node node, int nodeLevel) {
            if (nodeLevel != currentLevel) {
                onLevelChange(node, nodeLevel);
                return Optional.empty();
            }

            Node match = null;
            if (xyNode != null && (node.x == searchNode.x || node.y == searchNode.y)) {
                match = xyNode;
            } else if (xNode != null && node.y == searchNode.y) {
                match = xNode;
            } else if (yNode != null && node.x == searchNode.x) {
                match = yNode;
            }
            if (match != null) {
                return Optional.of(new Node[]{match, node});
            }
            return Optional.empty();
        }

        private void onLevelChange(Node node, int nodeLevel) {
            xNode = yNode = xyNode = null;
            if (node.equals(searchNode)) {
                xyNode = node;
            } else if (node.x == searchNode.x) {
                xNode = node;
            } else if (node.y == searchNode.y) {
                yNode = node;
            }
            if (xNode != null || yNode != null || xyNode != null) {
                currentLevel = nodeLevel;
            }
        }
    }

    private static class Node {
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
