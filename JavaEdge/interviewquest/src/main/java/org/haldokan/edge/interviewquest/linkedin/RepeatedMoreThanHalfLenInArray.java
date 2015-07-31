package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Collections;

/**
 * My solution to a Linkedin interview question
 * 
 * Write a program to find the element in an array that is repeated more than half number of times. Return -1 if no such
 * element is found.
 * 
 * @author haldokan
 *
 */
public class RepeatedMoreThanHalfLenInArray {
    public static void main(String[] args) {
	RepeatedMoreThanHalfLenInArray driver = new RepeatedMoreThanHalfLenInArray();
	System.out.println(driver.repeatedMoreThanHalf(new Integer[]{1, 2, 3, 4, 4, 4, 4, 4, 5}));
	System.out.println(driver.repeatedMoreThanHalf(new Integer[]{1, 2, 3, 4, 4, 5}));
    }
    
    // this can be done using a map and counting the occrances as we go with O(n) complexity, however since I have solved
    // another similar problem I am going to solve this by sorting the arr and scanning with complexity of nlog(n).
    public int repeatedMoreThanHalf(Integer[] arr) {
	Collections.sort(Arrays.asList(arr));
	int len = 0;
	for (int i : arr) {
	    int e1 = arr[i];
	    if (i + 1 < arr.length) {	
		int e2 = arr[i + 1];
		if (e1 == e2) {
		    len++;
		    if (len > (arr.length + 1)/2) 
			return e1;
		} else {
		    len = 0;
		}
	    }
	}
	return -1;
    }
}
