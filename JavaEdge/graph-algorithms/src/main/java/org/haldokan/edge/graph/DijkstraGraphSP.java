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

    public void dijkstra(Graph<Vertex<E>, Edge<Vertex<E>>> graph, Vertex<E> currentVertex) {
        Set<Vertex<E>> vertexes = graph.getVertexes();

        inTree.put(currentVertex, 0d);
        while (!Sets.difference(vertexes, inTree.keySet()).isEmpty()) {
            Edge<Vertex<E>> minEdgeForTree = null;
            Vertex<E> minEdgeCurrentVertex = null;

            for (Vertex<E> inTreeVertex : inTree.keySet()) {
                Edge<Vertex<E>> minEdgeForVertex = vertexMinEdge.get(inTreeVertex);
                if (minEdgeForVertex == null) {
                    updateMinEdgeForVertex(graph, inTreeVertex);
                }
                // see if update found an outgoing edge from v
                minEdgeForVertex = vertexMinEdge.get(inTreeVertex);

                if (minEdgeForVertex != null) {
                    if (minEdgeForTree == null
                            || minEdgeForTree.getWeight() + inTree.get(minEdgeCurrentVertex) > minEdgeForVertex.getWeight() + inTree.get(inTreeVertex)) {
                        minEdgeForTree = minEdgeForVertex;
                        minEdgeCurrentVertex = inTreeVertex;
                    }
                }
            }
            if (minEdgeForTree != null) {
                inTree.put(minEdgeForTree.getEndVertex(), inTree.get(minEdgeCurrentVertex) + minEdgeForTree.getWeight());
                parent.put(minEdgeForTree.getEndVertex(), minEdgeCurrentVertex);
                reassigneCosts(graph, minEdgeForTree.getEndVertex());
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

    private void updateMinEdgeForVertex(Graph<Vertex<E>, Edge<Vertex<E>>> graph, Vertex<E> vertex) {
        // using a heap (priority queue) is not efficient but cool! instead
        // we can simply get an element from the set
        // then iterate over the whole set to get the smallest weight.
        Queue<Edge<Vertex<E>>> edgesHeap = new PriorityQueue<>((e1, e2) -> e1.getWeight() < e2.getWeight() ? -1 : 1);

        Map<Vertex<E>, Edge<Vertex<E>>> adjacentEdges = graph.getAdjacent1(vertex);
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : adjacentEdges.entrySet()) {
            if (!inTree.containsKey(entry.getKey())) {
                entry.getValue().setDirection(Edge.Direction.D1_2);
                edgesHeap.add(entry.getValue());
            }
        }

        adjacentEdges = graph.getAdjacent2(vertex);
        for (Map.Entry<Vertex<E>, Edge<Vertex<E>>> entry : adjacentEdges.entrySet()) {
            if (!inTree.containsKey(entry.getKey())) {
                entry.getValue().setDirection(Edge.Direction.D2_1);
                edgesHeap.add(entry.getValue());
            }
        }

        Edge<Vertex<E>> minEdge = edgesHeap.peek();

        if (minEdge != null) {
            vertexMinEdge.put(vertex, minEdge);
        } else {
            vertexMinEdge.remove(vertex);
        }
    }
}
