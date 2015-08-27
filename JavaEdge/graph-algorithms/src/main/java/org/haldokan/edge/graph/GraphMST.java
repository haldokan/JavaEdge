package org.haldokan.edge.graph;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * Prim algorithm for graph Minium Spanning Tree (MST). Greedy algorithm.
 * <p>
 * An interesting take on this algorithm is that it is a flavor of dynamic programming: every step provides enough info
 * for the next step (recurrence relationship). Same goes for Graph shortest path.
 *
 * @param <E>
 * @author haldokan
 */
public class GraphMST<E> {
    Map<Vertex<E>, Edge<Vertex<E>>> vertexMinEdge = new HashMap<>();
    List<Edge<Vertex<E>>> mstree = new ArrayList<>();
    private Set<Vertex<E>> inTree = new HashSet<>();

    public void mst(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> start) {
        Set<Vertex<E>> gvertexes = g.getVertexes();
        inTree.add(start);
        while (!Sets.difference(gvertexes, inTree).isEmpty()) {
            Edge<Vertex<E>> minEdgeForTree = null;
            for (Vertex<E> v : inTree) {
                Edge<Vertex<E>> minEdgeForVertex = vertexMinEdge.get(v);
                if (minEdgeForVertex == null)
                    updateMinEdgeForVertex(g, v);
                // see if update found an outgoing edge from v
                minEdgeForVertex = vertexMinEdge.get(v);
                if (minEdgeForVertex != null) {
                    if (minEdgeForTree == null || minEdgeForTree.getWeight() > minEdgeForVertex.getWeight())
                        minEdgeForTree = minEdgeForVertex;
                }
            }
            if (minEdgeForTree != null) {
                inTree.add(minEdgeForTree.getEndVertex());
                mstree.add(minEdgeForTree);
                // System.out.println("adding: " + minEdgeForTree);
                // System.out.println(inTree);
                reassigneCosts(g, minEdgeForTree.getEndVertex());
            }
        }
        System.out.println("MST: " + mstree);
    }

    private void reassigneCosts(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> iv) {
        // since the entryset is a view that's backed by the map we make a
        // shallow copy to avoid removing items while iterating
        Set<Vertex<E>> vcopy = new HashSet<>();
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : vertexMinEdge.entrySet()) {
            if (entry.getValue().getEndVertex().equals(iv)) {
                vcopy.add(entry.getKey());
            }
        }
        for (Vertex<E> v : vcopy)
            updateMinEdgeForVertex(g, v);
    }

    private void updateMinEdgeForVertex(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> v) {
        // using a heap (priority queue) is not efficient be cool! instead
        // we can simply get an element from the set
        // then iterate over the whole set to get the smallest weight.
        Queue<Edge<Vertex<E>>> pq = new PriorityQueue<>(new Comparator<Edge<Vertex<E>>>() {
            @Override
            public int compare(Edge<Vertex<E>> o1, Edge<Vertex<E>> o2) {
                return o1.getWeight() < o2.getWeight() ? -1 : 1;
            }
        });
        Map<Vertex<E>, Edge<Vertex<E>>> adj = g.getAdjacent1(v);
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : adj.entrySet()) {
            if (!inTree.contains(entry.getKey())) {
                entry.getValue().setDirection(Edge.Direction.D1_2);
                pq.add(entry.getValue());
            }
        }

        adj = g.getAdjacent2(v);
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : adj.entrySet()) {
            if (!inTree.contains(entry.getKey())) {
                entry.getValue().setDirection(Edge.Direction.D2_1);
                pq.add(entry.getValue());
            }
        }

        Edge<Vertex<E>> minEdge = pq.poll();

        if (minEdge != null)
            vertexMinEdge.put(v, minEdge);
        else
            vertexMinEdge.remove(v);
    }
}
