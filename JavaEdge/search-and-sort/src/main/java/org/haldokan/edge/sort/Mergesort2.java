package org.haldokan.edge.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Mergesort2 {
    public static void main(String[] args) {
	Integer[] c1 = new Integer[] { 3, 4, 7, 8 };
	Integer[] c2 = new Integer[] { 1, 2, 5, 6, 9, 10 };
	Integer[] c = new Integer[c1.length + c2.length];
	mergeArrays(c, c1, c2);
	System.out.println(Arrays.toString(c));

	String[] s1 = new String[] { "bar", "baz", "foo" };
	String[] s2 = new String[] { "abu3bdo", "allat", "rabbo", "yahwah" };
	String[] s = new String[s1.length + s2.length];
	mergeArrays(s, s1, s2);
	System.out.println(Arrays.toString(s));

	Integer[] cc = new Integer[] { 2, 5, 7, 9, 10, 1, 3, 4, 6, 8, 11 };
	mergeArrays(cc, 0, cc.length / 2, cc.length);
	System.out.println(Arrays.toString(cc));

	cc = new Integer[] { 7, 4, 1, 3, 9, 3, 2, 8, 0, 5, 6 };
	mergesort(cc, 0, cc.length);
	System.out.println(Arrays.toString(cc));
    }

    public static void mergesort(Integer[] d, int low, int high) {
	// if we have a slice of 1 element or an empty slice return (1 element
	// is sorted!)

	if (high - low > 1) {
	    int mid = (low + high) / 2;
	    mergesort(d, low, mid);
	    mergesort(d, mid, high);
	    mergeArrays(d, low, mid, high);
	}
    }

    // the data is in a linked list - can we merge in situ
    private static <E extends Comparable<E>> void mergeArrays(List<E> l, int low, int mid, int high) {
	ListIterator<E> it1 = l.listIterator(low);
	ListIterator<E> it2 = l.listIterator(mid);
	while (it1.hasNext()) {
	    E e1 = it1.next();
	    while (it2.hasNext()) {
		E e2 = it2.next();
		if (e1.compareTo(e2) > 0) {
		    it1.set(e2);
		} else {

		}
	    }
	}

    }

    // m has the data. Copy data b/w low, mid and mid, high to 2 buffs then
    // update the d array.
    // a merge buffer is needed when merging arrays
    private static <E extends Comparable<E>> void mergeArrays(E[] d, int low, int mid, int high) {
	List<E> buf1 = copy(d, low, mid);
	List<E> buf2 = copy(d, mid, high);
	int x = low;
	while (!(buf1.isEmpty() || buf2.isEmpty())) {
	    if (buf1.get(0).compareTo(buf2.get(0)) < 0)
		d[x++] = buf1.remove(0);
	    else
		d[x++] = buf2.remove(0);

	}
	for (E e : buf1)
	    d[x++] = e;
	for (E e : buf2)
	    d[x++] = e;
    }

    private static <E> List<E> copy(E[] d, int start, int end) {
	List<E> l = new LinkedList<E>();
	for (int i = start; i < end; i++) {
	    l.add(d[i]);
	}
	return l;
    }

    // a merge buffer is needed when merging arrays
    private static <E extends Comparable<E>> void mergeArrays(E[] m, E[] c1, E[] c2) {
	int mi = 0;
	int j = 0;
	int k = 0;
	for (int i = 0; i < c1.length; i++) {
	    for (k = j; k < c2.length && c1[i].compareTo(c2[k]) > 0; k++) {
		m[mi] = c2[k];
		mi++;
	    }
	    m[mi++] = c1[i];
	    j = k;
	}
	for (int i = k; i < c2.length; i++) {
	    m[mi++] = c2[i];
	}
    }
}