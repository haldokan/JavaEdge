package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;

/**
 * My solution to a Linkedin interview question.
 * 
 * Implement a method which takes an integer array and returns an integer
 * array (of equal size) in which each element is the product of every number in the input array with the exception of
 * the number at that index.
 *
 * Example: [3, 1, 4, 2] => [8, 24, 6, 12]
 */
public class SelfExcludingArrayProduct {
    public static void main(String[] args) {
	SelfExcludingArrayProduct productArr = new SelfExcludingArrayProduct();
	System.out.println(Arrays.toString(productArr.product(new int[] { 3, 1, 4, 2 })));
    }

    public int[] product(int[] input) {
	int[] rslt = new int[input.length];

	for (int i = 0; i < input.length; i++) {
	    int product = 1;
	    for (int j = 0; j < input.length; j++) {
		if (i != j) {
		    product *= input[j];
		}
	    }
	    rslt[i] = product;
	}
	return rslt;
    }
}
