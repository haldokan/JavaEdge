package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * With extra space: sort asc then sort desc and weave the 2 arrays to produce a third array that has the required properties
 * Given unsorted array, sort it in such a way that the first
 * element is the largest value, the second element is the smallest,
 * the third element is the second largest element and so on.
 * [2, 4, 3, 5, 1] -> [5, 1, 4, 2, 3]
 * can you do it without using extra space
 *
 * public void sortAlternate(int[] nums){}
 *
 * Created by haldokanji on 4/16/17.
 */
public class AlternateSort {
    // 9 8 7 6 5 4 3 2 1 0
    public static void main(String[] args) {
        // n1, n2
        // n1 is even -> largest
        // n1 is odd -> smallest
        // n1=1, n2=9: arr[1]=0, arr[9]=8
        // n1 = 2: arr[2]=8, n2 = 8, arr[8] = 7;
    }

    public void sortAlternate(int[] numbers) {
    }
}
