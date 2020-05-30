package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * My solution to an Amazon interview question
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
public class CustomerTargeting2 {
    enum Tranche {BOTTOM, MIDDLE, TOP}

    public static void main(String[] args) {
        CustomerTargeting2 driver = new CustomerTargeting2();
        Map<String, Double> purchasesByCustomer = new HashMap<>();
        for (int i = 0; i < 40; i++) {
            purchasesByCustomer.put("name" + i, Double.parseDouble(String.valueOf(i)));
        }
        System.out.println(driver.customersByTranch(purchasesByCustomer));
    }

    public Multimap<Tranche, String> customersByTranch(Map<String, Double> spendingByCustomer) {
        List<Map.Entry<String, Double>> orderedCustomersSpending = spendingByCustomer.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        Multimap<Tranche, String> customerByTranche = ArrayListMultimap.create();
        int trancheSize = spendingByCustomer.size() / 4;

        Map<String, Integer> customerCountByTranche = ImmutableMap.of(Tranche.BOTTOM.name(), trancheSize, Tranche.MIDDLE.name(), trancheSize * 2, Tranche.TOP.name(), trancheSize);
        // the orderedCustomersSpending is sorted in ascending order
        Tranche[] tranches = new Tranche[]{Tranche.BOTTOM, Tranche.MIDDLE, Tranche.TOP};
        int index = 0;
        int count = 0;
        double lastValue = 0d;
        Tranche currentTranche = tranches[index];

        for (Map.Entry<String, Double> entry : orderedCustomersSpending) {
            double currentValue = entry.getValue();
            if (currentValue > lastValue) {
                lastValue = currentValue;
                count++;
            }
            if (count <= customerCountByTranche.get(currentTranche.name())) {
                customerByTranche.put(currentTranche, entry.getKey());
            } else {
                currentTranche = tranches[++index];
                customerByTranche.put(currentTranche, entry.getKey());
                count = 0;
                lastValue = currentValue;
            }
        }
        return customerByTranche;
    }
}
