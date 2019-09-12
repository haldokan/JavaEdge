package org.haldokan.edge.interviewquest.facebook;

/**
 * Given an unsorted array, sort it in such a way that the first
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
    public static void main(String[] args) {

    }

    public void sortAlternate(int[] numbers){
        // 9 8 7 6 5 4 3 2 1 0
        //   |
        // 9 0 7 6 5 4 3 2 1 8
        //     |
        // 9 0 8 6 5 4 3 2 1 7
        //       |
        // 9 0 8 1 5 4 3 2 6 7
        //         |
        // 9 0 8 1 7 4 3 2 6 5
        //           |
        // 9 0 8 1 7 2 3 4 6 5
        //             |
        // 9 0 8 1 7 2
    }
}
