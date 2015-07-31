package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * My solution to a Linkedin interview question
 * 
 * Write a function that would return the 5th element from the tail (or end) of a singly linked list of integers, in one
 * pass, and then provide a set of test cases against that function (please do not use any list manipulation functions
 * that you do not implement yourself).
 * 
 * @author haldokan
 *
 */
public class FifthValFromLastInLinkedListIn1Pass {
    public static void main(String[] args) {
	FifthValFromLastInLinkedListIn1Pass driver = new FifthValFromLastInLinkedListIn1Pass();
	List<Integer> list1 = Arrays.asList(new Integer[]{1, 2, 3});
	List<Integer> list2 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
	List<Integer> list3 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7});
	List<Integer> list4 = Arrays.asList(new Integer[]{1});
	List<Integer> list5 = Collections.emptyList();
	
	System.out.println(driver.fifthFromLast(list1));
	System.out.println(driver.fifthFromLast(list2));
	System.out.println(driver.fifthFromLast(list3));
	System.out.println(driver.fifthFromLast(list4));
	System.out.println(driver.fifthFromLast(list5));
    }
    
    public Integer fifthFromLast(List<Integer> list) {
	if (list == null)
	    return null;
	int i1 = 0;
	int i2 = -5;
	for(;;) {
	    try {
		list.get(i1++);
		i2++;
	    } catch(IndexOutOfBoundsException e) {
		if (i1 >= 5)
		    return list.get(i2);
		return null;
	    }
	}
    }
}
