package org.haldokan.edge.lunchbox;

import java.util.Arrays;

/**
 * Find the indexes of the shortest and longest streches of 1s, and 0s in the array
 * naming of variable in this class is shameful. the problem is generalized and solved in the another
 * class in this package LongestStretch
 * 
 * @author haldokan
 *
 */
public class Longest01Stretch {
    public static void main(String[] args) {
	// int[] a = new int[] { 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1 };
	int[] a = new int[] { 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1 };
	// int [] a = new int[]{};
	System.out.println(Arrays.toString(a));
	if (a == null || a.length == 0)
	    throw new IllegalArgumentException("array is null or empty");

	int s0, e0, s1, e1, cs0, cs1, ce0, ce1, c0, c1;
	s0 = e0 = s1 = e1 = cs0 = ce0 = cs1 = ce1 = -1;
	c0 = c1 = 0;
	int max0, max1;
	max0 = max1 = 0;
	int first = a[0];
	if (first == 0) {
	    cs0 = s0 = 0;
	    ce0 = e0 = 1;
	    c0 = 1;
	    max0 = 1;
	} else {
	    cs1 = s1 = 0;
	    ce1 = e1 = 1;
	    c1 = 1;
	    max1 = 1;
	}
	int prev = first;
	for (int i = 1; i < a.length; i++) {
	    if (a[i] == 1) {
		c1++;
		if (prev == 0) {
		    cs1 = i;
		    if (c0 > max0) {
			max0 = c0;
			s0 = cs0;
			e0 = i;
		    }
		    c0 = 0;
		}
		prev = a[i];
	    } else {
		c0++;
		if (prev == 1) {
		    cs0 = i;
		    if (c1 > max1) {
			max1 = c1;
			s1 = cs1;
			e1 = i;
		    }
		    c1 = 0;
		}
		prev = a[i];
	    }
	}
	if (c0 > max0) {
	    max0 = c0;
	    s0 = cs0;
	    e0 = a.length;
	}
	if (c1 > max1) {
	    max1 = c1;
	    s1 = cs1;
	    e1 = a.length;
	}

	System.out.println("s0/e0/max0/e0-s0/strech: " + s0 + "/" + e0 + "/" + max0 + "/" + (e0 - s0)
		+ Arrays.toString(Arrays.copyOfRange(a, s0, e0)));
	System.out.println("s1/e1/max1/e1-s1/strech: " + s1 + "/" + e1 + "/" + max1 + "/" + (e1 - s1)
		+ Arrays.toString(Arrays.copyOfRange(a, s1, e1)));
    }
}