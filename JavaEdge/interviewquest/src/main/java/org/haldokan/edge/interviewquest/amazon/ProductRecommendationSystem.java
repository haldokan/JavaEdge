package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * My solution to an Amazon interview question - a map-reduce type process examines customer purchases and classifies them
 * based on product categories. For each category (so we don't recommend computers when they are buying grocery!)
 * we create a product associativity undirected graph. The weight of the edge that connects 2 product vertices in
 * the graph is proportional to the number of times they appear together in customer purchases. Thus when a customer
 * purchases a product we find the associativity graph for the product category and then find the product vertex and get
 * all its adjacent vertices sorted by the edge weights we return the top N weighted vertices as the most pertinent
 * recommendations for the category of the purchased product.
 * The Question: 5_STAR
 * <p>
 * How will you design the backend of product recommendations (You may also like these carousal) system on amazon.com
 * <p>
 * Created by haytham.aldokanji on 7/21/16.
 */
public class ProductRecommendationSystem {
    private static final Map<String, Product> productCache = new HashMap<>();
    private static final Map<String, Customer> customerCache = new HashMap<>();
    private static final SetMultimap<String, String> purchasesCache = HashMultimap.create();
    private static final Map<String, ProductAssociativityGraph> productAssociativityGraphMap = new HashMap<>();
    private final int maxNumRecommendations;

    public ProductRecommendationSystem(int maxNumRecommendations) {
        this.maxNumRecommendations = maxNumRecommendations;
    }

    public static void main(String[] args) {
        test();
    }

    // batch job (possibly real-time using streaming APIs)
    public static void createProductAssociativityGraphPerCategory() {
        Set<String> customers = purchasesCache.keySet();
        customers.forEach(customer -> {
            List<String> customerPurchases = Lists.newArrayList(purchasesCache.get(customer));
            for (int i = 0; i < customerPurchases.size(); i++) {
                for (int j = i + 1; j < customerPurchases.size(); j++) {
                    Product product1 = productCache.get(customerPurchases.get(i));
                    Product product2 = productCache.get(customerPurchases.get(j));

                    if (product1.category.equals(product2.category)) {
                        ProductAssociativityGraph graph = productAssociativityGraphMap.getOrDefault(product1.category,
                                ProductAssociativityGraph.create());

                        graph.addAssociation(Vertex.create(product1.id), Vertex.create(product2.id), 1);
                        productAssociativityGraphMap.putIfAbsent(product1.category, graph);
                    }
                }
            }
        });
    }

    private static void test() {
        String customerKey = "customer";
        String productKey = "product";

        IntStream.range(0, 5).forEach(index ->
                customerCache.put(customerKey + index, new Customer(customerKey + index, "name" + index)));
        IntStream.range(0, 100).forEach(index ->
                productCache.put(productKey + index, new Product(productKey + index, "category" + (index % 5))));

        Random random = new Random();

        Set<String> productIds = new HashSet<>();
        int activeProductSize = productCache.size() / 10;

        for (int i = 0; i < 1000; i++) {
            Customer customer = customerCache.get(customerKey + random.nextInt(customerCache.size()));

            int randomSuffix = random.nextInt(activeProductSize);
            if (randomSuffix < 3) {
                randomSuffix = random.nextInt(productCache.size());
            }
            Product product = productCache.get(productKey + randomSuffix);
            if (!productIds.contains(product.id)) {
                purchasesCache.put(customer.id, product.id);
                productIds.add(product.id);
            }
        }
        createProductAssociativityGraphPerCategory();

        ProductRecommendationSystem recommendationSystem = new ProductRecommendationSystem(7);
        for (int i = 0; i < activeProductSize * 2; i++) {
            Customer customer = customerCache.get(customerKey + random.nextInt(customerCache.size()));
            Product product = productCache.get(productKey + random.nextInt(activeProductSize));

            List<Product> recommendations = recommendationSystem.purchase(customer, product);
            System.out.printf("%s%n", recommendations);
        }
    }

    // purchase product and return relevant product recommendations
    public List<Product> purchase(Customer customer, Product product) {
        purchasesCache.put(customer.id, product.id);

        ProductAssociativityGraph graph = productAssociativityGraphMap.get(product.category);
        Vertex v = Vertex.create(product.id);
        List<Vertex> associations = graph.getProductAssociations(v);

        int recommendSize = Math.min(associations.size(), maxNumRecommendations);
        return associations.stream()
                .map(vertex -> productCache.get(vertex.productId))
                .limit(recommendSize)
                .collect(Collectors.toList());
    }

    private static class Product {
        private String id;
        private String category;

        public Product(String id, String category) {
            this.id = id;
            this.category = category;
        }

        @Override
        public String toString() {
            return id + "/" + category;
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

        public static ProductAssociativityGraph create() {
            return new ProductAssociativityGraph();
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
            Map<Vertex, Edge> adjacent = Maps.newHashMap(graph.row(v));
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
