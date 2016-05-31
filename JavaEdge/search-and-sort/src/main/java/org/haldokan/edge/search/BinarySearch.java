package org.haldokan.edge.search;

/**
 * find an element in a sorted array using binary search
 *
 * @param <E>
 * @author haldokan
 */
public class BinarySearch<E extends Comparable<E>> {

    public static void main(String[] args) {
        Integer[] a = new Integer[]{1, 3, 5, 8, 12, 17, 34, 50, 55, 77};

        BinarySearch<Integer> bs = new BinarySearch<>();
        System.out.println(bs.findIndex(1, a, 0, a.length));
        System.out.println(bs.findIndex(77, a, 0, a.length));
        System.out.println(bs.findIndex(17, a, 0, a.length));
        System.out.println(bs.findIndex(555, a, 0, a.length));

        a = new Integer[]{1};
        System.out.println("->" + bs.findIndex(1, a, 0, a.length));
        System.out.println("->" + bs.findIndex(5, a, 0, a.length));
    }

    public int findIndex(E element, E[] arr, int start, int end) {
        if (end <= start) {
            return -1;
        }

        int mid = (start + end) / 2;
        if (element.compareTo(arr[mid]) == 0) {
            return mid;
        }

        if (element.compareTo(arr[mid]) < 0)
            return findIndex(element, arr, start, mid - 1);
        else
            return findIndex(element, arr, mid + 1, end);
    }
}