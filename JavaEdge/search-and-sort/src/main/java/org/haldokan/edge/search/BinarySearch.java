package org.haldokan.edge.search;

public class BinarySearch<E extends Comparable<E>> {

    public static void main(String[] args) {
	Integer[] a = new Integer[] { 1, 3, 5, 8, 12, 17, 34, 50, 55, 77 };

	BinarySearch<Integer> bs = new BinarySearch<>();
	System.out.println(bs.findIndex(1, a, 0, a.length));
	System.out.println(bs.findIndex(77, a, 0, a.length));
	System.out.println(bs.findIndex(17, a, 0, a.length));
	System.out.println(bs.findIndex(555, a, 0, a.length));
    }

    public int findIndex(E e, E[] a, int l, int h) {
	if (h <= l) {
	    return -1;
	}

	int m = (l + h) / 2;
	if (e.compareTo(a[m]) == 0) {
	    return m;
	}

	if (e.compareTo(a[m]) < 0)
	    return findIndex(e, a, l, m);
	else
	    return findIndex(e, a, m + 1, h);
    }
}