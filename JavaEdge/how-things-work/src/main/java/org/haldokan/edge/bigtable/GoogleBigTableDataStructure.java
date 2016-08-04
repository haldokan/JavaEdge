package org.haldokan.edge.bigtable;

import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * In order to appreciate the data structures used in Google Big Table I try here to implement some of the functionality
 * all in memory for now. In particular:
 * - Lexicographical key ranges that are hashed to separate tablets:
 * - Family keys that can be queried together
 * - key range queries
 *
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 8/2/16.
 */
//TODO: ADD dynamic key ranges distribution to tablets
public class GoogleBigTableDataStructure {
    // key ranges are calculated dynamically in the real Big Table (todo: how is that done?)
    private static final int OVERALL_KEY_RANGE = 128; // ascii printable chars
    private final int numberOfTablets; // should be passed in cntr
    // linked map so the column family returns the columns in the same order of adding them
    SetMultimap<String, String> columnFamilies = LinkedHashMultimap.create();
    // key range -> map {row -> map {column family -> map {timestamp -> content}}}
    private Map<KeyRange, NavigableMap<String, Map<String, NavigableMap<Long, String>>>> bigTable =
            new TreeMap<>((v1, v2) -> v1.start - v2.start);

    public GoogleBigTableDataStructure(int numberOfTablets) {
        this.numberOfTablets = numberOfTablets;
        segmentBigTable();
    }

    // updates to one row are atomic: the concurrent map we use insures that
    // content should be a string of bytes byte[]
    public void update(String rowKey, String columnName, String content) {
        verifyColumnFamilyExist(columnName);

        NavigableMap<String, Map<String, NavigableMap<Long, String>>> tablet = getTablet(rowKey);
        Map<String, NavigableMap<Long, String>> columns = tablet.computeIfAbsent(rowKey, v -> new HashMap<>());
        // data is sorted in descending time stamp order to simulate how it is put on disk: last value on top of the file
        // so it can be obtained w/o disk seek
        Map<Long, String> versionedData = columns.computeIfAbsent(columnName, v -> new TreeMap<>((v1, v2) -> v2.compareTo(v1)));

        long version = System.currentTimeMillis() + System.nanoTime();
        versionedData.put(version, content);
    }

    public void update(String rowKey, Map<String, String> contentByColumn) {
        contentByColumn.entrySet().stream().forEach(entry -> update(rowKey, entry.getKey(), entry.getValue()));
    }

    public String findValue(String rowKey, String qualifiedColumn) {
        verifyColumnFamilyExist(qualifiedColumn);
        // return the last version of the row/column data
        Map<String, NavigableMap<Long, String>> row = getTablet(rowKey).get(rowKey);

        if (row == null) {
            return null;
        }

        NavigableMap<Long, String> columnVersionedValues = row.get(qualifiedColumn);
        // different rows have different columns
        return columnVersionedValues == null ? null : columnVersionedValues.firstEntry().getValue();
    }

    // Going to assume for now that both start and end keys fall in the same tablet (todo: support ranges spanning multiple tablets)
    // approximately the way it is done in Google Big Table is to keep memory indexes to the data blocks in SSTables that
    // are the building blocks of tablets
    public Map<String, ListMultimap<String, String>> findValuesInKeyRange(String startRowKey,
                                                                          String endRowKey,
                                                                          Set<String> qualifiedColumns) {
        NavigableMap<String, Map<String, NavigableMap<Long, String>>> tablet = getTablet(startRowKey);
        SortedMap<String, Map<String, NavigableMap<Long, String>>> rows = tablet.subMap(startRowKey, endRowKey);

        // row -> map col -> list vals
        Map<String, ListMultimap<String, String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, NavigableMap<Long, String>>> rowEntry : rows.entrySet()) {
            String rowKey = rowEntry.getKey();
            Map<String, String> rowValuesByColumn = findValuesForColumns(rowKey, qualifiedColumns);

            Multimap<String, String> columnValues = result.computeIfAbsent(rowKey, v -> ArrayListMultimap.create());
            for (Map.Entry<String, String> entry : rowValuesByColumn.entrySet()) {
                columnValues.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public Map<String, String> findValuesForColumnFamily(String rowKey, String columnFamily) {
        if (!columnFamilies.containsKey(columnFamily)) {
            throw new IllegalArgumentException("Unknown column family");
        }
        Set<String> columns = columnFamilies.get(columnFamily);
        // family has a single column which in this case takes the same name as the family name
        Map<String, String> resultSet = new LinkedHashMap<>();
        if (columns.size() == 1 && columns.iterator().next() == null) {
            resultSet.put(columnFamily, findValue(rowKey, columnFamily));
        } else {
            for (String column : columns) {
                resultSet.put(column, findValue(rowKey, columnFamily + ":" + column));
            }
        }
        return resultSet;
    }

    public Map<String, String> findValuesForColumns(String rowKey, Set<String> columns) {
        Map<String, String> resultSet = new LinkedHashMap<>();

        for (String column : columns) {
            resultSet.put(column, findValue(rowKey, column));
        }
        return resultSet;
    }

    // this is O(n) in the number of ranges. It can be reduced to O(long n) using Interval tree
    public NavigableMap<String, Map<String, NavigableMap<Long, String>>> getTablet(String key) {
        KeyRange keyRange = bigTable.keySet().stream()
                .filter(range -> range.inRange(key))
                .findAny().get();

        return bigTable.get(keyRange);
    }

    // todo add support to adding new column to an existing family -
    public void createColumnFamily(String family, String... qualifiers) {
        if (qualifiers.length == 0) {
            columnFamilies.put(family, null);
        } else {
            Arrays.stream(qualifiers).forEach(qualifier -> columnFamilies.put(family, qualifier));
        }
    }

    private void verifyColumnFamilyExist(String qualifiedColumn) {
        if (qualifiedColumn == null) {
            throw new NullPointerException("Null qualified column");
        }

        int indexOfSeparator = qualifiedColumn.indexOf(":");
        String family;
        String qualifier = null;
        if (indexOfSeparator < 0) {
            family = qualifiedColumn;
        } else if (indexOfSeparator == qualifiedColumn.length() - 1) {
            if (qualifiedColumn.length() == 1) {
                throw new IllegalArgumentException("Illegal column name: " + qualifiedColumn);
            }
            family = qualifiedColumn.substring(0, qualifiedColumn.length() - 1);
        } else {
            family = qualifiedColumn.substring(0, indexOfSeparator);
            qualifier = qualifiedColumn.substring(indexOfSeparator + 1, qualifiedColumn.length());
        }

        boolean exists = qualifier == null ? columnFamilies.containsKey(family) : columnFamilies.containsEntry(family, qualifier);
        if (!exists) {
            System.out.printf("%s%n", columnFamilies);
            throw new RuntimeException("Family '" + family + "' or column '" + qualifier + "' do not exist");
        }
    }

    // tablet in the real Big Table are created dynamically (todo do the same)
    private void segmentBigTable() {
        int tabletRangeStart = 0;
        int tabletRangeLen = OVERALL_KEY_RANGE / numberOfTablets;

        KeyRange lastRange = new KeyRange(OVERALL_KEY_RANGE - tabletRangeLen, -1);
        bigTable.put(lastRange, new ConcurrentSkipListMap<>());

        for (int i = 1; i < numberOfTablets; i++) {
            KeyRange keyRange = new KeyRange(tabletRangeStart, tabletRangeStart + tabletRangeLen);
            bigTable.put(keyRange, new ConcurrentSkipListMap<>());
            tabletRangeStart = keyRange.end;
        }
    }

    public void print() {
        System.out.printf("%s%n", bigTable);
    }

    private static class KeyRange {
        private final int start, end;

        public KeyRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean inRange(String key) {
            char firstChar = key.charAt(0);
            boolean check1 = firstChar >= start;
            boolean check2 = true;
            if (end != -1) {
                check2 = firstChar < end;
            }
            return check1 && check2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyRange keyRange = (KeyRange) o;

            if (start != keyRange.start) return false;
            return end == keyRange.end;

        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ")";
        }
    }
}
