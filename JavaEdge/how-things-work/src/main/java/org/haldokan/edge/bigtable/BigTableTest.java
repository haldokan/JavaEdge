package org.haldokan.edge.bigtable;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * TODO: add asserts
 * Created by haytham.aldokanji on 8/3/16.
 *
 * SSTable with 64K blocks with index to them at the end of the SStable. Index loaded into memory and binary search is done
 * to find out the block location which can be retrieved with a single seek. SSTable can alternatively be loaded into memory
 *
 * Initially we start with one tablet and split it when it gets too big
 */
public class BigTableTest {
    private static final int NUM_TABLETS = 16;

    @Test
    public void testUpdateSingleColumn() {
        GoogleBigTableDataStructure bigTable = new GoogleBigTableDataStructure(NUM_TABLETS);
        String contentColumnFamily = "content";
        bigTable.createColumnFamily(contentColumnFamily);
        IntStream.range(0, 10).forEach(index -> {
            // Note how e reverse the url name so we get good data locality when when distribute key ranges among tablets
            bigTable.update("com.cnn.www", contentColumnFamily, "<html><body>some cnn content " + index + "</body></html>");
            bigTable.update("com.nbc.www", contentColumnFamily, "<html><body>some nbc content " + index + "</body></html>");
            bigTable.update("gov.cia.www", contentColumnFamily, "<html><body>some cia content " + index + "</body></html>");
            bigTable.update("net.un.www", contentColumnFamily, "<html><body>some un content " + index + "</body></html>");
            bigTable.update("de.spiegel.www", contentColumnFamily, "<html><body>some spiegel content " + index + "</body></html>");
        });

        bigTable.print();

        // note how columns are created dynamically
        bigTable.createColumnFamily("anchor", "cnnsi.com", "my.look.ca");

        String anchor_si = "anchor:cnnsi.com";
        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", anchor_si, "cnn" + index));

        bigTable.print();

        String anchor_mylook = "anchor:my.look.ca";
        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", anchor_mylook, "cnn.com" + index));

        bigTable.print();
    }

    @Test
    public void testUpdateMultipleColumns() {
        GoogleBigTableDataStructure bigTable = new GoogleBigTableDataStructure(NUM_TABLETS);
        String contentColumnFamily = "content";
        bigTable.createColumnFamily(contentColumnFamily);
        String contentColumn = "content";

        // not how columns are created dynamically
        String anchorColumnFamily = "anchor";
        String anchorColumn_si = "cnnsi.com";
        String anchorColumn_mylook = "my.look.ca";
        bigTable.createColumnFamily(anchorColumnFamily, anchorColumn_si, anchorColumn_mylook);

        String qualifiedAnchorColumn_si = anchorColumnFamily + ":" + anchorColumn_si;
        String qualifiedAnchorColumn_mylook = anchorColumnFamily + ":" + anchorColumn_mylook;

        Map<String, String> updateMap = new HashMap<>();

        IntStream.range(0, 10).forEach(index -> {
            updateMap.put(contentColumn, "<html><body>some content " + index + "</body></html>");
            updateMap.put(qualifiedAnchorColumn_si, "cnn" + index);
            updateMap.put(qualifiedAnchorColumn_mylook, "cnn.com" + index);
            // update multiple columns for row com.cnn.www
            bigTable.update("com.cnn.www", updateMap);
        });

        bigTable.print();
    }

    @Test
    public void testFindValue() {
        GoogleBigTableDataStructure bigTable = new GoogleBigTableDataStructure(NUM_TABLETS);
        String contentColumnFamily = "content";
        bigTable.createColumnFamily(contentColumnFamily);

        String contentColumn = "content";
        IntStream.range(0, 10).forEach(index ->
                bigTable.update("com.cnn.www", contentColumn, "<html><body>some content " + index + "</body></html>"));

        // not how columns are created dynamically
        String anchorColumnFamily = "anchor";
        String anchorColumn_si = "cnnsi.com";
        String anchorColumn_mylook = "my.look.ca";
        bigTable.createColumnFamily(anchorColumnFamily, anchorColumn_si, anchorColumn_mylook);

        String qualifiedAnchorColumn_si = anchorColumnFamily + ":" + anchorColumn_si;
        String qualifiedAnchorColumn_mylook = anchorColumnFamily + ":" + anchorColumn_mylook;

        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", qualifiedAnchorColumn_si, "cnn" + index));
        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", qualifiedAnchorColumn_mylook, "cnn.com" + index));

        // should get the latest versions
        System.out.printf("%s%n", bigTable.findValue("com.cnn.www", contentColumn));
        System.out.printf("%s%n", bigTable.findValue("com.nbc.www", contentColumn));
        System.out.printf("%s%n", bigTable.findValue("com.cnn.www", qualifiedAnchorColumn_si));
        System.out.printf("%s%n", bigTable.findValue("com.cnn.www", qualifiedAnchorColumn_mylook));
    }

    @Test
    public void testFindValuesInKeyRange() {
        GoogleBigTableDataStructure bigTable = new GoogleBigTableDataStructure(NUM_TABLETS);
        String contentColumnFamily = "content";
        bigTable.createColumnFamily(contentColumnFamily);

        String contentColumn = "content";
        IntStream.range(0, 10).forEach(index ->
                bigTable.update("com.cnn.www", contentColumn, "<html><body>some content " + index + "</body></html>"));

        // note how columns are created dynamically
        String anchorColumnFamily = "anchor";
        String anchorColumn_si = "cnnsi.com";
        String anchorColumn_mylook = "my.look.ca";
        bigTable.createColumnFamily(anchorColumnFamily, anchorColumn_si, anchorColumn_mylook);

        String qualifiedAnchorColumn_si = anchorColumnFamily + ":" + anchorColumn_si;
        String qualifiedAnchorColumn_mylook = anchorColumnFamily + ":" + anchorColumn_mylook;

        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cbc.www", qualifiedAnchorColumn_si, "cnn" + index));
        IntStream.range(0, 10).forEach(index -> bigTable.update("com.bbc.www", qualifiedAnchorColumn_mylook, "cnn.com" + index));

        // should get the latest versions
        Set<String> columns = new LinkedHashSet<>();
        columns.add(contentColumn);
        columns.add(qualifiedAnchorColumn_mylook);
        System.out.printf("%s%n", bigTable.findValuesInKeyRange("com.bbc.www", "com.cnn.www", columns));

        columns = new LinkedHashSet<>();
        columns.add(qualifiedAnchorColumn_si);
        columns.add(qualifiedAnchorColumn_mylook);
        System.out.printf("%s%n", bigTable.findValuesInKeyRange("com.bbc.www", "com.cnn.www", columns));
    }

    @Test
    public void testFindValuesForColumnFamily() {
        GoogleBigTableDataStructure bigTable = new GoogleBigTableDataStructure(NUM_TABLETS);
        String contentColumnFamily = "content";
        bigTable.createColumnFamily(contentColumnFamily);

        String contentColumn = "content";
        IntStream.range(0, 10).forEach(index ->
                bigTable.update("com.cnn.www", contentColumn, "<html><body>some content " + index + "</body></html>"));

        // not how columns are created dynamically
        String anchorColumnFamily = "anchor";
        String anchorColumn_si = "cnnsi.com";
        String anchorColumn_mylook = "my.look.ca";
        bigTable.createColumnFamily(anchorColumnFamily, anchorColumn_si, anchorColumn_mylook);

        String qualifiedAnchorColumn_si = anchorColumnFamily + ":" + anchorColumn_si;
        String qualifiedAnchorColumn_mylook = anchorColumnFamily + ":" + anchorColumn_mylook;

        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", qualifiedAnchorColumn_si, "cnn" + index));
        IntStream.range(0, 10).forEach(index -> bigTable.update("com.cnn.www", qualifiedAnchorColumn_mylook, "cnn.com" + index));

        // should get the latest versions
        System.out.printf("%s%n", bigTable.findValuesForColumnFamily("com.cnn.www", contentColumnFamily));
        System.out.printf("%s%n", bigTable.findValuesForColumnFamily("com.nbc.www", contentColumnFamily));
        System.out.printf("%s%n", bigTable.findValuesForColumnFamily("com.cnn.www", anchorColumnFamily));
        System.out.printf("%s%n", bigTable.findValuesForColumnFamily("com.cnn.www", anchorColumnFamily));
    }
}
