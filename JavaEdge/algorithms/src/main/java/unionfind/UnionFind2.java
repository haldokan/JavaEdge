package unionfind;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Based on the algorithm described in Algorithms: 24-part Lecture Series by Professor Robert Sedgewick.
 */

public class UnionFind2 {
    private final int[] items;
    public UnionFind2(int[] items) {
        this.items = items;
    }

    // O(N) - this union is cycle-proof in that union(a, b) then union(b, a) does not change the array (brilliant!)
    public void union(int item1, int item2) {
        items[root(item1)] = root(item2);
    }

    // O(N) wost case (skinny trees):
    // example 0-1-2-3-4 and 5-6-7-8-9
    // union(4, 9), then find(0, 5) will cause root(0) and root(5) to traverse whole tree
    // find(4, 8) is shorter traversal of the tree
    public boolean find(int item1, int item2) {
        return root(item1) == root(item2);
    }

    private int root(int item) {
        while (item != items[item]) {
            item = items[item];
        }
        return item;
    }
    public static final class Tester {
        @Test
        public void testUnion1() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind2 unionFind = new UnionFind2(items);

            unionFind.union(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertEquals(items[0], 4);

            unionFind.union(4, 8);
            System.out.println("(4, 8)->" + Arrays.toString(items));
            assertEquals(items[4], 8);

            unionFind.union(2, 4);
            System.out.println("(2, 4)->" + Arrays.toString(items));
            assertEquals(items[2], 8); // note how it got the grandpa
        }

        @Test
        public void testUnion2() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind2 unionFind = new UnionFind2(items);

            unionFind.union(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertEquals(items[0], 4);

            unionFind.union(4, 0);
            System.out.println("(4, 0)->" + Arrays.toString(items));
            assertEquals(items[0], 4); // does not change
        }

        @Test
        public void testFind1() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind2 unionFind = new UnionFind2(items);

            assertFalse(unionFind.find(0, 4));
            unionFind.union(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 4));

            assertFalse(unionFind.find(4, 8));
            assertFalse(unionFind.find(0, 8));
            unionFind.union(4, 8);
            System.out.println("(4, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.find(4, 8));
            assertTrue(unionFind.find(0, 8));

            assertFalse(unionFind.find(5, 8));
            unionFind.union(5, 8);
            System.out.println("(8, 5)->" + Arrays.toString(items));
            assertTrue(unionFind.find(5, 8));
            assertTrue(unionFind.find(4, 8));
            assertTrue(unionFind.find(0, 8));

            assertFalse(unionFind.find(3, 8));
            unionFind.union(3, 8);
            System.out.println("(3, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.find(3, 8));

            unionFind.union(8, 9);
            System.out.println("(8, 9)->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 9));
            assertTrue(unionFind.find(5, 9));
            assertTrue(unionFind.find(3, 9));
        }

        @Test
        public void testFind2() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind2 unionFind = new UnionFind2(items);

            unionFind.union(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 4));
            assertTrue(unionFind.find(4, 0));

            unionFind.union(2, 7);
            System.out.println("(2, 7)->" + Arrays.toString(items));
            assertTrue(unionFind.find(2, 7));
            assertTrue(unionFind.find(7, 2));

            unionFind.union(0, 2);
            System.out.println("(0, 2)->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 2));
            assertTrue(unionFind.find(2, 0));

            unionFind.union(8, 9);
            System.out.println("(8, 9)->" + Arrays.toString(items));
            unionFind.union(0, 8);
            System.out.println("(0, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.find(2, 9));
            assertTrue(unionFind.find(9, 2));
            assertTrue(unionFind.find(0, 9));
            assertTrue(unionFind.find(9, 0));
        }

        @Test
        public void testFind3() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind2 unionFind = new UnionFind2(items);

            unionFind.union(0, 4);
            unionFind.union(4, 0); // reverse union does not affect results (same array)
            System.out.println("(0, 4)R->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 4));
            assertTrue(unionFind.find(4, 0));

            unionFind.union(2, 7);
            unionFind.union(7, 2);
            System.out.println("(2, 7)R->" + Arrays.toString(items));
            assertTrue(unionFind.find(2, 7));
            assertTrue(unionFind.find(7, 2));

            unionFind.union(0, 2);
            unionFind.union(2, 0);
            System.out.println("(0, 2)R->" + Arrays.toString(items));
            assertTrue(unionFind.find(0, 2));
            assertTrue(unionFind.find(2, 0));

            unionFind.union(8, 9);
            unionFind.union(9, 8);
            System.out.println("(8, 9)R->" + Arrays.toString(items));
            unionFind.union(0, 8);
            unionFind.union(8, 0);
            System.out.println("(0, 8)R->" + Arrays.toString(items));
            assertTrue(unionFind.find(2, 9));
            assertTrue(unionFind.find(9, 2));
            assertTrue(unionFind.find(0, 9));
            assertTrue(unionFind.find(9, 0));
        }
    }
}
