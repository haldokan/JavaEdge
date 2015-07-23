package org.haldokan.edge.lunchbox;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO FIX - Not working for the general case
 * 
 * My implementation of a linkedin-interview-question found on Careercup 0 of 0 votes 19 Answers
 *
 * Suppose you have a long flowerbed in which some of the plots are planted and some are not. However, flowers cannot be
 * planted in adjacent plots - they would compete for water and both would die. Given a flowerbed (represented as an
 * array containing booleans), return if a given number of new flowers can be planted in it without violating the
 * no-adjacent-flowers rule Sample inputs
 *
 * Input 1,0,0,0,0,0,1,0,0: 3 => true; 4 => false
 *
 * Input 1,0,0,1,0,0,1,0,0: 1 => true; 2 => false
 *
 * input 0: 1 => true; 2 => false
 *
 *
 * public boolean canPlaceFlowers(List<Boolean> flowerbed, int numberToPlace)
 *
 * @author haldokan
 *
 */

public class Flowerbed {
    public static void main(String[] args) {
	Flowerbed fb = new Flowerbed();
	// boolean[] bed = new boolean[] { true, false, false, false, false, false, true, false, false };
	// boolean[] bed = new boolean[] {true, false, false, true, false, false, true, false, false, true};
	// boolean[] bed = new boolean[] { true, false, false, true };
	// boolean[] bed = new boolean[] {false, true, false, false, false, true};
	boolean[] bed = new boolean[] { true, false, false, false, true };
	// boolean[] bed = new boolean[] { false, false, false, false };

	fb.canPlaceFlowers(bed, 3);
    }

    public int canPlaceFlowers1(boolean[] flowerbed, int numberToPlace) {
	int availablePlaces = 0;
	boolean prev = false;
	boolean pendingPlaceAdded = false;
	for (boolean curr : flowerbed) {
	    if (!(curr || prev)) {
		availablePlaces++;
		pendingPlaceAdded = true;
	    }
	    if (curr && pendingPlaceAdded) {
		availablePlaces--;
		pendingPlaceAdded = false;
	    }
	    prev = curr;
	}
	return availablePlaces;
    }

    public int canPlaceFlowers2(boolean[] flowerbed, int numberToPlace) {
	Deque<Boolean> deck = new LinkedList<>();
	for (boolean spot : flowerbed) {
	    if (deck.isEmpty()) {
		deck.add(spot);
		continue;
	    }
	    if (!(spot || deck.getFirst())) {
		deck.addFirst(spot);
	    } else if (spot || deck.getFirst()) {
		deck.removeFirst();
		if (spot)
		    deck.addFirst(spot);
	    }
	}
	if (deck.getLast())
	    deck.removeLast();
	System.out.println(deck);
	return deck.size();
    }

    public boolean canPlaceFlowers(boolean[] flowerbed, int numberToPlace) {
	// add check for empty array and 0 numberToPlace
	if (numberToPlace > (flowerbed.length + 1) / 2)
	    return false;

	int k = 1;

	int avspots = 0;
	for (int i = 0; i < flowerbed.length; i += k) {
	    boolean start = flowerbed[i];
	    int stretch = 0;
	    for (int j = i; j < flowerbed.length || !flowerbed[i]; j++) {
		stretch++;
		k++;
	    }
	    boolean end = flowerbed[k];
	    if (start && end) {
		stretch -= 2;
		avspots += stretch / 2;
	    }
	    System.out.println(avspots);
	}
	return false;
    }
}