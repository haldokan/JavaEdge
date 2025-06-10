package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - I build the tree adding children based on parents and proceed with a BFS
 * print all weights keep accumulative subtree weights in a map.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Question: You are given a CSV file with 3 columns -- all integers:
 * <p>
 * id,parent,weight
 * 10,30,1
 * 30,0,10
 * 20,30,2
 * 50,40,3
 * 40,30,4
 * <p>
 * 0 is the assumed root node with weight 0
 * <p>
 * which describes a tree-like structure -- each line is a node, 'parent' refers to 'id' of another node.
 * <p>
 * Print out, for each node, the total weight of a subtree below this node (by convention, the weight of a subtree for
 * node X includes own weight of X).
 * <p>
 * You may assume that the input comes pre-parsed as a sequence of Node objects
 * (substitute the appropriate syntax for java/python/c++):
 * <p>
 * Node {
 * int id;
 * int parent;
 * int weight;
 * // ... you can add other fields right here, if necessary
 * }
 * <p>
 * implement the following:
 * public void printSubTreeWeight(List<Node> nodes) {
 * ....}
 * <p>
 * Created by haytham.aldokanji on 6/26/16.
 */
public class SubTreeWeight2 {
    public static void main(String[] args) {
        SubTreeWeight2 driver = new SubTreeWeight2();
        driver.test1();
        driver.test2();
    }

    void test1() {
        Node root = new Node(0, null, 0);
        Node node01 = new Node(1, root, 1);
        Node node02 = new Node(2, root, 2);
        Node node03 = new Node(3, root, 3);
        root.addChild(node01);
        root.addChild(node02);
        root.addChild(node03);

        Node node11 = new Node(11, node01, 11);
        Node node12 = new Node(12, node01, 12);
        Node node13 = new Node(13, node01, 13);
        node01.addChild(node11);
        node01.addChild(node12);
        node01.addChild(node13);

        Node node31 = new Node(31, node03, 31);
        Node node32 = new Node(32, node03, 32);
        Node node33 = new Node(33, node03, 33);
        node03.addChild(node31);
        node03.addChild(node32);
        node03.addChild(node33);

        Node node5 = new Node(5, node13, 5);
        Node node6 = new Node(6, node33, 6);
        Node node7 = new Node(7, node02, 7);
        node13.addChild(node5);
        node33.addChild(node6);
        node02.addChild(node7);
        doPrint(root);
    }

    void test2() {
        Node root = new Node(0, null, 0);
        Node node01 = new Node(1, root, 1);
        Node node02 = new Node(2, root, 2);
        Node node03 = new Node(3, root, 3);

        Node node11 = new Node(11, node01, 11);
        Node node12 = new Node(12, node01, 12);
        Node node13 = new Node(13, node01, 13);

        Node node31 = new Node(31, node03, 31);
        Node node32 = new Node(32, node03, 32);
        Node node33 = new Node(33, node03, 33);

        Node node5 = new Node(5, node13, 5);
        Node node6 = new Node(6, node33, 6);
        Node node7 = new Node(7, node02, 7);

        print(Lists.newArrayList(root, node01, node02, node03, node11, node12, node13, node31, node32, node33, node5, node6, node7));
    }

    Node buildTree(List<Node> nodes) {
        Multimap<Node, Node> ancestry = ArrayListMultimap.create();
        Node root = null;
        for (Node node : nodes) {
            if (node.parent == null) {
                root = node;
            } else {
                ancestry.put(node.parent, node);
            }
        }
        for (Node node : ancestry.keySet()) {
            Collection<Node> children = ancestry.get(node);
            for (Node child : children) {
                node.addChild(child);
            }
        }
        return root;
    }

    void print(List<Node> nodes) {
        doPrint(buildTree(nodes));
    }

    void doPrint(Node node) {
        Deque<Node> deck = new ArrayDeque<>();
        deck.addLast(node);
        Map<Node, Integer> nodeWeights = new HashMap<>();

        while (!deck.isEmpty()) {
            Node current = deck.removeFirst();
            nodeWeights.put(current, current.weight);

            for (Node child : current.children) {
                deck.addLast(child);
            }

            Node ancestor = current.parent;
            while (ancestor != null) {
                nodeWeights.computeIfPresent(ancestor, (k, v) -> v + current.weight);
                ancestor = ancestor.parent;
            }
        }
        System.out.println(nodeWeights);
    }

    static class Node {
        int id;
        Node parent;
        int weight;
        List<Node> children;

        public Node(int id, Node parent, int weight) {
            this.id = id;
            this.parent = parent;
            this.weight = weight;
            this.children = new ArrayList<>();
        }

        void addChild(Node node) {
            children.add(node);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return id == node.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return String.format("%d", id);
        }
    }
}
