package org.haldokan.edge.rssserver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haytham.aldokanji on 9/23/15.
 */
public class FetchedData {
    private final List<Item> items;
    private LocalTime next;
    private boolean failed;

    public FetchedData() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public LocalTime getNext() {
        return next;
    }

    public void setNext(LocalTime next) {
        this.next = next;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "FetchedData{" +
                "items=" + items +
                ", next=" + next +
                ", failed=" + failed +
                '}';
    }
}
