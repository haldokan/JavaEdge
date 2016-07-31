package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * My solution to an Amazon interview question
 * The Question: 4_STAR
 * <p>
 * customers want to buy some products but products are out of stock, design a system to notify them when those
 * products are again available?
 * <p>
 * Created by haytham.aldokanji on 7/30/16.
 */
public class ProductAvailabilityNotificationSystem {
    private final Emailer emailer;
    private final Map<String, Product> productStore = new HashMap<>();
    private final Map<String, Integer> productStocks = new HashMap<>();
    private final Map<String, Integer> productShortage = new HashMap<>();
    private final SetMultimap<String, Client> productWaitingList = HashMultimap.create();

    public ProductAvailabilityNotificationSystem(Emailer emailer) {
        this.emailer = emailer;
    }

    public void stockProduct(String productId, int amount) {
        productStocks.compute(productId, (k, v) -> v == null ? amount : v + amount);
        Integer shortage = productShortage.computeIfPresent(productId, (k, v) -> v - amount);
        // null shortage indicates that tha product has never been in shortage
        if (shortage != null) {
            // return empty set if there's no waiting list
            Set<Client> waitingList = productWaitingList.get(productId);
            // only of stocks are replenished to meet the shortage we update remove the waiting list and product from
            // the shortage map
            if (shortage <= 0) {
                productWaitingList.removeAll(productId);
                productShortage.remove(productId);
            }
            // notify clients that their products are now available - stocks might not be replenished to the point of
            // meeting all shortage but we can always see that we ran again of stocks. Clients who manage to buy their
            // products before the exhaustion of stocks might get another email of the availability of their product
            // whe we replenish a partially stocked store but that is ok (they may be interested in buying more of the
            // same product!)
            waitingList.forEach(client -> emailer.email(client.getEmail(),
                    productStore.get(productId).getName() + " is available now"));
        }
    }

    public boolean buyProduct(Client client, String productId, int amount) {
        Integer stocks = productStocks.get(productId);
        // product should always have entry in the stocks map so checking for null is not strictly needed
        if (stocks == null || stocks < amount) {
            int shortage = amount - (stocks == null ? 0 : stocks);
            productShortage.compute(productId, (k, v) -> v == null ? shortage : v + shortage);
            productWaitingList.put(productId, client);
            return false; // out of stock
        }
        productStocks.computeIfPresent(productId, (k, v) -> v - amount);
        return true; // congratulations!
    }

    private static final class Client {
        private final String username;
        private final String email;

        public Client(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Client client = (Client) o;
            return username.equals(client.username);
        }

        @Override
        public int hashCode() {
            return username.hashCode();
        }
    }

    private static final class Emailer {
        public void email(String emailAddress, String message) {

        }
    }

    private final class Product {
        private final String id;
        private final String name;

        public Product(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
