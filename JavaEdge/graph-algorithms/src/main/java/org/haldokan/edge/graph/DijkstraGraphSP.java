package org.haldokan.edge.graph;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * Dijkstra algorithm for finding shortest paths from a start vertex to all other vertexes. I use Guava Table data
 * structure to represent the graph as an adjacency list.
 * <p>
 * An interesting take on this algorithm is that it is a flavor of dynamic programming: every step provides enough info
 * for the next step (recurrence relationship). Same goes for Graph MST.
 * The Question: 5_STAR
 * @param <E>
 * @author haldokan
 */
public class DijkstraGraphSP<E> {
    private final Map<Vertex<E>, Double> inTree = new HashMap<>();
    private final Map<Vertex<E>, Vertex<E>> parent = new HashMap<>();
    private final Map<Vertex<E>, Edge<Vertex<E>>> vertexMinEdge = new HashMap<>();

    public void dijkstra(Graph<Vertex<E>, Edge<Vertex<E>>> g, Vertex<E> start) {
        Set<Vertex<E>> gvertexes = g.getVertexes();

        inTree.put(start, 0d);
        while (!Sets.difference(gvertexes, inTree.keySet()).isEmpty()) {
            Edge<Vertex<E>> minEdgeForTree = null;
            Vertex<E> minEdgeStartVertex = null;

            for (Vertex<E> v : inTree.keySet()) {
                Edge<Vertex<E>> minEdgeForVertex = vertexMinEdge.get(v);
                if (minEdgeForVertex == null)
                    updateMinEdgeForVertex(g, v);
                // see if update found an outgoing edge from v
                minEdgeForVertex = vertexMinEdge.get(v);

                if (minEdgeForVertex != null) {
                    if (minEdgeForTree == null
                            || minEdgeForTree.getWeight() + inTree.get(minEdgeStartVertex) > minEdgeForVertex
                            .getWeight() + inTree.get(v)) {
                        minEdgeForTree = minEdgeForVertex;
                        minEdgeStartVertex = v;
                    }
                }
            }
            if (minEdgeForTree != null) {
                inTree.put(minEdgeForTree.getEndVertex(), inTree.get(minEdgeStartVertex) + minEdgeForTree.getWeight());
                parent.put(minEdgeForTree.getEndVertex(), minEdgeStartVertex);
                reassigneCosts(g, minEdgeForTree.getEndVertex());
            }
        }
    }

    public List<Vertex<E>> shortestPathTo(Vertex<E> v) {
        Deque<Vertex<E>> path = new LinkedList<>();
        doShortestPathTo(v, path);
        return new ArrayList<>(path);
    }

    private void doShortestPathTo(Vertex<E> v, Deque<Vertex<E>> stack) {
        if (v == null)
            return;
        stack.addFirst(v);
        doShortestPathTo(parent.get(v), stack);
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
            if (!inTree.containsKey(entry.getKey())) {
                entry.getValue().setDirection(Edge.Direction.D1_2);
                pq.add(entry.getValue());
            }
        }

        adj = g.getAdjacent2(v);
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : adj.entrySet()) {
            if (!inTree.containsKey(entry.getKey())) {
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
