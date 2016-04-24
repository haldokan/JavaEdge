package org.haldokan.edge.interviewquest.facebook;

/**
 * My failed attempt to resolve a Facebook interview question.
 * TODO: use ternary search to find the pivot in O(log n) then do binary search to find the search value
 * You are given an integer K, and a sorted array as an input which has been rotated about an unknown pivot.
 * For example, 4 5 6 7 8 9 1 2 3.
 * We need to write a function which should return the index of K in the array, if K is present, else return -1.
 */
public class IndexOfElementInRotatedSortedArray {

    public static void main(String[] args) {
        IndexOfElementInRotatedSortedArray driver = new IndexOfElementInRotatedSortedArray();
//        driver.test1();
        driver.test2();
    }

    public int index(int[] arr, int val) {
//        if (arr[arr.length - 1] >= arr[0]) {
//            return Arrays.binarySearch(arr, val);
//        }
        return doIndex(arr, val, 0, arr.length);
    }

    private int doIndex(int[] arr, int val, int start, int end) {

        if (end <= start) {
            return -1;
        }

        int mid = (start + end) / 2;

        if (arr[mid] == val) {
            return mid;
        }

        if (val > arr[mid] && val > arr[start]) {
            return doIndex(arr, val, start, mid);
        } else {
            return doIndex(arr, val, mid, end);
        }
    }

    private void test1() {
        //4, 5, 6, 7, 8, 9, 12, 15, 17, 18, 20
        System.out.println("F->" + index(getSortedArr(), 21));
        System.out.println("F->" + index(getSortedArr(), 2));
        System.out.println("T->" + index(getSortedArr(), 6));
        System.out.println("T->" + index(getSortedArr(), 20));
        System.out.println("T->" + index(getSortedArr(), 12));
    }

    private void test2() {
        //4, 5, 6, 7, 8, 9, 1, 2, 3
        System.out.println("T->" + index(getUnsortedArr(), 8));
        System.out.println("T->" + index(getUnsortedArr(), 4));
        System.out.println("T->" + index(getUnsortedArr(), 2));
        System.out.println("T->" + index(getUnsortedArr(), 3));
    }

    private int[] getUnsortedArr() {
        return new int[]{4, 5, 6, 7, 8, 9, 1, 2, 3};
    }

    private int[] getSortedArr() {
        return new int[]{4, 5, 6, 7, 8, 9, 12, 15, 17, 18, 20};
    }

}
