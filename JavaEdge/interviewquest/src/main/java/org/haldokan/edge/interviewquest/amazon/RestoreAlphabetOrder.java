package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question - This is a directed acyclic graph problem (DAG). I construct the DAG and do a DFS
 * descend which yield what is termed topographical sort: basically the ordered alphabet
 * <p>
 * The Question: 4.5-STAR
 * <p>
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
        List<Character> orderedAlphabet = driver.restoreOrder(new Tuple[]{
                new Tuple('C', 'D'),
                new Tuple('C', 'E'),
                new Tuple('C', 'K'),
                new Tuple('D', 'E'),
                new Tuple('A', 'C'),
                new Tuple('B', 'C'),
                new Tuple('E', 'H'),
                new Tuple('H', 'K'),
                new Tuple('H', 'I'),
                new Tuple('I', 'K'),
                new Tuple('A', 'B')
        });
        System.out.println(orderedAlphabet);
    }

    private List<Character> restoreOrder(Tuple[] alphabet) {
        Multimap<Character, Character> adjacencyList = createAdjacencyList(alphabet);
        System.out.println(adjacencyList);

        Set<Character> reverseOrderedAlphabet = new LinkedHashSet<>();
        Set<Character> charSet1 = Arrays.stream(alphabet).map(t -> t.char1).collect(Collectors.toSet());
        Set<Character> charSet2 = Arrays.stream(alphabet).map(t -> t.char2).collect(Collectors.toSet());

        char start = Sets.difference(charSet1, charSet2).stream().findFirst().orElseThrow();
        System.out.println("start: " + start);
        topographicSort(adjacencyList, reverseOrderedAlphabet, start);

        List<Character> orderedAlphabet = new LinkedList<>(); // better than array list for inserting at the start (no shift)
        reverseOrderedAlphabet.forEach(chr -> orderedAlphabet.add(0, chr));
        return orderedAlphabet;
    }

    private List<Character> restoreOrder2(Tuple[] alphabet) {
        Multimap<Character, Character> adjacencyList = createAdjacencyList(alphabet);
        System.out.println(adjacencyList);

        Set<Character> charSet1 = Arrays.stream(alphabet).map(t -> t.char1).collect(Collectors.toSet());
        Set<Character> charSet2 = Arrays.stream(alphabet).map(t -> t.char2).collect(Collectors.toSet());

        char start = Sets.difference(charSet1, charSet2).stream().findFirst().orElseThrow();
        System.out.println("start: " + start);

        String ordered = topographicSort(adjacencyList, start);
        System.out.println("ordered " + ordered);
        return Collections.emptyList();
    }

    private Multimap<Character, Character> createAdjacencyList(Tuple[] alphabet) {
        Multimap<Character, Character> adjacencyList = ArrayListMultimap.create();
        for (Tuple tuple : alphabet) {
            adjacencyList.put(tuple.char1, tuple.char2);
        }
        return adjacencyList;
    }

    // the dfs magic
    private void topographicSort(Multimap<Character, Character> adjacencyList, Set<Character> result, Character chr) {
        for (Character adjacent : adjacencyList.get(chr)) {
            if (!result.contains(adjacent)) {
                System.out.println("topo: char: " + chr + ", adjacent: " + adjacent);
                topographicSort(adjacencyList, result, adjacent);
            }
        }
        System.out.println("add: " + chr);
        result.add(chr);
    }

    // This does only one DFS: A -> -> D -> E -> H -> K (does not work)
    private String topographicSort(Multimap<Character, Character> adjacencyList, Character chr) {
        for (Character adjacent : adjacencyList.get(chr)) {
            System.out.println("topo: char: " + chr + ", adjacent: " + adjacent);
            return topographicSort(adjacencyList, adjacent) + chr;
        }
        System.out.println("return: " + chr);
        return String.valueOf(chr);
    }

    private record Tuple(Character char1, Character char2) {}
}
