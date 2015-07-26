package org.haldokan.edge.lunchbox;

import org.junit.Assert;

/**
 * My solutoin to a Linkedin interview question
 * 
 * If a sorted array is shifted left by unknown number, modify existing binary search to find an element in the modified
 * array
 * 
 * arr = [1, 2, 3, 4, 5, 6] is shifted to
 * 
 * arr = [4, 5, 6, 1, 2, 3]
 * 
 * @author haldokan
 *
 */
public class SearchShiftedSortedArray {

    public static void main(String[] args) {
	int[] arr1 = new int[] { 1, 2, 3, 4, 5, 6 };
	int[] arr2 = new int[] { 4, 5, 6, 1, 2, 3 };
	int[] arr3 = new int[] { 3, 4, 5, 6, 1, 2 };

	SearchShiftedSortedArray searcher = new SearchShiftedSortedArray();
	for (int i = 0; i < arr1.length; i++) {
	    int index = searcher.shiftedBinsearch(arr1, arr1[i], 0, arr1.length);
	    System.out.print(index + ", ");
	    Assert.assertEquals(i, index);
	}
	System.out.println();
	for (int i = 0; i < arr2.length; i++) {
	    int index = searcher.shiftedBinsearch(arr2, arr2[i], 0, arr2.length);
	    System.out.print(index + ", ");
	    Assert.assertEquals(i, index);
	}
	System.out.println();
	for (int i = 0; i < arr3.length; i++) {
	    int index = searcher.shiftedBinsearch(arr3, arr3[i], 0, arr3.length);
	    System.out.print(index + ", ");
	    Assert.assertEquals(i, index);
	}
	System.out.println();
	System.out.println(searcher.shiftedBinsearch(arr1, 10, 0, arr1.length));
    }

    public int shiftedBinsearch(int[] arr, int elem, int start, int end) {
	int inflectionIndex = findInflectionPoint(arr);
	if (inflectionIndex == -1 || elem <= arr[arr.length - 1])
	    return binsearch(arr, elem, inflectionIndex, arr.length);
	else
	    return binsearch(arr, elem, 0, inflectionIndex);
    }

    // going to assume the shifted array was sorted in asc order (implicit in normal bin search)
    public int findInflectionPoint(int[] arr) {
	int ndx = -1;
	for (int i = 0; i < arr.length - 1; i++) {
	    if (arr[i] > arr[i + 1]) {
		ndx = i + 1;
		break;
	    }
	}
	return ndx;
    }

    public int binsearch(int[] arr, int elem, int start, int end) {
	if (end == start)
	    return -1;

	int mid = (start + end) / 2;
	if (elem == arr[mid])
	    return mid;

	if (elem < arr[mid]) {
	    return binsearch(arr, elem, start, mid);
	} else {
	    return binsearch(arr, elem, mid + 1, end);
	}
    }
}
