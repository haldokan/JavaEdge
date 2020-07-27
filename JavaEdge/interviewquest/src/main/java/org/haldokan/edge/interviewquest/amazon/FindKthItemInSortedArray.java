package org.haldokan.edge.interviewquest.amazon;

/**
 * @Pending: I think this a good start but still does not seal the deal
 *
 * Find the kth missing element in a sorted array. For example [2,3,5,7], k = 0: return 4, k = 1: return 6
 *
 * expected time complexity log(n)
 *
 */
public class FindKthItemInSortedArray {
    public static void main(String[] args) {
        System.out.println(find(new int[]{2, 3, 5, 7}, 0, 4, 0));
    }

    static int find(int[] arr, int start, int end, int k) {
        if (start >= end) {
            return -start;
        }
        int mid = (start + end) / 2;
        System.out.printf("%d-%d-%d%n", start, mid, end);
        if ((arr[mid] - arr[start] - (mid - start) == k)) {
            return mid;
        }
        if ((arr[mid] - arr[start] - (mid - start) > k)) {
            return find(arr, start, mid, k);
        } else {
            return find(arr, mid + 1, end, k);
        }
    }
}
