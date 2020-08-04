package org.haldokan.edge.search;

/**
 * find an element in a sorted array using binary search - made array generic so any value that implements Comparable
 * can use the function
 *
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
        if (start >= end) {
            return -start; // negative value of where it would have been if it existed in the array
        }
        int mid = (start + end) / 2;
        if (element.compareTo(arr[mid]) == 0) {
            return mid;
        }
        return element.compareTo(arr[mid]) < 0 ? findIndex(element, arr, start, mid) : findIndex(element, arr, mid + 1, end);
    }
}
