package org.haldokan.edge.interviewquest.amazon;

import com.google.inject.internal.cglib.core.$Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Design a vending machine with following functionalities
 * Three types of users : [User, Operator, Admin]
 * <p>
 * [User] can select and buy multiple items at a time. Money can be deposited multiple times (you will get the item if there is a time gap > 30 secs).
 * He can also do window shopping (see only the prices of items and buy nothing)
 * <p>
 * [Operator] can load the items and mark the items as expired if needed, gets notified if a product goes out of stock.
 * <p>
 * [Admin] can own multiple vending machines, he should have an analytics report of the items purchased in a month.
 * He can also change the prices directly and it should reflect in all the vending machines which he owns.
 * <p>
 * Exception handling in all the edge cases
 * Both high & low level designs were expected.
 */
public class DesignVendingMachine {
    List<VendingMachine> vendingMachines;

    public DesignVendingMachine(List<VendingMachine> vendingMachines) {
        this.vendingMachines = vendingMachines;
    }

    static class VendingMachine {
        Map<String, Item> itemsById;
        Set<String> expiredItems = new HashSet<>();
        Map<String, Integer> countByItemId;
        String id;
        List<Item> selections = new ArrayList<>();
        double total;

        void reset() {
            selections.clear();
            total = 0d;
        }

        double buy() {
            double cost = selections.stream().mapToDouble(p -> p.price).sum();
            if (total >= cost) {
                selections.forEach(item -> countByItemId.computeIfPresent(item.id, (k, count) -> count - 1));
                reset();
                return total - cost;
            }
            return 0;
        }

        void select(Item item) {
            if (expiredItems.contains(item.id)) {
                return;
            }
            if (countByItemId.get(item.id) - selections.stream().filter(item1 -> item1.id.equals(item.id)).count() > 0) {
                selections.add(item);
            } else {
                // notify operator that this item is out of stock - alternatively we can have a background process to scan the machine inventory and notify if
                // stock of a product is close to 0
                runningOutOfStock(item); // disadvantage is that operator will be getting notified every time someone selects an item in short supply
            }
        }

        void runningOutOfStock(Item item) {
            System.out.println(item);
        }

        // todo trigger buy after delay of 30 secs...
        void deposit(double amount) {
            total += amount;

        }

        void load(Map<Item, Integer> itemsLoad) {
            for (Item item : itemsLoad.keySet()) {
                itemsById.put(item.id, item);
                countByItemId.compute(item.id, (k, count) -> count == null ? itemsLoad.get(item) : count + itemsLoad.get(item));
            }
        }

        void expireItem(String id) {
            expiredItems.add(id);
        }

        int stocks(String id) {
            return countByItemId.get(id);
        }
    }


    static class Item {
        String id;
        double price;

        public Item(String id, double price) {
            this.id = id;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(id, item.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
