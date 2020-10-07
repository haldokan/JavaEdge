package org.haldokan.edge.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Use DFS to detect cycles in directed graphs. Cycle exist when a graph has a backward edge: An edge that points back to
 * an ancestor vertex in the DFS tree that is not the parent of the edge's start vertex.
 * This implementation is based on the Algorithm presented Algorithms Design Manual by Steven S. Skiena
 *
 * The Question: 4_STAR
 */
public class UndirectedGraphCycleFinder<E> {
    Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    Map<Vertex<E>, Integer> entryTime = new HashMap<>();
    Map<Vertex<E>, Integer> exitTime = new HashMap<>();
    private Map<Vertex<E>, State> visitedState = new HashMap<>();
    private Integer time = 0;

    public boolean traverse(Graph<Vertex<E>, Edge<Vertex<E>>> graph, Vertex<E> currentVertex) {
        time++;
        visitedState.put(currentVertex, State.DISCOVERED);
        parent.put(currentVertex, currentVertex);
        entryTime.put(currentVertex, time);
        // processVertexEarly(vx);
        for (Vertex<E> child : graph.getAdjacent1(currentVertex).keySet()) {
            if (visitedState.get(child) == null) {
                parent.put(child, currentVertex);
                processEdge(currentVertex, child, graph.getEdge(currentVertex, child));
                traverse(graph, child);
            } else if (visitedState.get(child) == State.DISCOVERED && !parent.get(currentVertex).equals(child) || graph.isDirected()) {
                boolean cycle = processEdge(currentVertex, child, graph.getEdge(currentVertex, child));
                if (cycle) {
                    return true; // todo this will not terminate execution and return true to the caller - fix
                }
            }
        }
        // processVertexLate(vx);
        exitTime.put(currentVertex, time);
        visitedState.put(currentVertex, State.PROCESSED);
        return false;
    }

    public void dumpEntrytime() {
        System.out.println("entry time: " + entryTime);
    }

    public void dumpExitTime() {
        System.out.println("exit time: " + exitTime);
    }

    public void dumpParent() {
        System.out.println("parent: " + parent);
    }

    public void dumpState() {
        System.out.println("vx state: " + visitedState);
    }

    private boolean processEdge(Vertex<E> v1, Vertex<E> v2, Edge<Vertex<E>> e) {
        System.out.println("processing edge: " + v1.toString() + v2.toString() + e);
        if (visitedState.get(v2) == State.DISCOVERED && !parent.get(v2).equals(v1)) {
            System.out.println("found cycle: b/w: " + v1.toString() + v2.toString());
            System.out.println("cycle path: " + getCyclePath(v2, v1));
            return true;
        }
        return false;
    }

    //TODO: recurse over the parent map
    private List<Vertex<E>> getCyclePath(Vertex<E> v1, Vertex<E> v2) {
        List<Vertex<E>> path = new LinkedList<>();
        return path;
    }

    private enum State {
        DISCOVERED, PROCESSED
    }
}
