package org.haldokan.edge.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapted from this geeks4geeks post: https://www.geeksforgeeks.org/detect-cycle-in-a-graph/?ref=rp
 * I generified the implementation and used better data-structures
 *
 * Note that this algorithm/implementation bales out once a cycle is found. The graph can still have many other cycles
 *
 * The Question: 4.5-STAR
 *
 * 10/07/20
 */
public class DirectedGraphCycleFinder<E> {
    private final Graph<Vertex<E>, Edge<Vertex<E>>> graph;

    public DirectedGraphCycleFinder(Graph<Vertex<E>, Edge<Vertex<E>>> graph) {
        this.graph = graph;
    }

    public boolean isCyclic() {
        // we check for cycles starting at every vertex because in a directed graph the edges we traverse depend on the
        // start vertex. Also this takes care of disconnected directed graphs.
        for (Vertex<E> vertex : graph.getVertexes()) {
            if (isCyclic(vertex, new HashSet<>(), new HashSet<>())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCyclic(Vertex<E> vertex, Set<Vertex<E>> visited, Set<Vertex<E>> recursionStack) {
        if (recursionStack.contains(vertex)) {
            System.out.printf("in recursionStack: %s%n", vertex);
            return true;
        }
        if (visited.contains(vertex)) {
            return false;
        }
        visited.add(vertex);
        recursionStack.add(vertex);

        Set<Vertex<E>> children = graph.getAdjacent1(vertex).keySet();
        for (Vertex<E> child: children) {
            if (isCyclic(child, visited, recursionStack)) {
                System.out.printf("cycle with child: %s-%s%n", vertex, child);
                return true;
            }
        }
        recursionStack.remove(vertex);
        return false;
    }
}
