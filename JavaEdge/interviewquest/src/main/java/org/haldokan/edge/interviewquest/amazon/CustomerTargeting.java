package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * NOTE: faulty solution in that purchases of the same value can fall into different tranches - Check CustomerTargeting2 for a valid solution
 * My solution to an Amazon interview question - using pure functional programming
 * <p>
 * The Question: 4_STAR
 * Company will start a new marketing campaign targeting the users according
 * to their purchasing profiles.
 * <p>
 * This campaign has 3 kinds of messages each one targeting one group of customers:
 * <p>
 * Message 1 - targets the 25% of customers that spend most at the site
 * <p>
 * Message 2 - targets the 25% of customers that spend least at the site
 * <p>
 * Message 3 - targets the rest of the customers.
 * <p>
 * Given the list of purchases made during the week, write a program that lists
 * what kind of message each customer will receive.
 * <p>
 * Each purchase in this list features the customer id, the purchase amount among other information.
 */
public class CustomerTargeting {

    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("c1", 1.0));
        orders.add(new Order("c1", 2.0));
        orders.add(new Order("c1", 3.0));
        orders.add(new Order("c2", 10.0));
        orders.add(new Order("c2", 20.0));
        orders.add(new Order("c3", 11.0));
        orders.add(new Order("c3", 21.0));
        orders.add(new Order("c3", 3.0));
        orders.add(new Order("c4", 12.0));
        orders.add(new Order("c4", 20.0));
        orders.add(new Order("c5", 1.0));
        orders.add(new Order("c5", 2.0));
        orders.add(new Order("c5", 30.0));
        orders.add(new Order("c6", 10.0));
        orders.add(new Order("c6", 20.0));
        orders.add(new Order("c7", 110.0));
        orders.add(new Order("c7", 21.0));
        orders.add(new Order("c7", 3.0));
        orders.add(new Order("c8", 188.0));
        orders.add(new Order("c8", 20.0));

        System.out.println(customerMessages(orders).entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getKey().getAmount()))
                .collect(Collectors.toList()));
    }


    private static Map<Order, String> customerMessages(List<Order> orders) {
        List<Optional<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer,
                        Collectors.reducing((Order o1, Order o2) ->
                                new Order(o1.getCustomer(), o1.getAmount() + o2.getAmount()))))
                .values()
                .stream().sorted(Comparator.comparing(o -> o.get().getAmount()))
                .collect(Collectors.toList());

        int trancheLen = groupedOrders.size() / 4;

        List<Optional<Order>> tranche1 = groupedOrders.subList(0, trancheLen);
        List<Optional<Order>> tranche2 = groupedOrders.subList(trancheLen, trancheLen * 3);
        List<Optional<Order>> tranche3 = groupedOrders.subList(trancheLen * 3, groupedOrders.size());

        Map<Order, String> custMsgs = customerMessages(tranche1, "Buy more punk!");
        custMsgs.putAll(customerMessages(tranche2, "Good punk but still buy more!"));
        custMsgs.putAll(customerMessages(tranche3, "Awesome punk!"));

        return custMsgs;
    }

    private static Map<Order, String> customerMessages(List<Optional<Order>> orders, String message) {
        return orders.stream().collect(Collectors.toMap(Optional::get, o -> message));
    }

    private static class Order {
        private String customer;
        private Double amount;

        public Order(String customer, Double amount) {
            this.customer = customer;
            this.amount = amount;
        }

        public String getCustomer() {
            return customer;
        }

        public Double getAmount() {
            return amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Order order = (Order) o;

            return !(customer != null ? !customer.equals(order.customer) : order.customer != null);

        }

        @Override
        public int hashCode() {
            return customer != null ? customer.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "customer='" + customer + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
}
