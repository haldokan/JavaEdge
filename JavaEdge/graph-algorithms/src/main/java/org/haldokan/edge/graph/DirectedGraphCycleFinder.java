package org.haldokan.edge.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * NOTE: the algorithm has a problem and detects more cycles than exist in the graph
 * Use DFS to detect cycles in directed graphs. Cycle exist when a graph has a backward edge: An edge that points back to
 * an ancestor vertex in the DFS tree that is not the parent of the edge's start vertex.
 * The Question: 4_STAR
 * @param <E>
 * @author haldokan
 */
public class DirectedGraphCycleFinder<E> {
    Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    Map<Vertex<E>, Integer> entryTime = new HashMap<>();
    Map<Vertex<E>, Integer> exitTime = new HashMap<>();
    private Map<Vertex<E>, State> vstate = new HashMap<>();
    private Integer time = 0;

    public boolean traverse(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> currentVertex) {
        time++;
        vstate.put(currentVertex, State.DISCOVERED);
        parent.put(currentVertex, currentVertex);
        entryTime.put(currentVertex, time);
        // processVertexEarly(vx);
        for (Vertex<E> child : g.getAdjacent1(currentVertex).keySet()) {
            if (vstate.get(child) == null) {
                parent.put(child, currentVertex);
                processEdge(currentVertex, child, g.getEdge(currentVertex, child));
                traverse(g, child);
            } else if (vstate.get(child) == State.DISCOVERED && !parent.get(currentVertex).equals(child) || g.isDirected()) {
                boolean cycle = processEdge(currentVertex, child, g.getEdge(currentVertex, child));
                if (cycle) {
                    return true; // todo this will not terminate execution and return true to the caller - fix
                }
            }
        }
        // processVertexLate(vx);
        exitTime.put(currentVertex, time);
        vstate.put(currentVertex, State.PROCESSED);
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
        System.out.println("vx state: " + vstate);
    }

    private boolean processEdge(Vertex<E> v1, Vertex<E> v2, Edge<Vertex<E>> e) {
        System.out.println("processing edge: " + v1.toString() + v2.toString() + e);
        if (vstate.get(v2) == State.DISCOVERED && !parent.get(v2).equals(v1)) {
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
