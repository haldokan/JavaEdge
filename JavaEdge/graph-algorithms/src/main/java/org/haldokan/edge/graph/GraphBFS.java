package org.haldokan.edge.graph;

import java.util.*;

/**
 * Breadth First Search of a graph. Using BFS we can construct the tree that represent the shortest path between any 2 vertexes in the graph
 * by following the links from child nodes to their ancestors. The explanation for why that's true is intuitive: a child node that is reachable
 * via multiple paths will link to a parent node that is closest to the start node due to the level-based (breadth) traversal of the graph
 * The Question: 4_STAR
 * @param <E>
 * @author haldokan
 */
public class GraphBFS<E> {
    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> graph, Vertex<E> vertex) {
        Deque<Vertex<E>> undiscovered = new LinkedList<>();
        Map<Vertex<E>, Vertex<E>> parents = new HashMap<>();
        Map<Vertex<E>, State> vertexState = new HashMap<>();

        parents.put(vertex, null);
        processVertex(vertex);
        vertexState.put(vertex, State.PROCESSED);

        for (Vertex<E> child : graph.getAdjacent1(vertex).keySet()) {
            undiscovered.add(child);
            vertexState.put(child, State.UNDISCOVERED);
            parents.put(child, vertex);
            processEdge(vertex, child, graph.getEdge(vertex, child));
        }

        while (!undiscovered.isEmpty()) {
            Vertex<E> currentVertex = undiscovered.removeFirst();
            // System.out.println("removing: " + v.getId());
            vertexState.put(currentVertex, State.DISCOVERED);
            processVertex(currentVertex);
            for (Vertex<E> neighbor : graph.getAdjacent1(currentVertex).keySet()) {
                processEdge(currentVertex, neighbor, graph.getEdge(currentVertex, neighbor));
                if (vertexState.get(neighbor) == null) {
                    // System.out.println("adding: " + e.getV2().getId());
                    undiscovered.add(neighbor);
                    vertexState.put(neighbor, State.UNDISCOVERED);
                    parents.put(neighbor, currentVertex);
                }
            }
            vertexState.put(currentVertex, State.PROCESSED);
        }
        dumpShortestPath(parents);
    }

    // BSF give the shortest path b/w any 2 vertexes provided edges have the
    // same weight: the bfs algorithm does not takes weights into regard when
    // traversing the graph.
    private void dumpShortestPath(Map<Vertex<E>, Vertex<E>> parents) {
        System.out.println("\nShortest paths:");
        for (Vertex<E> v : parents.keySet()) {
            Deque<Vertex<E>> spath = new ArrayDeque<>();
            shortestPathForVertex(spath, parents, v);
            System.out.println(spath);
        }
    }

    private void shortestPathForVertex(Deque<Vertex<E>> spath, Map<Vertex<E>, Vertex<E>> parents, Vertex<E> v) {
        if (parents.get(v) == null) {
            spath.add(v);
            return;
        }
        shortestPathForVertex(spath, parents, parents.get(v));
        spath.add(v);
    }

    private void processVertex(Vertex<E> v) {
        System.out.printf("vertex: V-%s%n", v.getId());
    }

    private void processEdge(Vertex<E> v1, Vertex<E> v2, Edge<Vertex<E>> e) {
        System.out.printf("V:%s, V:%s - edge:%s%n", v1.getId(), v2.getId(), e);
    }

    private enum State {
        DISCOVERED, UNDISCOVERED, PROCESSED
    }
}
