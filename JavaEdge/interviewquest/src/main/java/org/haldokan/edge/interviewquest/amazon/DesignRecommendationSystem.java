package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question
 * The Question: 5_STAR
 * <p>
 * How will you design the backend of product recommendations (You may also like these carousal) system on amazon.com
 * <p>
 * Created by haytham.aldokanji on 7/21/16.
 */
public class DesignRecommendationSystem {
    private static final Map<String, Product> products = new HashMap<>();
    private static final Map<String, Customer> customers = new HashMap<>();
    private final ListMultimap<String, String> purchases = ArrayListMultimap.create();
    private final int maxNumRecommendations;
    private Map<String, ProductAssociativityGraph> productAssociativityGraphMap;


    public DesignRecommendationSystem(int maxNumRecommendations) {
        this.maxNumRecommendations = maxNumRecommendations;
    }

    public List<Product> purchase(Customer customer, Product product) {
        purchases.put(customer.id, product.id);

        ProductAssociativityGraph graph = productAssociativityGraphMap.get(product.category);
        Vertex v = Vertex.create(product.id);
        List<Vertex> associations = graph.getProductAssociations(v);

        int recommendSize = Math.min(associations.size(), maxNumRecommendations);
        return associations.stream()
                .map(vertex -> products.get(vertex.productId))
                .limit(recommendSize)
                .collect(Collectors.toList());
    }

    // batch job (possibly real-time using streaming APIs)
    public void createProductAssociativityGraphPerCategory() {
        Set<String> customers = purchases.keySet();
        customers.forEach(customer -> {
            List<String> customerPurchases = purchases.get(customer);
            for (int i = 0; i < customerPurchases.size(); i++) {
                for (int j = i + 1; j < customerPurchases.size(); j++) {
                    Product product1 = products.get(customerPurchases.get(i));
                    Product product2 = products.get(customerPurchases.get(j));

                    if (product1.category.equals(product2.category)) {
                        ProductAssociativityGraph graph = productAssociativityGraphMap.getOrDefault(product1.category,
                                new ProductAssociativityGraph(product1.category));

                        graph.addAssociation(Vertex.create(product1.id), Vertex.create(product2.id), 1);
                        productAssociativityGraphMap.putIfAbsent(product1.category, graph);
                    }
                }
            }
        });
    }

    private static class Product {
        private String id;
        private String category;

        public Product(String id, String category) {
            this.id = id;
            this.category = category;
        }
    }

    private static class Customer {
        private String id;
        private String name;

        public Customer(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class ProductAssociativityGraph {
        private final Table<Vertex, Vertex, Edge> graph = HashBasedTable.create();
        private final String productCategory;

        public ProductAssociativityGraph(String productCategory) {
            this.productCategory = productCategory;
        }

        public static ProductAssociativityGraph create(String productCategory) {
            return new ProductAssociativityGraph(productCategory);
        }

        public void addAssociation(Vertex v1, Vertex v2, int edgeWeight) {
            if (containsAssociation(v1, v2)) {
                Edge edge = getAssociationWeight(v1, v2);
                edge.addWeight(edgeWeight);
            } else {
                graph.put(v1, v2, new Edge(edgeWeight));
            }
        }

        public Edge getAssociationWeight(Vertex v1, Vertex v2) {
            Edge edge = graph.get(v1, v2);
            if (edge == null) {
                edge = graph.get(v2, v1);
            }
            return edge;
        }

        public List<Vertex> getProductAssociations(Vertex v) {
            Map<Vertex, Edge> adjacent = graph.row(v);
            adjacent.putAll(graph.column(v));

            return adjacent.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().weight - e1.getValue().weight)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        public boolean containsAssociation(Vertex v1, Vertex v2) {
            return graph.contains(v1, v2) || graph.contains(v2, v1);
        }

        public void clear() {
            graph.clear();
        }

        @Override
        public String toString() {
            return "ProductAssociativityGraph{" +
                    "graph=" + graph +
                    '}';
        }
    }

    private static class Edge {
        private int weight;

        private Edge(int weight) {
            this.weight = weight;
        }

        public static Edge create(int weight) {
            return new Edge(weight);
        }

        public void addWeight(int amount) {
            this.weight += amount;
        }

        @Override
        public String toString() {
            return String.valueOf(weight);
        }
    }

    private static class Vertex {
        private final String productId;

        public Vertex(String productId) {
            this.productId = productId;
        }

        public static Vertex create(String productId) {
            return new Vertex(productId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return productId.equals(vertex.productId);
        }

        @Override
        public int hashCode() {
            return productId.hashCode();
        }

        @Override
        public String toString() {
            return String.valueOf(productId);
        }
    }
}
