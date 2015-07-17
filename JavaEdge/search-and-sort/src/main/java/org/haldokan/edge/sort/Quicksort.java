package org.haldokan.edge.sort;

import java.util.Arrays;

public class Quicksort {
    public static void main(String[] args) {
	int[] a = new int[] { 7, 3, 1, 10, 0, 2, 14, 13, 17, 20, 4 };
	sort(a, 0, a.length);
	System.out.println(Arrays.toString(a));
    }

    public static void sort(int[] a, int l, int h) {
	// System.out.println(l + "/" + h + "/" +
	// Arrays.toString(Arrays.copyOfRange(a, l, h)));
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
