package org.haldokan.edge.graph;

import org.junit.jupiter.api.Test;

import java.util.*;

public class SpanningTreePrimAlgoAI {

    public MSTResult primMST(Map<Integer, List<Edge>> graph, int startVertex) {
        MSTResult result = new MSTResult();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Node> minHeap = new PriorityQueue<>();

        minHeap.add(new Node(startVertex, 0, -1)); // parent = -1 means root

        while (!minHeap.isEmpty()) {
            Node current = minHeap.poll();

            if (visited.contains(current.vertex)) continue;

            visited.add(current.vertex);
            result.totalWeight += current.weight;

            if (current.parent != -1) {
                result.edges.add(new int[]{current.parent, current.vertex, current.weight});
            }

            for (Edge edge : graph.getOrDefault(current.vertex, Collections.emptyList())) {
                if (!visited.contains(edge.to)) {
                    minHeap.add(new Node(edge.to, edge.weight, current.vertex));
                }
            }
        }
        return result;
    }

    public static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    private static class Node implements Comparable<Node> {
        int vertex;
        int weight;
        int parent;

        Node(int vertex, int weight, int parent) {
            this.vertex = vertex;
            this.weight = weight;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    public static class MSTResult {
        int totalWeight;
        List<int[]> edges = new ArrayList<>(); // Each edge is [from, to, weight]
    }

    @Test
    public void testId() {
        //     (0)
        //    /   \
        // 4 /     \ 2
        //  /       \
        //(1)-------(2)
        //  \   5   /
        // 3 \     / 1
        //    \   /
        //     (3)

       //      (0)
        //       \
        //        \ 2
        //         \
        //         (2)
        //           \
        //            \1
        //             \
        //             (3)
        //              |
        //              |3
        //             |
        //           (1)
        Map<Integer, List<Edge>> graph = new HashMap<>();

        graph.computeIfAbsent(0, x -> new ArrayList<>()).add(new Edge(1, 4));
        graph.get(0).add(new Edge(2, 2));
        graph.computeIfAbsent(1, x -> new ArrayList<>()).add(new Edge(0, 4));
        graph.get(1).add(new Edge(2, 5));
        graph.get(1).add(new Edge(3, 3));
        graph.computeIfAbsent(2, x -> new ArrayList<>()).add(new Edge(0, 2));
        graph.get(2).add(new Edge(1, 5));
        graph.get(2).add(new Edge(3, 1));
        graph.computeIfAbsent(3, x -> new ArrayList<>()).add(new Edge(1, 3));
        graph.get(3).add(new Edge(2, 1));

        MSTResult result = primMST(graph, 0);
        System.out.println("Total weight of MST: " + result.totalWeight);
        System.out.println("Edges in MST:");
        for (int[] edge : result.edges) {
            System.out.println(edge[0] + " - " + edge[1] + " (weight: " + edge[2] + ")");
        }
    }
}
