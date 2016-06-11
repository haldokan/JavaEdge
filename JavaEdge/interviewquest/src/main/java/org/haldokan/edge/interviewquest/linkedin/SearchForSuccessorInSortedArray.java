package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * My solution to a Linkedin question.
 * The Question: 3_STAR
 * Return the smallest character that is strictly larger than the search character, If no such character exists, return
 * the smallest character in the array. We can assume the array/list is sorted in ascending order
 * <p>
 * Given the following inputs we expect the corresponding output:
 * <p>
 * ['c', 'f', 'j', 'p', 'v'], 'a' => 'c'
 * <p>
 * ['c', 'f', 'j', 'p', 'v'], 'c' => 'f'
 * <p>
 * ['c', 'f', 'j', 'p', 'v'], 'k' => 'p'
 * <p>
 * ['c', 'f', 'j', 'p', 'v'], 'z' => 'c' // The wrap around case
 * <p>
 * ['c', 'f', 'k'], 'f' => 'k'
 * <p>
 * ['c', 'f', 'k'], 'c' => 'f'
 * <p>
 * ['c', 'f', 'k'], 'd' => 'f'
 */
public class SearchForSuccessorInSortedArray {
    public static void main(String[] args) {
        SearchForSuccessorInSortedArray searcher = new SearchForSuccessorInSortedArray();
        List<Character> chars1 = Arrays.asList(new Character[]{'c', 'f', 'j', 'p', 'v'});

        System.out.println(searcher.findSuccessor(chars1, 'a')); // c
        System.out.println(searcher.findSuccessor(chars1, 'c')); // f
        System.out.println(searcher.findSuccessor(chars1, 'k')); // p
        System.out.println(searcher.findSuccessor(chars1, 'z')); // c

        List<Character> chars2 = Arrays.asList(new Character[]{'c', 'f', 'k'});
        System.out.println(searcher.findSuccessor(chars2, 'f')); // k
        System.out.println(searcher.findSuccessor(chars2, 'c')); // f
        System.out.println(searcher.findSuccessor(chars2, 'd')); // f

        // corner cases
        List<Character> chars3 = Arrays.asList(new Character[]{'c'});
        System.out.println(searcher.findSuccessor(chars3, 'a')); // c
        System.out.println(searcher.findSuccessor(chars3, 'd')); // c
    }

    public char findSuccessor(List<Character> chars, char c) {
        int index = Collections.binarySearch(chars, c);
        if (index == chars.size() - 1)
            return chars.get(0);
        if (index >= 0)
            return chars.get(index + 1);

        int succIndex = Math.abs(index + 1);
        if (succIndex >= chars.size() - 1)
            return chars.get(0);

        return chars.get(Math.abs(index + 1));
    }
}
