package org.haldokan.edge.sort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Mergesort2 {
    public static void main(String[] args) {
        Mergesort2 driver = new Mergesort2();
        driver.sort(new int[]{7, 2, 8, 1, 4, 11, 9, 10, 3});
    }

    void sort(int[] arr) {
        doSort(arr, 0, arr.length);
    }

    void doSort(int[] arr, int start, int end) {
//        System.out.printf("%d, %d%n", start, end);
        if (end - start > 1) {
            int middle = (start + end) / 2;
            doSort(arr, start, middle);
            doSort(arr, middle, end);

            merge(arr, start, middle, end);
        }
    }

    void  merge(int[] arr, int start, int middle, int end) {
        System.out.printf("%d, %d, %d%n", start, middle, end);
        int[] arr1 = new int[middle - start];
        int[] arr2 = new int[end - middle];

        for (int i = start; i < end; i++) {
            if (i < middle) {
                arr1[i - start] = arr[i];
            } else {
                arr2[i - middle] = arr[i];
            }
        }
        System.out.println("arr1->" + Arrays.toString(arr1));
        System.out.println("arr2->" + Arrays.toString(arr2));
        int[] merged = doMerge(arr1, arr2);
        System.out.println("merged->" + Arrays.toString(merged));

        // we can use arrayCopy but let's stay primitive
        for (int i = start; i < end; i++) {
            arr[i] = merged[i - start];
        }
        System.out.println(Arrays.toString(arr));
    }

    int[] doMerge(int[] arr1, int[] arr2) {
        int[] merged = new int[arr1.length + arr2.length];

        int ndx = 0;
        int ndx1 = 0;
        int ndx2 = 0;
        while (ndx < merged.length) {
            if (ndx1 >= arr1.length) {
                merged[ndx] = arr2[ndx2++];
            } else if (ndx2 >= arr2.length) {
                merged[ndx] = arr1[ndx1++];
            } else if (arr1[ndx1] < arr2[ndx2]) {
                merged[ndx] = arr1[ndx1++];
            } else {
                merged[ndx] = arr2[ndx2++];
            }
            ndx++;
        }
        return merged;
    }
}
