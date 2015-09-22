package org.haldokan.edge.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Use DFS to detect cycles in directed graphs. Cycle exist when a graph has a backward edge: An edge that points back to
 * an ancestor vertex in the DFS tree that is not the parent of the edge's start vertex.
 *
 * @param <E>
 * @author haldokan
 */
public class DirectedGraphCycleFinder<E> {
    Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    Map<Vertex<E>, Integer> entryTime = new HashMap<>();
    Map<Vertex<E>, Integer> exitTime = new HashMap<>();
    private Map<Vertex<E>, State> vstate = new HashMap<>();
    private Integer time = 0;

    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> vx) {
        time++;
        vstate.put(vx, State.DISCOVERED);
        entryTime.put(vx, time);
        // processVertexEarly(vx);
        for (Vertex<E> v : g.getAdjacent1(vx).keySet()) {
            if (vstate.get(v) == null) {
                parent.put(v, vx);
                processEdge(vx, v, g.getEdge(vx, v));
                traverse(g, v);
            } else if (vstate.get(v) != State.PROCESSED && !parent.get(vx).equals(v) || g.isDirected()) {
                processEdge(vx, v, g.getEdge(vx, v));
            }
        }
        // processVertexLate(vx);
        exitTime.put(vx, time);
        vstate.put(vx, State.PROCESSED);
        time++;

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
        if (vstate.get(v2) == State.DISCOVERED && !parent.get(v1).equals(v2)) {
            System.out.println("found cycle: b/w: " + v2.toString() + v1.toString());
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
