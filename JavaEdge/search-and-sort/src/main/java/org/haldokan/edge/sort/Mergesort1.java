package org.haldokan.edge.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Mergesort1 {
    public static void mergesortWithArrays(String debug, Integer[] d, int low, int high) {
	// if we have a slice of 1 element or an empty slice return (1 element
	// is sorted!)
	int mid;
	System.out.println(debug + "/" + low + "/" + high + "/recur:" + Arrays.toString(d));
	if (high - low > 1) {
	    mid = (low + high) / 2;
	    // System.out.println(mid);

	    mergesortWithArrays("lm", d, low, mid);
	    mergesortWithArrays("mh", d, mid, high);
	    mergeArrays(d, low, mid, high);
	}
    }

    public static void mergeWithSubArrays(String debug, Integer[] a) {
	System.out.println(debug + "/" + Arrays.toString(a));
	if (a.length > 1) {
	    int q = a.length / 2;

	    Integer[] leftArray = Arrays.copyOfRange(a, 0, q);
	    Integer[] rightArray = Arrays.copyOfRange(a, q, a.length);

	    mergeWithSubArrays("left", leftArray);
	    mergeWithSubArrays("right", rightArray);

	    mergeSubArrays(a, leftArray, rightArray);
	}
    }

    // m has the data. Copy data b/w low, mid and mid, high to 2 buffs then
    // update the d array.
    // a merge buffer is needed when merging arrays
    public static <E extends Comparable<E>> void mergeArrays(E[] d, int low, int mid, int high) {
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
	System.out.println(low + "/" + high + "/(" + mid + ")/result" + Arrays.toString(d));
    }

    private static <E> List<E> copy(E[] d, int start, int end) {
	List<E> l = new LinkedList<E>();
	for (int i = start; i < end; i++) {
	    l.add(d[i]);
	}
	return l;
    }

    public static <E extends Comparable<E>> void mergeSubArrays(E[] m, E[] c1, E[] c2) {
	System.out.println("merge/" + Arrays.toString(c1) + "/" + Arrays.toString(c2));
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
	System.out.println("result/" + Arrays.toString(m));
    }
}
