package org.haldokan.edge.interviewquest.facebook;

/**
 * My solution to a Facebook interview question. The idea is to find the pivot doing scan while trying to find the
 * number K. If found we return it else we do a binary search on the 2nd half of the array after the pivot. This works well
 * if the rotated array is used to find many such numbers K since we need to find the pivot one time then do binary search
 * on both segments of the array: 0 -> pivot and pivot + 1 -> arr.len. For the latter scenario we actually find the element
 * K in log(n) as if the array is sorted but not rotated (we can ignore the one time scan of the array to find the pivot)
 *
 * The Question: 4_STAR
 * You are given an integer K, and a sorted array as an input which has been rotated about an unknown pivot.
 * For example, 4, 5, 6, 7, 8, 9, 1, 2, 3.
 * We need to write a function which should return the index of K in the array, if K is present, else return -1.
 * Created by haytham.aldokanji on 6/23/20.
 */
public class IndexOfElementInRotatedSortedArray {

    public static void main(String[] args) {
        test1();
        test2();
    }

    static void test1() {
        int[] arr1 = new  int[]{1, 2, 5, 7, 10};
        System.out.println(binarySearch(arr1, 0, arr1.length, 7));
        System.out.println(binarySearch(arr1, 0, arr1.length, 1));
        System.out.println(binarySearch(arr1, 0, arr1.length, 10));
        System.out.println(binarySearch(arr1, 0, arr1.length, 11));
    }

    static void test2() {
        int[] arr2 = new int[]{4, 5, 6, 7, 8, 9, 1, 2, 3};
        System.out.println(findIndex(arr2, 8));
        System.out.println(findIndex(arr2, 9));
        System.out.println(findIndex(arr2, 1));
        System.out.println(findIndex(arr2, 2));
        System.out.println(findIndex(arr2, 3));
        System.out.println(findIndex(arr2, 11));
    }

    static int findIndex(int arr[], int k) {
        if (arr[0] < arr[arr.length -1]) {
            return binarySearch(arr, 0, arr.length, k);
        }
        int index = 0;
        int pivot = index + 1;
        while (pivot < arr.length && arr[index] < arr[pivot]) {
            if (arr[index] == k) {
                return index;
            }
            index = pivot;
            pivot++;
        }
        return binarySearch(arr, pivot, arr.length, k);
    }

    static int binarySearch(int arr[], int start, int end, int k) {
//        System.out.printf("%d, %d%n", start, end);
        int mid = (start + end) / 2;
        if (start >= end) {
            return -1;
        }
        if (k == arr[mid]) {
            return mid;
        }
        if (k < arr[mid]) {
            return binarySearch(arr, start, mid, k);
        }
        return binarySearch(arr, mid + 1, end, k);
    }
}
