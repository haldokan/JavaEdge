package org.haldokan.edge.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * The Question: 3_STAR
 */
public class Mergesort {
    private static int index;
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{7, 2, 8, 1, 4, 11, 9, 10, 3};
        mergesort(arr, 0, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergesort(Integer[] d, int low, int high) {
        // if we have a slice of 1 element or an empty slice return (1 element is sorted!)
        if (high - low > 1) {
            int mid = (low + high) / 2;
//            System.out.printf("left %d %d %d\n", low, mid, high);
            mergesort(d, low, mid);
//            System.out.printf("right %d %d %d\n", low, mid, high);
            mergesort(d, mid, high);
            mergeArrays(d, low, mid, high);
        }
    }

    // m has the data. Copy data b/w low, mid and mid, high to 2 buffs then
    // update the d array a merge buffer is needed when merging arrays
    public static <E extends Comparable<E>> void mergeArrays(E[] d, int low, int mid, int high) {
        System.out.printf("merge %d, %d, %d%n", low, mid, high);
//        System.out.println("before:" + Arrays.toString(d));
        List<E> buf1 = copy(d, low, mid);
        List<E> buf2 = copy(d, mid, high);
//        System.out.println(buf1);
//        System.out.println(buf2);
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
//        System.out.println("after :" + Arrays.toString(d));
    }

    private static <E> List<E> copy(E[] d, int start, int end) {
        return new LinkedList<E>(Arrays.asList(d).subList(start, end));
    }

    // a merge buffer is needed when merging arrays
    public static <E extends Comparable<E>> void mergeArrays(E[] m, E[] c1, E[] c2) {
        int mi = 0;
        int j = 0;
        int k = 0;
        for (E e : c1) {
            for (k = j; k < c2.length && e.compareTo(c2[k]) > 0; k++) {
                m[mi] = c2[k];
                mi++;
            }
            m[mi++] = e;
            j = k;
        }
        for (int i = k; i < c2.length; i++) {
            m[mi++] = c2[i];
        }
    }
}