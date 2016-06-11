package org.haldokan.edge.interviewquest.facebook;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * Given - a number (n) and a sorted array
 * Find a number in the array having least difference with the given number (n).
 * Created by haytham.aldokanji on 4/23/16.
 */
public class ClosestValueInSortedArray {
    public static void main(String[] args) {
        ClosestValueInSortedArray driver = new ClosestValueInSortedArray();

        int[] arr = driver.getSortedArr();
        System.out.println(driver.closestVal(arr, 13, arr[0], 0, arr.length));

        arr = driver.getSortedArr();
        System.out.println(driver.closestVal(arr, 19, arr[0], 0, arr.length));

        arr = driver.getSortedArr();
        System.out.println(driver.closestVal(arr, 2, arr[0], 0, arr.length));

        arr = driver.getSortedArr();
        System.out.println(driver.closestVal(arr, 25, arr[0], 0, arr.length));
    }

    private int closestVal(int[] arr, int val, int closest, int start, int end) {
        if (end <= start) {
            return closest;
        }
        int mid = (start + end) / 2;

        if (Math.abs(arr[mid] - val) < Math.abs(closest - val)) {
            closest = arr[mid];
        }
        if (val > arr[mid]) {
            return closestVal(arr, val, closest, mid + 1, end);
        } else {
            return closestVal(arr, val, closest, start, mid);
        }
    }

    private int[] getSortedArr() {
        return new int[]{4, 5, 6, 7, 8, 9, 12, 15, 17, 18, 22, 25};
    }
}
