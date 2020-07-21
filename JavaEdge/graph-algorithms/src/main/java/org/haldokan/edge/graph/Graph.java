package org.haldokan.edge.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Guava Table is use to represent the graph as an adjacency list
 *
 * @param <V>
 * @param <E>
 * @author haldokan
 */
public class Graph<V, E extends IEdge<V>> {
    private Table<V, V, E> graph = HashBasedTable.create();
    private boolean directed;
    private Set<E> edges = new HashSet<>();

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public void add(V v1, V v2, E e) {
        graph.put(v1, v2, e);
        e.setV1(v1);
        e.setV2(v2);
        edges.add(e);
    }

    public boolean isDirected() {
        return directed;
    }

    public E getEdge(V v1, V v2) {
        return graph.get(v1, v2);
    }

    public Set<V> getVertexes() {
        return Sets.union(graph.rowKeySet(), graph.columnKeySet());
    }

    public Map<V, E> getAdjacent1(V v) {
        return graph.row(v);
    }

    public Map<V, E> getAdjacent2(V v) {
        return graph.column(v);
    }

    @Override
    public String toString() {
        return graph.toString();
    }
}
