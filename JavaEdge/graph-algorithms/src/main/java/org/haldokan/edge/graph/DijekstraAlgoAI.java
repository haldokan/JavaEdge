package org.haldokan.edge.graph;

import java.util.*;

public class DijekstraAlgoAI {
    // Represents an edge in the graph
    public static final class Edge {
        int destination;
        int weight;

        public Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Represents a node with its current shortest distance from the source
    static class NodeDistance implements Comparable<NodeDistance> {
        int node;
        int distance;

        public NodeDistance(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        // Compare based on distance for the PriorityQueue
        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest paths from a source node
     * to all other nodes in a graph with non-negative edge weights.
     *
     * @param graph The graph represented as an adjacency list.
     * Map: Key = source node, Value = List of Edge objects (destination, weight)
     * @param source The starting node for finding shortest paths.
     * @param numVertices The total number of vertices in the graph.
     * @return A Map where keys are destination nodes and values are their shortest distances from the source.
     * Returns an empty map if the source is invalid or graph is empty.
     */
    public Map<Integer, Integer> findShortestPaths(Map<Integer, List<Edge>> graph, int source, int numVertices) {
        // Map to store the shortest distance from the source to each node
        // Initialize all distances to infinity
        Map<Integer, Integer> distances = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(source, 0); // Distance to source itself is 0

        // PriorityQueue to store nodes to visit, ordered by their current shortest distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.add(new NodeDistance(source, 0));

        // Set to keep track of visited nodes (those for which shortest path is finalized)
        Set<Integer> visited = new HashSet<>();

        // Map to reconstruct the actual path (optional)
        // Map<Integer, Integer> predecessors = new HashMap<>();

        while (!pq.isEmpty()) {
            // Get the node with the smallest distance from the priority queue
            NodeDistance current = pq.poll();
            int node = current.node;
            int distance = current.distance;

            // If we've already finalized the shortest path to this node, skip it
            // This is important because a node might be added to PQ multiple times
            // with different distances, but we only care about the first (shortest) one.
            if (visited.contains(node)) {
                continue;
            }

            // Mark the current node as visited (its shortest path is finalized)
            visited.add(node);

            // If the current distance from PQ is greater than already recorded distance, skip.
            // This handles cases where a longer path to 'u' was added to PQ earlier.
            if (distance > distances.get(node)) {
                continue;
            }

            // Iterate over neighbors of the current node 'u'
            // Get the list of edges from 'u'. If 'u' has no edges, graph.get(u) might be null.
            List<Edge> neighbors = graph.getOrDefault(node, Collections.emptyList());

            for (Edge edge : neighbors) {
                int v = edge.destination;
                int weight = edge.weight;

                // Only consider unvisited neighbors
                if (!visited.contains(v)) {
                    // Calculate potential new distance to neighbor 'v'
                    int newDistance = distances.get(node) + weight;

                    // If a shorter path to 'v' is found through 'node'
                    if (newDistance < distances.get(v)) {
                        distances.put(v, newDistance); // Update distance
                        pq.add(new NodeDistance(v, newDistance)); // Add/update in PQ
                        // predecessors.put(v, node); // Store predecessor for path reconstruction
                    }
                }
            }
        }

        // Remove nodes that are unreachable (still have MAX_VALUE distance)
        // Or you can leave them as MAX_VALUE to indicate unreachable
        distances.entrySet().removeIf(entry -> entry.getValue() == Integer.MAX_VALUE);

        return distances;
    }

    // Helper method to build the graph for demonstration
    private static Map<Integer, List<Edge>> buildGraph(int numVertices) {
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            graph.put(i, new ArrayList<>());
        }
        return graph;
    }

    public static void main(String[] args) {
        DijekstraAlgoAI dijkstra = new DijekstraAlgoAI();
        int numVertices = 5; // Nodes 0, 1, 2, 3, 4

        // Example Graph 1 (from a common textbook example)
        // Edges: (0,1,10), (0,2,3), (1,2,1), (1,3,2), (2,1,4), (2,3,8), (2,4,2), (3,4,5), (4,3,7)
        Map<Integer, List<Edge>> graph1 = buildGraph(numVertices);
        graph1.get(0).add(new Edge(1, 10));
        graph1.get(0).add(new Edge(2, 3));
        graph1.get(1).add(new Edge(2, 1));
        graph1.get(1).add(new Edge(3, 2));
        graph1.get(2).add(new Edge(1, 4));
        graph1.get(2).add(new Edge(3, 8));
        graph1.get(2).add(new Edge(4, 2));
        graph1.get(3).add(new Edge(4, 5));
        graph1.get(4).add(new Edge(3, 7));

        int source1 = 0;
        System.out.println("Graph 1 - Shortest paths from source " + source1 + ":");
        Map<Integer, Integer> shortestPaths1 = dijkstra.findShortestPaths(graph1, source1, numVertices);
        shortestPaths1.forEach((node, dist) -> System.out.println("To node " + node + ": " + dist));
        // Expected Output: To node 0: 0, To node 1: 7, To node 2: 3, To node 3: 9, To node 4: 5
        System.out.println("--------------------\n");

        // Example Graph 2 (A simple linear path)
        numVertices = 4; // Nodes 0, 1, 2, 3
        Map<Integer, List<Edge>> graph2 = buildGraph(numVertices);
        graph2.get(0).add(new Edge(1, 5));
        graph2.get(1).add(new Edge(2, 2));
        graph2.get(2).add(new Edge(3, 1));

        int source2 = 0;
        System.out.println("Graph 2 - Shortest paths from source " + source2 + ":");
        Map<Integer, Integer> shortestPaths2 = dijkstra.findShortestPaths(graph2, source2, numVertices);
        shortestPaths2.forEach((node, dist) -> System.out.println("To node " + node + ": " + dist));
        // Expected Output: To node 0: 0, To node 1: 5, To node 2: 7, To node 3: 8
        System.out.println("--------------------\n");

        // Example Graph 3 (Disconnected graph)
        numVertices = 5;
        Map<Integer, List<Edge>> graph3 = buildGraph(numVertices);
        graph3.get(0).add(new Edge(1, 1));
        graph3.get(2).add(new Edge(3, 1)); // 2 and 3 are isolated from 0,1,4
        graph3.get(1).add(new Edge(4, 1));

        int source3 = 0;
        System.out.println("Graph 3 - Shortest paths from source " + source3 + ":");
        Map<Integer, Integer> shortestPaths3 = dijkstra.findShortestPaths(graph3, source3, numVertices);
        shortestPaths3.forEach((node, dist) -> System.out.println("To node " + node + ": " + dist));
        // Expected Output: To node 0: 0, To node 1: 1, To node 4: 2 (node 2 and 3 will be absent or MAX_VALUE)
        System.out.println("--------------------\n");
    }
}