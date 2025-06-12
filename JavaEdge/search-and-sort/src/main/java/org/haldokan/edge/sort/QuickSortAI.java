package org.haldokan.edge.sort;

import java.util.Arrays;

/**
 *
 * Advantages of Quick Sort
 *
 * Fast in Practice: Generally faster than other O(N log N) algorithms like Merge Sort or Heap Sort for typical inputs.
 * In-Place: It sorts the array without requiring significant additional memory beyond the recursion stack.
 * Cache Friendly: Its partitioning logic tends to access data that is close together in memory, leading to better cache performance.
 *
 * Disadvantages of Quick Sort
 *
 * Worst-Case Performance: The O(N^2) worst case is a concern, though good pivot selection strategies significantly reduce its likelihood.
 * Not Stable: Quick Sort is generally not a stable sorting algorithm, meaning the relative order of equal elements might not be preserved. If stability is required, Merge Sort is usually preferred.
 */
public class QuickSortAI {
    /**
     * Main Quick Sort method. Sorts the array in ascending order.
     * @param arr The array to be sorted.
     * @param low The starting index of the sub-array to be sorted.
     * @param high The ending index of the sub-array to be sorted.
     */
    public void quickSort(int[] arr, int low, int high) {
        // Base case: If the sub-array has 0 or 1 element, it's already sorted.
        if (low < high) {
            // partitionIndex is the final sorted position of the pivot element
            int partitionIndex = partition(arr, low, high);

            // Recursively sort the sub-array before the pivot
            quickSort(arr, low, partitionIndex - 1);

            // Recursively sort the sub-array after the pivot
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    /**
     * Partitions the sub-array around a pivot element (last element in this implementation).
     * Elements smaller than or equal to the pivot are moved to its left, and
     * elements greater than the pivot are moved to its right.
     * @param arr The array to be partitioned.
     * @param low The starting index of the sub-array.
     * @param high The ending index of the sub-array (pivot element).
     * @return The final sorted index of the pivot element.
     */
    private int partition(int[] arr, int low, int high) {
        // Choose the last element as the pivot
        int pivot = arr[high];

        // i will be the index of the last element that is less than or equal to the pivot.
        // It also marks the "wall" between smaller elements and larger elements.
        int i = (low - 1);

        // Iterate through elements from 'low' up to 'high-1' (excluding the pivot)
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to the pivot
            if (arr[j] <= pivot) {
                i++; // Increment index of smaller element

                // Swap arr[i] and arr[j]
                // This moves arr[j] (which is <= pivot) to the left side of the 'wall'
                swap(arr, i, j);
            }
        }

        // After the loop, all elements less than or equal to pivot are before index 'i+1'.
        // Place the pivot (originally at arr[high]) at its correct sorted position (i+1).
        swap(arr, i + 1, high);

        // Return the final index of the pivot
        return i + 1;
    }

    /**
     * Swaps two elements in an array.
     * @param arr The array.
     * @param i Index of the first element.
     * @param j Index of the second element.
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // --- Main method for demonstration ---
    public static void main(String[] args) {
        QuickSortAI sorter = new QuickSortAI();

        // Test case 1
        int[] arr1 = {10, 80, 30, 90, 40, 50, 70};
        System.out.println("Original array 1: " + Arrays.toString(arr1));
        sorter.quickSort(arr1, 0, arr1.length - 1);
        System.out.println("Sorted array 1:   " + Arrays.toString(arr1)); // Expected: [10, 30, 40, 50, 70, 80, 90]
        System.out.println("---");

        // Test case 2: Already sorted array (worst case for naive pivot choice)
        int[] arr2 = {1, 2, 3, 4, 5};
        System.out.println("Original array 2: " + Arrays.toString(arr2));
        sorter.quickSort(arr2, 0, arr2.length - 1);
        System.out.println("Sorted array 2:   " + Arrays.toString(arr2)); // Expected: [1, 2, 3, 4, 5]
        System.out.println("---");

        // Test case 3: Reverse sorted array (worst case for naive pivot choice)
        int[] arr3 = {5, 4, 3, 2, 1};
        System.out.println("Original array 3: " + Arrays.toString(arr3));
        sorter.quickSort(arr3, 0, arr3.length - 1);
        System.out.println("Sorted array 3:   " + Arrays.toString(arr3)); // Expected: [1, 2, 3, 4, 5]
        System.out.println("---");

        // Test case 4: Array with duplicate elements
        int[] arr4 = {7, 2, 1, 6, 8, 3, 5, 4, 7, 2};
        System.out.println("Original array 4: " + Arrays.toString(arr4));
        sorter.quickSort(arr4, 0, arr4.length - 1);
        System.out.println("Sorted array 4:   " + Arrays.toString(arr4)); // Expected: [1, 2, 2, 3, 4, 5, 6, 7, 7, 8]
        System.out.println("---");

        // Test case 5: Empty array
        int[] arr5 = {};
        System.out.println("Original array 5: " + Arrays.toString(arr5));
        sorter.quickSort(arr5, 0, arr5.length - 1);
        System.out.println("Sorted array 5:   " + Arrays.toString(arr5)); // Expected: []
        System.out.println("---");

        // Test case 6: Single element array
        int[] arr6 = {42};
        System.out.println("Original array 6: " + Arrays.toString(arr6));
        sorter.quickSort(arr6, 0, arr6.length - 1);
        System.out.println("Sorted array 6:   " + Arrays.toString(arr6)); // Expected: [42]
        System.out.println("---");
    }
}
