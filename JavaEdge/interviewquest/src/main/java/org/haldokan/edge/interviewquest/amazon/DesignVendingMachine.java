package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * My solution of an Amazon interview question - I provide a rough-edged design/implementation
 *
 * The Question: 3.5-STAR
 *
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
 *
 * 07/15/20
 */
public class DesignVendingMachine {
    List<VendingMachine> vendingMachines;

    public DesignVendingMachine(List<VendingMachine> vendingMachines) {
        this.vendingMachines = vendingMachines;
    }

    void updatePrices(Item[] items) {
        for (VendingMachine machine : vendingMachines) {
            machine.updatePrices(items);
        }
    }

    Map<String, Integer> salesReport(int numDays) {
        Map<String, Integer> salesPerItem = new HashMap<>();

        int doy = LocalDate.now().getDayOfYear();
        for (int i = 1; i <= numDays; i++) {
            for (VendingMachine machine : vendingMachines) {
                Map<String, Integer> salesForDoy = machine.sales.get(doy);
                if (salesForDoy != null) {
                    for (String item : salesForDoy.keySet()) {
                        int saleForItem = salesForDoy.get(item);
                        salesPerItem.compute(item, (k, count) -> count == null ? saleForItem : count + saleForItem);
                    }
                }
                doy = LocalDate.now().minus(i, ChronoUnit.DAYS).getDayOfYear();
            }
        };
        return salesPerItem;
    }

    static class VendingMachine {
        String id;
        Map<String, Item> itemsById;
        Set<String> expiredItems = new HashSet<>();
        Map<Integer, Map<String, Integer>> sales = new HashMap<>(); // day-of-year->item->quantity
        Map<String, Integer> countByItemId;
        List<Item> selections = new ArrayList<>();
        double total;

        void reset() {
            selections.clear();
            total = 0d;
        }

        double buy() {
            double cost = selections.stream().mapToDouble(p -> p.price).sum();
            if (total >= cost) {
                selections.forEach(item -> {
                    countByItemId.computeIfPresent(item.id, (k, count) -> count - 1);

                    int doy = LocalDate.now().getDayOfYear();
                    sales.putIfAbsent(doy, new HashMap<>());
                    sales.get(doy).compute(item.id, (k, count) -> count == null ? 1 : count + 1);
                });
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
            System.out.printf("machine: %s, item: %s%n", id, item);
        }

        void deposit(double amount) {
            total += amount;
            double cost = selections.stream().mapToDouble(p -> p.price).sum();

            if (amount >= cost) {
                buy();
            }
        }

        void load(Map<Item, Integer> itemsLoad) {
            for (Item item : itemsLoad.keySet()) {
                itemsById.put(item.id, item);
                countByItemId.compute(item.id, (k, count) -> count == null ? itemsLoad.get(item) : count + itemsLoad.get(item));
            }
        }

        void expireItem(String id, boolean val) {
            if (val) {
                expiredItems.add(id);
            } else {
                expiredItems.remove(id);
            }
        }

        int stocks(String id) {
            return countByItemId.get(id);
        }

        void updatePrices(Item[] items) {
            for (Item item : items) {
                itemsById.put(item.id, item);
            }
        }

        double price(Item item) {
            return item.price;
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
