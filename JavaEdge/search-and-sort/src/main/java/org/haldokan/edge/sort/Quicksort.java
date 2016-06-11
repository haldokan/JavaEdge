package org.haldokan.edge.sort;

import java.util.Arrays;

/**
 * Nice and concise algorithm for quick sort that relies on the idea of swapping a smaller value with the pivot.
 * Note how the sort is done "in situ" which is great because no extra buffering is needed. I should note that merge sort
 * cannot be done "in situ". The algorithm is presented by Steven S. Skiena in his book Algorithm Design Manual.
 * The Question: 3_STAR
 * @author haldokan
 */
public class Quicksort {
    public static void main(String[] args) {
        int[] a = new int[]{7, 3, 1, 10, 0, 2, 14, 13, 17, 20, 4};
        sort(a, 0, a.length);
        System.out.println(Arrays.toString(a));
    }

    public static void sort(int[] a, int l, int h) {
        if (h <= l)
            return;
        int p = (l + h) / 2;
        for (int i = 0; i < p; i++) {
            if (a[i] > a[p]) {
                int tmp = a[p];
                a[p] = a[i];
                a[i] = tmp;
            }
        }
        sort(a, l, p);
        sort(a, p + 1, h);
    }
}
