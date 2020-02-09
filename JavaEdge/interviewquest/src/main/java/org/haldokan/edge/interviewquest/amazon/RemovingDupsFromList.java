package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

/**
 * My Solution to an Amazon interview question. Assume we don't have the Set data structure
 * The Question: 3_STAR
 * 1. Write a function that removes the duplicate of a collection of numbers and returns the number of elements
 * remaining in the collection after the duplicates have been removed.
 */
public class RemovingDupsFromList {

    public static void main(String[] args) {
        System.out.println(removeDups(new LinkedList<>(Arrays.asList(1, 1, 1, 3, 3, 4, 5, 6, 5, 6, 2))));
    }

    // todo there might exist a bit logic operator and shift that can tell if the same numbers is used twice...
    private static List<Integer> removeDups(List<Integer> list) {
        // using a BitSet is very space efficient
        BitSet bitSet = new BitSet();
        for (Iterator<Integer> it = list.iterator(); it.hasNext(); ) {
            Integer num = it.next();
            if (bitSet.get(num)) {
                it.remove();
            } else {
                bitSet.set(num);
            }
        }
        return list;
    }
}
