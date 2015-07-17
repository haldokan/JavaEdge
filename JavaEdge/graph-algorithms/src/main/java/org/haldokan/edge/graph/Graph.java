package org.haldokan.edge.graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

public class Graph<V, E extends IEdge<V>> {
    private Table<V, V, E> adjList = HashBasedTable.create();
    private boolean directed;
    private Set<E> edges = new HashSet<>();

    public Graph(boolean directed) {
	this.directed = directed;
    }

    public void add(V v1, V v2, E e) {
	adjList.put(v1, v2, e);
	e.setV1(v1);
	e.setV2(v2);
	edges.add(e);
    }

    public boolean isDirected() {
	return directed;
    }

    public E getEdge(V v1, V v2) {
	return adjList.get(v1, v2);
    }

    public Set<V> getVertexes() {
	return Sets.union(adjList.rowKeySet(), adjList.columnKeySet());
    }

    public Map<V, E> getAdjacent1(V v) {
	return adjList.row(v);
    }

    public Map<V, E> getAdjacent2(V v) {
	return adjList.column(v);
    }

    @Override
    public String toString() {
	return "Graph [adjList=" + adjList + "]";
    }
}
