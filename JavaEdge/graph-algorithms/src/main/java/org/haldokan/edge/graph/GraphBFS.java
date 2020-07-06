package org.haldokan.edge.graph;

import java.util.*;

/**
 * Breadth First Search of a graph. Iteration (not recursion) is used for BFS.
 * The Question: 4_STAR
 * @param <E>
 * @author haldokan
 */
public class GraphBFS<E> {
    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> graph, Vertex<E> start) {
        Deque<Vertex<E>> undiscovered = new LinkedList<>();
        Map<Vertex<E>, Vertex<E>> parents = new HashMap<>();
        Map<Vertex<E>, State> vertexState = new HashMap<>();

        parents.put(start, null);
        processVertex(start);
        vertexState.put(start, State.PROCESSED);

        for (Vertex<E> vertex : graph.getAdjacent1(start).keySet()) {
            undiscovered.add(vertex);
            vertexState.put(vertex, State.UNDISCOVERED);
            parents.put(vertex, start);
            processEdge(start, vertex, graph.getEdge(start, vertex));
        }

        while (!undiscovered.isEmpty()) {
            Vertex<E> vertext = undiscovered.removeFirst();
            // System.out.println("removing: " + v.getId());
            vertexState.put(vertext, State.DISCOVERED);
            processVertex(vertext);
            for (Vertex<E> v : graph.getAdjacent1(vertext).keySet()) {
                processEdge(vertext, v, graph.getEdge(vertext, v));
                if (vertexState.get(v) != null) {
                    // System.out.println("adding: " + e.getV2().getId());
                    undiscovered.add(v);
                    vertexState.put(v, State.UNDISCOVERED);
                    parents.put(v, vertext);
                }
            }
            vertexState.put(vertext, State.PROCESSED);
        }
        dumpShortestPath(parents);
    }

    // BSF give the shortest path b/w any 2 vertexes provided edges have the
    // same weight: the bfs algo does not takes weights into regard when
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
        System.out.println("processing V: " + v.getId());
    }

    private void processEdge(Vertex<E> v1, Vertex<E> v2, Edge<Vertex<E>> e) {
        System.out.println(v1.toString() + v2.toString() + e);
    }

    private enum State {
        DISCOVERED, UNDISCOVERED, PROCESSED
    }
}
