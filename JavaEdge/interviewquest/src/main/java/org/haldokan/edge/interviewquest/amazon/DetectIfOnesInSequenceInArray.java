package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 *
 * The Question: 3.5-STAR
 *
 * Write a code to return a value 'True' if the number '1' throughout the array appears consecutively. Ex: S = {1,1,1,0,0,3,4}.
 * Else, return 'False' if the array does not have the given number (char = '1' in this case) in the consecutive order. Ex: S = {1,1,0,0,1,3,4}
 */
public class DetectIfOnesInSequenceInArray {
    public static void main(String[] args) {
        System.out.println(sequence(new int[]{1,1,1,0,0,3,4}));
        System.out.println(sequence(new int[]{1,1,0,0,1,3,4}));
        System.out.println(sequence(new int[]{0,0,1,3,4}));
        System.out.println(sequence(new int[]{1,0,0,3,4}));
        System.out.println(sequence(new int[]{0,0,3,1}));
        System.out.println(sequence(new int[]{0,0,3}));
    }
    static boolean sequence(int[] arr) {
        int pos = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                if (pos != -1 && i - pos > 1) {
                    return false;
                }
                pos = i;
            }
        }
        return pos != -1;
    }
}
