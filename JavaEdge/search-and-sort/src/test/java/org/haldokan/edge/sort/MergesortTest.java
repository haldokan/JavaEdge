package org.haldokan.edge.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

//TODO where on earth are the asserts? 
public class MergesortTest {
    @Test
    public void test1() throws Exception {
        Integer[] c1 = new Integer[]{3, 4, 7, 8};
        Integer[] c2 = new Integer[]{1, 2, 5, 6, 9, 10};
        Integer[] c = new Integer[c1.length + c2.length];
        Mergesort.mergeArrays(c, c1, c2);
        System.out.println(Arrays.toString(c));

        String[] s1 = new String[]{"bar", "baz", "foo"};
        String[] s2 = new String[]{"abu3bdo", "allat", "rabbo", "yahwah"};
        String[] s = new String[s1.length + s2.length];
        Mergesort.mergeArrays(s, s1, s2);
        System.out.println(Arrays.toString(s));

        Integer[] cc = new Integer[]{2, 5, 7, 9, 10, 1, 3, 4, 6, 8, 11};
        Mergesort.mergeArrays(cc, 0, cc.length / 2, cc.length);
        System.out.println(Arrays.toString(cc));

        cc = new Integer[]{7, 4, 1, 3, 9, 3, 2, 8, 0, 5, 6};
        Mergesort.mergesort(cc, 0, cc.length);
        System.out.println(Arrays.toString(cc));
    }

    @Test
    public void test2() {
        Integer[] array = new Integer[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 20, 17, 15, 13, 12, 16, 18, 19, 25, 26, 24, 27, 23, 28, 22, 30, 20};
        System.out.printf("array len %d\n", array.length);
        Mergesort.mergesort(array, 0, array.length);
        System.out.println(Arrays.toString(array));
    }
}