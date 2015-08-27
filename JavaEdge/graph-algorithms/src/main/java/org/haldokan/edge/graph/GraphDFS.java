package org.haldokan.edge.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Depth First Search of graph. DFS uses recursion
 *
 * @param <E>
 * @author haldokan
 */
public class GraphDFS<E> {
    Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    Map<Vertex<E>, Integer> entryTime = new HashMap<>();
    Map<Vertex<E>, Integer> exitTime = new HashMap<>();
    private Map<Vertex<E>, State> vstate = new HashMap<>();
    private Integer time = 0;
    private LinkedList<Vertex<E>> topoSort = new LinkedList<>();

    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> vx) {
        time++;
        vstate.put(vx, State.DISCOVERED);
        entryTime.put(vx, time);
        processVE(vx);
        for (Vertex<E> v : g.getAdjacent1(vx).keySet()) {
            if (vstate.get(v) == null) {
                processE(vx, v, g.getEdge(vx, v));
                parent.put(v, vx);
                traverse(g, v);
            } else if (vstate.get(v) != State.PROCESSED && !parent.get(vx).equals(v) || g.isDirected()) {
                processE(vx, v, g.getEdge(vx, v));
            }
        }
        processVL(vx);
        exitTime.put(vx, time);
        vstate.put(vx, State.PROCESSED);
        topoSort.addFirst(vx);
        time++;

    }

    public void dumpTopoSort() {
        System.out.println("toposort: " + topoSort);
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

    private void processVE(Vertex<E> v) {
        System.out.println("process early: " + v);
    }

    private void processVL(Vertex<E> v) {
        System.out.println("process late: " + v);
    }

    private void processE(Vertex<E> v1, Vertex<E> v2, Edge<Vertex<E>> e) {
        System.out.println(v1.toString() + v2.toString() + e);
    }

    private enum State {
        DISCOVERED, PROCESSED
    }
}
