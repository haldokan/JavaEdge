package org.haldokan.edge.cucumber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by haytham.aldokanji on 9/10/15.
 */
public class ProvisionsStore {
    private Map<String, Integer> store = new ConcurrentHashMap<>();

    public Integer provisionsFor(String item) {
        return store.computeIfAbsent(item, e -> uploadFromStorage(item));
    }

    public void updateProvisionsFor(String item, Integer amount) {
        Integer newAmount = store.compute(item, (k, v) -> v == null ? amount : v + amount);
        store(item, newAmount);
    }

    private Integer uploadFromStorage(String item) {
        System.out.printf("Uploading item %s\n", item);
        return item.length() * 1000;
    }

    private void store(String item, Integer amount) {
        System.out.format("Storing %s/%n\n", item, amount);
    }
}
