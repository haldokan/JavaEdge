package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - idea is to create a dependency tree and do a DFS. The DFS insures that
 * the children are processed before their parents which enables us to calculate the subtrees weights
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
 * node X includes the own weight of X).
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
public class SubTreeWeight {
    private static final Node ROOT = new Node(0, -1, 0);

    public static void main(String[] args) {
        SubTreeWeight driver = new SubTreeWeight();

        driver.test();
    }

    public Map<Integer, Integer> computeSubTreeWeight(List<Node> nodes) {
        Map<Integer, Node> nodeById = new HashMap<>();
        nodeById.put(0, ROOT);
        nodes.forEach(n -> nodeById.put(n.id, n));

        FamilyTree familyTree = FamilyTree.create();
        nodes.stream()
                .filter(node -> !node.equals(ROOT))
                .forEach(node -> familyTree.add(nodeById.get(node.parent), node));

        return familyTree.sortTopographically(ROOT);

    }

    private void test() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(10, 30, 1));
        nodes.add(new Node(30, 0, 10));
        nodes.add(new Node(7, 0, 11));
        nodes.add(new Node(20, 30, 2));
        nodes.add(new Node(50, 40, 3));
        nodes.add(new Node(40, 30, 4));
        nodes.add(new Node(60, 50, 3));
        nodes.add(new Node(70, 50, 10));
        nodes.add(new Node(80, 10, 2));
        nodes.add(new Node(90, 80, 5));
        nodes.add(new Node(100, 50, 1));

        Map<Integer, Integer> result = computeSubTreeWeight(nodes);
        System.out.printf("%s%n", result);

        assertThat(result, hasEntry(20, 2));
        assertThat(result, hasEntry(70, 10));
        assertThat(result, hasEntry(80, 7));
        assertThat(result, hasEntry(90, 5));
        assertThat(result, hasEntry(100, 1));
        assertThat(result, hasEntry(7, 11));
        assertThat(result, hasEntry(20, 2));
        assertThat(result, hasEntry(10, 8));
        assertThat(result, hasEntry(60, 3));
        assertThat(result, hasEntry(50, 17));
        assertThat(result, hasEntry(40, 21));
        assertThat(result, hasEntry(30, 41));
        assertThat(result, hasEntry(0, 52));
    }

    private static class FamilyTree {
        // edge is not important for our solution so we simply go with a Boolean
        private Table<Node, Node, Boolean> graph = HashBasedTable.create();

        public static FamilyTree create() {
            return new FamilyTree();
        }

        public void add(Node node1, Node node2) {
            graph.put(node1, node2, false);
        }

        public Map<Node, Boolean> getAdjacent(Node v) {
            return graph.row(v);
        }

        public boolean contains(Node node1, Node node2) {
            return graph.contains(node1, node2);
        }

        public Map<Integer, Integer> sortTopographically(Node node) {
            Map<Node, String> traversalState = new HashMap<>();
            Map<Integer, Integer> subTreeWeight = new HashMap<>();

            dfs(node, traversalState, subTreeWeight);
            return subTreeWeight;
        }

        private void dfs(Node node,
                         Map<Node, String> traversalState,
                         Map<Integer, Integer> subTreeWeightMap) {
            traversalState.put(node, "discovered");
            Set<Node> dependencies = getAdjacent(node).keySet();
            dependencies.stream()
                    .filter(dependency -> traversalState.get(dependency) == null)
                    .forEach(dependency -> dfs(dependency, traversalState, subTreeWeightMap));

            traversalState.put(node, "processed");
            subTreeWeightMap.compute(node.id, (k, v) -> v == null ? node.weight : v + node.weight);

            if (!node.equals(ROOT)) {
                int subTreeWeight = subTreeWeightMap.get(node.id);
                subTreeWeightMap.compute(node.parent, (k, v) -> v == null ? subTreeWeight : v + subTreeWeight);
            }
        }

        @Override
        public String toString() {
            return "FamilyTree{" +
                    "graph=" + graph +
                    '}';
        }
    }

    private static class Node {
        private final int id, parent, weight;

        public Node(int id, int parent, int weight) {
            this.id = id;
            this.parent = parent;
            this.weight = weight;
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
            return id;
        }

        @Override
        public String toString() {
            return "(" + id + ", " + parent + ", " + weight + ")";
        }
    }
}
