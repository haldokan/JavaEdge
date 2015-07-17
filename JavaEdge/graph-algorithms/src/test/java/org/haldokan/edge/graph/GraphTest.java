package org.haldokan.edge.graph;

import org.junit.Test;

public class GraphTest {
    private Vertex<Integer> v1 = new Vertex<>(1, 0, 0);
    private Vertex<Integer> v2 = new Vertex<>(2, 0, 0);
    private Vertex<Integer> v3 = new Vertex<>(3, 0, 0);
    private Vertex<Integer> v4 = new Vertex<>(4, 0, 0);
    private Vertex<Integer> v5 = new Vertex<>(5, 0, 0);
    private Vertex<Integer> v6 = new Vertex<>(6, 0, 0);

    // we assigne coords to 0 since they don't play part in this solution
    private Vertex<String> seattle = new Vertex<>("Seattle", 0, 0);
    private Vertex<String> sf = new Vertex<>("San-Francisco", 0, 0);
    private Vertex<String> minneapolis = new Vertex<>("Minneapolis", 0, 0);
    private Vertex<String> denver = new Vertex<>("Denver", 0, 0);
    private Vertex<String> la = new Vertex<>("Los-Angelese", 0, 0);
    private Vertex<String> lv = new Vertex<>("Las-Vegas", 0, 0);
    private Vertex<String> dallas = new Vertex<>("Dallas", 0, 0);
    private Vertex<String> chicago = new Vertex<>("Chicago", 0, 0);
    private Vertex<String> boston = new Vertex<>("Boston", 0, 0);
    private Vertex<String> washDC = new Vertex<>("Washington-DC", 0, 0);
    private Vertex<String> miami = new Vertex<>("Miami", 0, 0);
    private Vertex<String> ny = new Vertex<>("New York", 0, 0);

    @Test
    public void bfsearchTest() {
	Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeGraph1();
	GraphBFS<Integer> bfs = new GraphBFS<>();
	bfs.traverse(g, v1);
    }

    @Test
    public void bfsearchTest2() {
	Graph<Vertex<String>, Edge<Vertex<String>>> g = makeUSACitiesGraph();
	GraphBFS<String> bfs = new GraphBFS<>();
	bfs.traverse(g, seattle);
    }

    @Test
    public void dfsearchTest() {
	Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeGraph();
	GraphDFS<Integer> dfs = new GraphDFS<>();
	dfs.traverse(g, v1);
    }

    @Test
    public void testMST() {
	Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeGraph();
	GraphMST<Integer> gmst = new GraphMST<>();
	gmst.mst(g, v1);
    }

    @Test
    public void testMST2() {
	Graph<Vertex<String>, Edge<Vertex<String>>> g = makeUSACitiesGraph();
	GraphMST<String> gmst = new GraphMST<>();
	gmst.mst(g, seattle);
    }

    @Test
    public void testShortestPath() {
	Graph<Vertex<String>, Edge<Vertex<String>>> g = makeUSACitiesGraph();
	DijkstraGraphSP<String> gspath = new DijkstraGraphSP<>();
	gspath.dijkstra(g, seattle);
	// now we should be able to get shortest path from the start "Seattle"
	// to any other city
	System.out.println(gspath.shortePathTo(boston));
	System.out.println(gspath.shortePathTo(miami));
	System.out.println(gspath.shortePathTo(ny));
	System.out.println(gspath.shortePathTo(lv));
	System.out.println(gspath.shortePathTo(la));
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeGraph1() {
	Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(false);
	g.add(v1, v2, new Edge<Vertex<Integer>>(1));
	g.add(v1, v5, new Edge<Vertex<Integer>>(1));
	g.add(v1, v6, new Edge<Vertex<Integer>>(1));
	g.add(v2, v3, new Edge<Vertex<Integer>>(1));
	g.add(v2, v5, new Edge<Vertex<Integer>>(1));
	g.add(v3, v4, new Edge<Vertex<Integer>>(1));
	g.add(v5, v4, new Edge<Vertex<Integer>>(1));
	g.add(v6, v5, new Edge<Vertex<Integer>>(1));

	return g;
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeGraph() {
	Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(false);
	g.add(v2, v1, new Edge<Vertex<Integer>>(1));
	g.add(v1, v5, new Edge<Vertex<Integer>>(7));
	g.add(v6, v1, new Edge<Vertex<Integer>>(3));
	g.add(v2, v3, new Edge<Vertex<Integer>>(1));
	g.add(v2, v5, new Edge<Vertex<Integer>>(4));
	g.add(v3, v4, new Edge<Vertex<Integer>>(5));
	g.add(v4, v5, new Edge<Vertex<Integer>>(6));
	g.add(v5, v6, new Edge<Vertex<Integer>>(3));

	return g;
    }

    private Graph<Vertex<String>, Edge<Vertex<String>>> makeUSACitiesGraph() {
	Graph<Vertex<String>, Edge<Vertex<String>>> g = new Graph<>(false);
	g.add(seattle, sf, new Edge<Vertex<String>>(1306));
	g.add(seattle, minneapolis, new Edge<Vertex<String>>(2661));
	g.add(seattle, denver, new Edge<Vertex<String>>(2161));
	g.add(sf, la, new Edge<Vertex<String>>(629));
	g.add(sf, lv, new Edge<Vertex<String>>(919));
	g.add(la, lv, new Edge<Vertex<String>>(435));
	g.add(lv, denver, new Edge<Vertex<String>>(1225));
	g.add(lv, dallas, new Edge<Vertex<String>>(1983));
	g.add(denver, dallas, new Edge<Vertex<String>>(1258));
	g.add(denver, minneapolis, new Edge<Vertex<String>>(1483));
	g.add(minneapolis, chicago, new Edge<Vertex<String>>(661));
	g.add(chicago, boston, new Edge<Vertex<String>>(1613));
	g.add(chicago, washDC, new Edge<Vertex<String>>(1145));
	g.add(dallas, miami, new Edge<Vertex<String>>(2161));
	g.add(dallas, washDC, new Edge<Vertex<String>>(2113));
	g.add(miami, washDC, new Edge<Vertex<String>>(1709));
	g.add(miami, ny, new Edge<Vertex<String>>(2145));
	g.add(washDC, ny, new Edge<Vertex<String>>(383));
	g.add(washDC, boston, new Edge<Vertex<String>>(725));
	g.add(ny, boston, new Edge<Vertex<String>>(338));

	return g;
    }
}
