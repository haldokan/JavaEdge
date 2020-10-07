package org.haldokan.edge.graph;

import org.junit.Test;

public class GraphTest {
    private Vertex<Integer> v1 = new Vertex<>(1, 0, 0);
    private Vertex<Integer> v2 = new Vertex<>(2, 0, 0);
    private Vertex<Integer> v3 = new Vertex<>(3, 0, 0);
    private Vertex<Integer> v4 = new Vertex<>(4, 0, 0);
    private Vertex<Integer> v5 = new Vertex<>(5, 0, 0);
    private Vertex<Integer> v6 = new Vertex<>(6, 0, 0);

    // we assign coords to 0 since they don't play part in this solution
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
        dfs.traverse(g, v2);
        System.out.println("---");
        dfs.dumpTopoSort();
        System.out.println("---");
        dfs.dumpEntrytime();
        System.out.println("---");
        dfs.dumpExitTime();
    }

    @Test
    public void undirectedGraphCycleTest() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeCycleGraph();
        UndirectedGraphCycleFinder<Integer> dfs = new UndirectedGraphCycleFinder<>();
        boolean cycle = dfs.traverse(g, v2);
        System.out.printf("cycle: %s%n", cycle);
    }

    @Test
    public void directedGraphCycleTest() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeCycleGraph();
        DirectedGraphCycleFinder<Integer> cycleFinder = new DirectedGraphCycleFinder<>(g);
        System.out.printf("isCyclic: %s%n", cycleFinder.isCyclic());
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
        System.out.println(gspath.shortestPathTo(boston));
        System.out.println(gspath.shortestPathTo(miami));
        System.out.println(gspath.shortestPathTo(ny));
        System.out.println(gspath.shortestPathTo(lv));
        System.out.println(gspath.shortestPathTo(la));
    }

    @Test
    public void heapShortestPathTest() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = makeHeapGraph();
        DijkstraGraphSP<Integer> gspath = new DijkstraGraphSP<>();
        gspath.dijkstra(g, Vertex.create(90));

        System.out.println(gspath.shortestPathTo(Vertex.create(50)));
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeGraph1() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(false);
        g.add(v1, v2, new Edge<>(1));
        g.add(v1, v5, new Edge<>(1));
        g.add(v1, v6, new Edge<>(1));
        g.add(v2, v3, new Edge<>(1));
        g.add(v2, v5, new Edge<>(1));
        g.add(v3, v4, new Edge<>(1));
        g.add(v5, v4, new Edge<>(1));
        g.add(v6, v5, new Edge<>(1));

        return g;
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeGraph() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(true);
        g.add(v2, v1, new Edge<>(1));
        g.add(v1, v5, new Edge<>(7));
        g.add(v1, v6, new Edge<>(3));
        g.add(v2, v3, new Edge<>(1));
        g.add(v2, v5, new Edge<>(4));
        g.add(v3, v4, new Edge<>(5));
        g.add(v2, v4, new Edge<>(5));
        g.add(v4, v5, new Edge<>(6));
        g.add(v5, v6, new Edge<>(3));

        return g;
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeCycleGraph() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(true);
        g.add(v2, v1, new Edge<>(1));
        g.add(v1, v5, new Edge<>(7));
        g.add(v1, v6, new Edge<>(3));
        g.add(v2, v3, new Edge<>(1));
        g.add(v2, v5, new Edge<>(4));
        g.add(v3, v4, new Edge<>(5));
        g.add(v2, v4, new Edge<>(5));
        g.add(v4, v5, new Edge<>(6));
        g.add(v5, v6, new Edge<>(3));
        g.add(v6, v2, new Edge<>(8));

        return g;
    }

    private Graph<Vertex<String>, Edge<Vertex<String>>> makeUSACitiesGraph() {
        Graph<Vertex<String>, Edge<Vertex<String>>> g = new Graph<>(false);
        g.add(seattle, sf, new Edge<>(1306));
        g.add(seattle, minneapolis, new Edge<>(2661));
        g.add(seattle, denver, new Edge<>(2161));
        g.add(sf, la, new Edge<>(629));
        g.add(sf, lv, new Edge<>(919));
        g.add(la, lv, new Edge<>(435));
        g.add(lv, denver, new Edge<>(1225));
        g.add(lv, dallas, new Edge<>(1983));
        g.add(denver, dallas, new Edge<>(1258));
        g.add(denver, minneapolis, new Edge<>(1483));
        g.add(minneapolis, chicago, new Edge<>(661));
        g.add(chicago, boston, new Edge<>(1613));
        g.add(chicago, washDC, new Edge<>(1145));
        g.add(dallas, miami, new Edge<>(2161));
        g.add(dallas, washDC, new Edge<>(2113));
        g.add(miami, washDC, new Edge<>(1709));
        g.add(miami, ny, new Edge<>(2145));
        g.add(washDC, ny, new Edge<>(383));
        g.add(washDC, boston, new Edge<>(725));
        g.add(ny, boston, new Edge<>(338));

        return g;
    }

    private Graph<Vertex<Integer>, Edge<Vertex<Integer>>> makeHeapGraph() {
        Graph<Vertex<Integer>, Edge<Vertex<Integer>>> g = new Graph<>(false);
        int[] heap = new int[]{90, 80, 30, 60, 70, 20, 10, 40, 50};
        g.add(Vertex.create(90), Vertex.create(80), new Edge<>(10));
        g.add(Vertex.create(90), Vertex.create(30), new Edge<>(60));
        g.add(Vertex.create(80), Vertex.create(65), new Edge<>(15));
        g.add(Vertex.create(80), Vertex.create(70), new Edge<>(10));
        g.add(Vertex.create(80), Vertex.create(30), new Edge<>(50));
        g.add(Vertex.create(30), Vertex.create(20), new Edge<>(10));
        g.add(Vertex.create(30), Vertex.create(10), new Edge<>(20));
        g.add(Vertex.create(20), Vertex.create(10), new Edge<>(10));
        g.add(Vertex.create(65), Vertex.create(40), new Edge<>(25));
        g.add(Vertex.create(65), Vertex.create(50), new Edge<>(15));
        g.add(Vertex.create(65), Vertex.create(70), new Edge<>(5));
        g.add(Vertex.create(40), Vertex.create(50), new Edge<>(10));

        return g;
    }
}
