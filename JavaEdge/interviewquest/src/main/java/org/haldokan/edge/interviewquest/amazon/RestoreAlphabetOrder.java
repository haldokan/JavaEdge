package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * My solution to an Amazon interview question - This is a directed acyclic graph problem (DAG). I construct the DAG and do a DFS
 * descend which yield what is termed topographical sort: basically the ordered alphabet
 *
 * The Question: 4.5-STAR
 *
 * Given an alphabet where we do not know the order of the letters also do not know the number of letters.
 * We are given an input list of tuples where each entry in the list gives an ordering between 2 letters
 * Determine the alphabet order.
 * <p>
 * Ex-
 * <A, B>
 * <C, D>
 * <C, E>
 * <D, E>
 * <A, C>
 * <B, C>
 * Order is A, B, C, D, E
 */
public class RestoreAlphabetOrder {

    public static void main(String[] args) {
        RestoreAlphabetOrder driver = new RestoreAlphabetOrder();
        List<Character> orderedAlphabet = driver.restoreOrder(new Character[][]{
                new Character[]{'A', 'B'},
                new Character[]{'C', 'D'},
                new Character[]{'C', 'E'},
                new Character[]{'D', 'E'},
                new Character[]{'A', 'C'},
                new Character[]{'B', 'C'},
                new Character[]{'E', 'H'},
                new Character[]{'H', 'K'},
                new Character[]{'H', 'I'},
                new Character[]{'I', 'K'},
        });
        System.out.println(orderedAlphabet);
    }

    List<Character> restoreOrder(Character[][] alphabet) {
        Multimap<Character, Character> adjacencyList = createAdjacencyList(alphabet);
        System.out.println(adjacencyList);

        Set<Character> reverseOrderedAlphabet = new LinkedHashSet<>();
        topographicSort(adjacencyList, reverseOrderedAlphabet, alphabet[0][0]);

        List<Character> orderedAlphabet = new LinkedList<>(); // better than array list for inserting at the start (no shift)
        reverseOrderedAlphabet.forEach(chr -> orderedAlphabet.add(0, chr));
        return orderedAlphabet;
    }

    Multimap<Character, Character> createAdjacencyList(Character[][] alphabet) {
        Multimap<Character, Character> adjacencyList = ArrayListMultimap.create();

        for (Character[] ordering : alphabet) {
            adjacencyList.put(ordering[0], ordering[1]);
        }
        return adjacencyList;
    }

    // the dfs magic
    void topographicSort(Multimap<Character, Character> adjacencyList, Set<Character> visited, Character chr) {
        for (Character adjacent : adjacencyList.get(chr)) {
            topographicSort(adjacencyList, visited, adjacent);
            if (!visited.contains(chr)) {
                topographicSort(adjacencyList, visited, adjacent);
            }
        }
        visited.add(chr);
    }
}
