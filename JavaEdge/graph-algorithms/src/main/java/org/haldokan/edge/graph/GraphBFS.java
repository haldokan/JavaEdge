package org.haldokan.edge.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Breadth Frist Search of a graph. Iteration (not recursion) is used for BFS.
 *
 * @param <E>
 * @author haldokan
 */
public class GraphBFS<E> {
    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> start) {
        LinkedList<Vertex<E>> undiscovered = new LinkedList<>();
        Map<Vertex<E>, Vertex<E>> parents = new HashMap<>();
        Map<Vertex<E>, State> vstate = new HashMap<>();

        parents.put(start, null);
        vstate.put(start, State.PROCESSED);
        processVertex(start);

        for (Vertex<E> v : g.getAdjacent1(start).keySet()) {
            undiscovered.add(v);
            vstate.put(v, State.UNDISCOVERED);
            parents.put(v, start);
            processEdge(start, v, g.getEdge(start, v));
        }

        while (!undiscovered.isEmpty()) {
            Vertex<E> vx = undiscovered.removeFirst();
            // System.out.println("removing: " + v.getId());
            vstate.put(vx, State.DISCOVERED);
            processVertex(vx);
            for (Vertex<E> v : g.getAdjacent1(vx).keySet()) {
                processEdge(vx, v, g.getEdge(vx, v));
                if (vstate.get(v) != State.UNDISCOVERED) {
                    // System.out.println("adding: " + e.getV2().getId());
                    undiscovered.add(v);
                    vstate.put(v, State.UNDISCOVERED);
                    parents.put(v, vx);
                }
            }
            vstate.put(vx, State.PROCESSED);
        }
        dumpShortestPath(parents);
    }

    ;

    // BSF give the shortest path b/w any 2 vertexes provided edges have the
    // same weight: the bfs algo does not takes weights into regard when
    // traversing the graph.
    private void dumpShortestPath(Map<Vertex<E>, Vertex<E>> parents) {
        System.out.println("\nShortest paths:");
        for (Vertex<E> v : parents.keySet()) {
            LinkedList<Vertex<E>> spath = new LinkedList<>();
            shortestPathForVertex(spath, parents, v);
            System.out.println(spath);
        }
    }

    private void shortestPathForVertex(LinkedList<Vertex<E>> spath, Map<Vertex<E>, Vertex<E>> parents, Vertex<E> v) {
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