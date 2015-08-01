package org.haldokan.edge.interviewquest.linkedin;

/**
 * My solution to a LinkedIn interview question
 *
 * You have two arrays of integers, where the integers do not repeat and the two arrays have no common integers.
 *
 * Let x be any integer in the first array, y any integer in the second. Find min(Abs(x-y)). That is, find the smallest
 * difference between any of the integers in the two arrays.
 *
 * Assumptions: Assume both arrays are sorted in ascending order.
 *
 * @author haldokan
 *
 */
public class SmallestDiffBwTwoSortedArrays {

    public static void main(String[] args) {
	SmallestDiffBwTwoSortedArrays driver = new SmallestDiffBwTwoSortedArrays();
	System.out.println(driver.sdiff(new int[] { 1, 5, 9, 13, 17, 21 }, new int[] { 4, 7, 11, 15, 19 }));
	System.out.println(driver.sdiff(new int[] { 0, 5, 9, 13, 16, 21 }, new int[] { 3, 7, 11, 15, 19 }));
	System.out.println(driver.sdiff(new int[] { 0, 5, 9 }, new int[] { 11, 15, 19 }));
	System.out.println(driver.sdiff(new int[] { 11, 15, 19 }, new int[] { 0, 5, 9 }));
	System.out.println(driver.sdiff(new int[] { 11 }, new int[] { 9 }));
	System.out.println(driver.sdiff(new int[] { 11 }, new int[] {}));
    }

    public Diff sdiff(int[] arr1, int[] arr2) {
	if (arr1 == null || arr2 == null || arr1.length == 0 || arr2.length == 0)
	    return null;

	int start = 0;
	Diff smallDiff = new Diff();
	smallDiff.diff = -1;
	for (int i = 0; i < arr1.length; i++) {
	    Diff currDiff = findDiff(arr1[i], start, arr2);
	    start = currDiff.ndx2;
	    if (smallDiff.diff == -1 || currDiff.diff < smallDiff.diff) {
		smallDiff.diff = currDiff.diff;
		smallDiff.ndx1 = i;
		smallDiff.ndx2 = currDiff.ndx2;
		smallDiff.val1 = currDiff.val1;
		smallDiff.val2 = currDiff.val2;
	    }
	}
	return smallDiff;
    }

    private Diff findDiff(int v, int start, int[] arr) {
	int lastVal = arr[arr.length - 1];
	if (v > lastVal)
	    return new Diff(start, arr.length - 1, v, lastVal, Math.abs(v - lastVal));
	// return new int[] { Math.abs(v - last), arr.length - 1 };
	int firstVal = arr[0];
	if (v < firstVal)
	    return new Diff(start, 0, v, firstVal, Math.abs(v - firstVal));
	// return new int[] { Math.abs(v - first), 0 };
	for (int i = start; i < arr.length; i++) {
	    int next = i + 1;
	    if (next <= arr.length - 1) {
		if (arr[i] < v && v < arr[next]) {
		    int d1 = Math.abs(v - arr[i]);
		    int d2 = Math.abs(v - arr[next]);
		    int ndx = d1 < d2 ? i : next;
		    int d = d1 < d2 ? arr[i] : arr[next];
		    return new Diff(start, ndx, v, d, Math.min(d1, d2));
		}
	    }
	}
	throw new IllegalStateException("Diff must have been found");
    }

    private static class Diff {
	private int ndx1, ndx2, val1, val2, diff;

	public Diff() {

	}

	public Diff(int ndx1, int ndx2, int val1, int val2, int diff) {
	    this.ndx1 = ndx1;
	    this.ndx2 = ndx2;
	    this.val1 = val1;
	    this.val2 = val2;
	    this.diff = diff;
	}

	@Override
	public String toString() {
	    return "Diff [ndx1=" + ndx1 + ", ndx2=" + ndx2 + ", val1=" + val1 + ", val2=" + val2 + ", diff=" + diff
		    + "]";
	}
    }
}
