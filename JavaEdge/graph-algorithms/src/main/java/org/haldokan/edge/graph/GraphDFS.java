package org.haldokan.edge.graph;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Depth First Search of graph (DFS). Used usually with directed acyclical graphs (DAGs)
 * sample applications:
 * Loading interdependent libs in an app insuring a lib is loaded only when all its dependencies did
 * Topographical sorting: traverse a DAG from start to end in the reverse order of dependencies
 * The Question: 4_STAR
 * @param <E>
 * @author haldokan
 */
public class GraphDFS<E> {
    Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    Map<Vertex<E>, Integer> entryTime = new HashMap<>();
    Map<Vertex<E>, Integer> exitTime = new HashMap<>();
    private Map<Vertex<E>, State> vstate = new HashMap<>();
    private Integer time = 0;
    private Deque<Vertex<E>> topoSort = new LinkedList<>();

    public void traverse(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> currentVertex) {
        time++;
        vstate.put(currentVertex, State.DISCOVERED);
        entryTime.put(currentVertex, time);
        processVE(currentVertex);
        for (Vertex<E> child : g.getAdjacent1(currentVertex).keySet()) {
            if (vstate.get(child) == null) {
                processE(currentVertex, child, g.getEdge(currentVertex, child));
                parent.put(child, currentVertex);
                traverse(g, child);
            } else if (vstate.get(child) == State.DISCOVERED && !parent.get(currentVertex).equals(child) || g.isDirected()) {
                processE(currentVertex, child, g.getEdge(currentVertex, child));
            }
        }
        processVL(currentVertex);
        exitTime.put(currentVertex, time);
        vstate.put(currentVertex, State.PROCESSED);
        topoSort.addFirst(currentVertex);
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
