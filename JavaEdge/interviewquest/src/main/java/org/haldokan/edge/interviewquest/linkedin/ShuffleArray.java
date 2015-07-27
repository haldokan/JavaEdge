package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Random;

/**
 * My solution to a Linkedin interview question
 * 
 * Shuffle an array to make its elements equally likely
 * 
 * @author haldokan
 *
 */
public class ShuffleArray {
    public static void main(String[] args) {
	ShuffleArray shuffler = new ShuffleArray();
	int[] arr = new int[]{3, 3, 3, 6, 7, 8, 9, 10, 6, 11, 8, 9, 12, 7, 5, 5, 5, 10, 11, 6, 6};
	shuffler.shuffle(arr);
	System.out.println(Arrays.toString(arr));
    }
    
    // the idea is to swap each element in the array with a random element the comes after it
    public void shuffle(int[] arr) {
	Random rand = new Random();
	for (int i = 0; i < arr.length; i++) {
	    int tmp = arr[i];
	    int randIndex = (rand.nextInt(arr.length) + i) % arr.length;
	    arr[i] = arr[randIndex];
	    arr[randIndex] = tmp;
	}
    }

}
