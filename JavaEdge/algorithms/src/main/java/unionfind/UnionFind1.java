package unionfind;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// take 1: naive solution N^2
public class UnionFind1 {
    private final int[] items;
    private final Map<String, Integer> indexByItem = new HashMap<>();

    public UnionFind1(String[] items) { // passed array param can be of any type
        this.items = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = i;
            indexByItem.put(items[i], i);
        }
    }

    // O(N)
    public void union(String item1, String item2) {
        int item1Val = items[indexByItem.get(item1)];
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item1Val) {
                items[i] = items[indexByItem.get(item2)];
            }
        }
    }

    // O(1)
    public boolean find(String item1, String item2) {
        return items[indexByItem.get(item1)] == items[indexByItem.get(item2)];
    }

    public static final class Tester {
        @Test
        public void testUnionFind() {
            String[] items = new String[]{"v0", "v1", "v2", "v3", "v4", "v5", "v6", "v7", "v8", "v9"};
            UnionFind1 unionFind = new UnionFind1(items);

            assertFalse(unionFind.find("v0", "v4"));
            unionFind.union("v0", "v4");
            System.out.println(Arrays.toString(unionFind.items));
            assertTrue(unionFind.find("v0", "v4"));

            unionFind.union("v4", "v8");
            System.out.println(Arrays.toString(unionFind.items));
            assertTrue(unionFind.find("v4", "v8"));
            assertTrue(unionFind.find("v0", "v4"));
            assertTrue(unionFind.find("v0", "v8"));

            unionFind.union("v0", "v7");
            System.out.println(Arrays.toString(unionFind.items));
            assertTrue(unionFind.find("v4", "v8"));
            assertTrue(unionFind.find("v0", "v4"));
            assertTrue(unionFind.find("v0", "v8"));
            assertTrue(unionFind.find("v4", "v7"));
            assertTrue(unionFind.find("v0", "v7"));
            assertTrue(unionFind.find("v7", "v8"));

            unionFind.union("v7", "v5");
            System.out.println(Arrays.toString(unionFind.items));
            assertTrue(unionFind.find("v5", "v7"));
            assertTrue(unionFind.find("v0", "v5"));
            assertTrue(unionFind.find("v5", "v8"));
        }
    }
}
