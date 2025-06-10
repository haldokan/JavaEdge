package unionfind;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// take 3: n log(n) - winner!
public class UnionFind3 {
    private final int[] items;
    private final int [] treeSize;

    public UnionFind3(int[] items) {
        this.items = items;
        this.treeSize = new int[items.length];
        Arrays.fill(treeSize, 1);
    }

    // O log(N): the tree with smaller size is linked to the tree with bigger size
    // this union is cycle-proof in that union(a, b) then union(b, a) does not change the array (brilliant!)
    public void connect(int item1, int item2) {
        int root1 = root(item1);
        int root2 = root(item2);
        if (treeSize[root1] > treeSize[root2]) {
            items[root2] = root1;
            treeSize[root1] += treeSize[root2];
        } else {
            items[root1] = root2;
            treeSize[root2] += treeSize[root1];
        }
    }

    // O log(N) because the depth of the tree is maxed at log(N)
    public boolean connected(int item1, int item2) {
        return root(item1) == root(item2);
    }

    // compress the path by making all the nodes encountered in the search point to the root
    // Optionally we can make each item on the path point to his grandpa (almost as good as pointing to the root)
    private int root(int item) {
        List<Integer> path = new ArrayList<>();
        while (item != items[item]) {
            item = items[item];
            path.add(item);
        }
        for (int pathItem : path) {
            items[pathItem] = item;
        }
        return item;
    }

    public static final class Tester {
        @Test
        public void testUnion1() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            unionFind.connect(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[0]);

            unionFind.connect(4, 8);
            System.out.println("(4, 8)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[8]);

            unionFind.connect(2, 4);
            System.out.println("(2, 4)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[2]);
        }

        @Test
        public void testUnion2() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            unionFind.connect(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertEquals(4, items[0]);

            unionFind.connect(4, 0);
            System.out.println("(4, 0)->" + Arrays.toString(items));
            assertEquals(4, items[0]); // does not change
        }

        @Test
        public void testUnion3() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            unionFind.connect(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[0]);

            unionFind.connect(4, 6);
            System.out.println("(4, 6)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[6]); // attached to the bigger tree

            unionFind.connect(0, 8);
            System.out.println("(0, 8)->" + Arrays.toString(items));
            System.out.println("sizes->" + Arrays.toString(unionFind.treeSize));
            assertEquals(4, items[8]); // attached to the bigger tree
        }

        @Test
        public void testFind1() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            assertFalse(unionFind.connected(0, 4));
            unionFind.connect(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 4));

            assertFalse(unionFind.connected(4, 8));
            assertFalse(unionFind.connected(0, 8));
            unionFind.connect(4, 8);
            System.out.println("(4, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(4, 8));
            assertTrue(unionFind.connected(0, 8));

            assertFalse(unionFind.connected(5, 8));
            unionFind.connect(5, 8);
            System.out.println("(8, 5)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(5, 8));
            assertTrue(unionFind.connected(4, 8));
            assertTrue(unionFind.connected(0, 8));

            assertFalse(unionFind.connected(3, 8));
            unionFind.connect(3, 8);
            System.out.println("(3, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(3, 8));

            unionFind.connect(8, 9);
            System.out.println("(8, 9)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 9));
            assertTrue(unionFind.connected(5, 9));
            assertTrue(unionFind.connected(3, 9));
        }

        @Test
        public void testFind2() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            unionFind.connect(0, 4);
            System.out.println("(0, 4)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 4));
            assertTrue(unionFind.connected(4, 0));

            unionFind.connect(2, 7);
            System.out.println("(2, 7)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(2, 7));
            assertTrue(unionFind.connected(7, 2));

            unionFind.connect(0, 2);
            System.out.println("(0, 2)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 2));
            assertTrue(unionFind.connected(2, 0));

            unionFind.connect(8, 9);
            System.out.println("(8, 9)->" + Arrays.toString(items));
            unionFind.connect(0, 8);
            System.out.println("(0, 8)->" + Arrays.toString(items));
            assertTrue(unionFind.connected(2, 9));
            assertTrue(unionFind.connected(9, 2));
            assertTrue(unionFind.connected(0, 9));
            assertTrue(unionFind.connected(9, 0));
        }

        @Test
        public void testFind3() {
            int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
            UnionFind3 unionFind = new UnionFind3(items);

            unionFind.connect(0, 4);
            unionFind.connect(4, 0); // reverse union does not affect results (same array)
            System.out.println("(0, 4)R->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 4));
            assertTrue(unionFind.connected(4, 0));

            unionFind.connect(2, 7);
            unionFind.connect(7, 2);
            System.out.println("(2, 7)R->" + Arrays.toString(items));
            assertTrue(unionFind.connected(2, 7));
            assertTrue(unionFind.connected(7, 2));

            unionFind.connect(0, 2);
            unionFind.connect(2, 0);
            System.out.println("(0, 2)R->" + Arrays.toString(items));
            assertTrue(unionFind.connected(0, 2));
            assertTrue(unionFind.connected(2, 0));

            unionFind.connect(8, 9);
            unionFind.connect(9, 8);
            System.out.println("(8, 9)R->" + Arrays.toString(items));
            unionFind.connect(0, 8);
            unionFind.connect(8, 0);
            System.out.println("(0, 8)R->" + Arrays.toString(items));
            assertTrue(unionFind.connected(2, 9));
            assertTrue(unionFind.connected(9, 2));
            assertTrue(unionFind.connected(0, 9));
            assertTrue(unionFind.connected(9, 0));
        }
    }
}
