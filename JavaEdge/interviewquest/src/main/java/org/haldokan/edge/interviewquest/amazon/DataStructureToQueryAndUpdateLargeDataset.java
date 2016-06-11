package org.haldokan.edge.interviewquest.amazon;

import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * My solution to an Amazon interview question
 * The Question: 4_STAR
 * Design a data structure to keep track of top k elements out of 2 billion records.
 * Each record has a key which is 32 bit and a number which is count of how many times the customer has visited us.
 * Come up with a data structure so that the update of elements in 2 billion records is efficient, and getting top k
 * elements is efficient
 */

public class DataStructureToQueryAndUpdateLargeDataset {
    private static Random random = new Random();
    private final Map<Integer, Record> map = new ConcurrentHashMap<>();
    private final SortedSet<Record> mostAccessed = new TreeSet<>((r1, r2) -> r1.count - r2.count);
    private final int limit;

    public DataStructureToQueryAndUpdateLargeDataset(int limit) {
        this.limit = limit;
    }

    public static void main(String[] args) {
        DataStructureToQueryAndUpdateLargeDataset driver = new DataStructureToQueryAndUpdateLargeDataset(5);
        int sampleSize = 20;
        for (int i = 0; i < sampleSize; i++) {
            driver.add(new Record(i, i * 2));
        }

        for (int i = 0; i < 500; i++) {
            driver.update(i % sampleSize, random.nextInt(100));
        }

        driver.mostAccessed.stream()
                .sorted((r1, r2) -> r1.count - r2.count)
                .forEach(System.out::println);

        driver.map.entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().getCount() - e2.getValue().getCount())
                .forEach(System.out::println);

    }

    public void add(Record record) {
        map.putIfAbsent(record.getId(), record);
        updateMostAccessed(record);
    }

    public void update(Integer id, Integer val) {
        map.computeIfPresent(id, (k, v) -> {
            mostAccessed.remove(v);
            v.increaseCount(val);
            updateMostAccessed(v);

            return v;
        });
    }

    private void updateMostAccessed(Record record) {
        if (mostAccessed.size() < limit) {
            mostAccessed.add(record);
        } else if (record.count > mostAccessed.first().count) {
            mostAccessed.remove(mostAccessed.first());
            mostAccessed.add(record);
        }
    }

    private static class Record {
        private final Integer id;
        private Integer count;

        public Record(Integer id, Integer count) {
            this.id = id;
            this.count = count;
        }

        public Record(Record record) {
            this.id = record.id;
            this.count = record.count;
        }

        public Integer getId() {
            return id;
        }

        public Integer getCount() {
            return count;
        }

        public Integer increaseCount(Integer val) {
            count += val;
            return count;
        }

        @Override
        public String toString() {
            return "\n" + id + "/" + count;
        }
    }
}
