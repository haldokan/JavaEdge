package org.haldokan.edge;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

/**
 * @InProgress From the Google research paper on Big Table:
 * http://static.googleusercontent.com/media/research.google.com/en//archive/bigtable-osdi06.pdf
 * A slice of an example table that stores Web pages. The row name is a reversed URL. The contents column family contains
 * the page contents, and the anchor column family contains the text of any anchors that reference the page. CNN’s home page
 * is referenced by both the Sports Illustrated and the MY-look home pages, so the row contains columns named anchor:cnnsi.com
 * and anchor:my.look.ca. Each anchor cell has one version; the contents column has three versions, at timestamps t3, t5, and t6.
 * <p>
 * Created by haytham.aldokanji on 8/2/16.
 */
//TODO: ADD dynamic key ranges distribution to tablets
// TODO: operations on a single row (read/write for any column) should be atomic
public class GoogleBigTableDataStructure {
    private static final int KEY_RANGE = 128; // ascii printable chars
    // row -> map {column family -> map {timestamp -> content}}
    private SortedMap<String, Map<String, NavigableMap<Long, String>>> tablet = new ConcurrentSkipListMap<>();
    private Map<KeyRange, SortedMap<String, Map<String, NavigableMap<Long, String>>>> bigTable = new HashMap<>();

    public static void main(String[] args) {
        GoogleBigTableDataStructure driver = new GoogleBigTableDataStructure();
        driver.testUpdate();
        driver.testFind();
    }

    public void update(String rowKey, String columnName, String content) {
        Map<String, NavigableMap<Long, String>> columns = tablet.computeIfAbsent(rowKey, v -> new HashMap<>());
        Map<Long, String> versionedData = columns.computeIfAbsent(columnName, v -> new TreeMap<>());

        long version = System.currentTimeMillis() + System.nanoTime();
        versionedData.put(version, content);
    }

    public String find(String rowKey, String columnName) {
        return tablet.get(rowKey).get(columnName).lastEntry().getValue();
    }

    private void testUpdate() {
        String contentColumnFamily = "content";
        IntStream.range(0, 10).forEach(index -> {
            // Note how e reverse the url name so we get good data locality when when distribute key ranges among tablets
            update("com.cnn.www", contentColumnFamily, "<html><body>some cnn content " + index + "</body></html>");
            update("com.nbc.www", contentColumnFamily, "<html><body>some nbc content " + index + "</body></html>");
        });

        System.out.printf("%s%n", tablet);

        // not how columns are created dynamically
        String anchorColumnFamily_si = "anchor:cnnsi.com";
        IntStream.range(0, 10).forEach(index -> update("com.cnn.www", anchorColumnFamily_si, "cnn" + index));

        System.out.printf("%s%n", tablet);

        String anchorColumnFamily_mylook = "anchor:my.look.ca";
        IntStream.range(0, 10).forEach(index -> update("com.cnn.www", anchorColumnFamily_mylook, "cnn.com" + index));

        System.out.printf("%s%n", tablet);
    }

    private void testFind() {
        String contentColumnFamily = "content";
        IntStream.range(0, 10).forEach(index ->
                update("com.cnn.www", contentColumnFamily, "<html><body>some content " + index + "</body></html>"));
        // not how columns are created dynamically
        String anchorColumnFamily_si = "anchor:cnnsi.com";
        IntStream.range(0, 10).forEach(index -> update("com.cnn.www", anchorColumnFamily_si, "cnn" + index));

        String anchorColumnFamily_mylook = "anchor:my.look.ca";
        IntStream.range(0, 10).forEach(index -> update("com.cnn.www", anchorColumnFamily_mylook, "cnn.com" + index));

        // should get the latest versions
        System.out.printf("%s%n", find("com.cnn.www", contentColumnFamily));
        System.out.printf("%s%n", find("com.nbc.www", contentColumnFamily));
        System.out.printf("%s%n", find("com.cnn.www", anchorColumnFamily_si));
        System.out.printf("%s%n", find("com.cnn.www", anchorColumnFamily_mylook));
    }

    private static class KeyRange {
        private final int start, end;

        public KeyRange(char start, char end) {
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
    }
}
