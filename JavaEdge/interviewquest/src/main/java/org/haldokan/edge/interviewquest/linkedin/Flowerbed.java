package org.haldokan.edge.interviewquest.linkedin;

import java.util.Deque;
import java.util.LinkedList;

/**
 * My Solution to Linkedin interview question
 * I think using a stack to track the empty spots is a valid idea. However somehow when I try to translate that to
 * available spots some use cases don't pass. I give up on this little sucker.
 * <p>
 * Suppose you have a long flowerbed in which some of the plots are planted and some are not. However, flowers cannot be
 * planted in adjacent plots - they would compete for water and both would die. Given a flowerbed (represented as an
 * array containing booleans), return if a given number of new flowers can be planted in it without violating the
 * no-adjacent-flowers rule Sample inputs
 * <p>
 * Input 1,0,0,0,0,0,1,0,0: 3 => true; 4 => false
 * <p>
 * Input 1,0,0,1,0,0,1,0,0: 1 => true; 2 => false
 * <p>
 * input 0: 1 => true; 2 => false
 * <p>
 * <p>
 * public boolean canPlaceFlowers(List<Boolean> flowerbed, int numberToPlace)
 *
 * @author haldokan
 */

public class Flowerbed {
    public static void main(String[] args) {
        Flowerbed fb = new Flowerbed();
        boolean[] bed = new boolean[]{true, false, false, false, false, false, true, false, false}; // ok
        // boolean[] bed = new boolean[] {true, false, false, true, false, false, true, false, false, true};
        // boolean[] bed = new boolean[] { true, false, false, true };
        // boolean[] bed = new boolean[] {false, true, false, false, false, true}; // XXX
        // boolean[] bed = new boolean[] { true, false, false, false, true }; //ok
        // boolean[] bed = new boolean[] { false, false, false, false }; //ok

        System.out.println(fb.canPlaceFlowers(bed, 3));
    }

    public boolean canPlaceFlowers(boolean[] flowerbed, int numberToPlace) {
        Deque<Boolean> deck = new LinkedList<>();
        int emptySpots = 0;
        for (Boolean spot : flowerbed) {
            Boolean top = deck.peekFirst();
            if (top == null) {
                deck.add(spot);
                emptySpots++;
            } else if (!top && !spot) {
                deck.push(spot);
                emptySpots++;
            } else if (!top && spot) {
                deck.pop();
                deck.push(spot);
                emptySpots--;
            } else if (top && !spot) {
                deck.pop();
            }
        }

        System.out.println("deck " + deck);
        System.out.println("empty " + emptySpots);
        int avSpots = (emptySpots + 1) / 2;
        System.out.println("avl " + avSpots);
        return (avSpots >= numberToPlace);
    }
}