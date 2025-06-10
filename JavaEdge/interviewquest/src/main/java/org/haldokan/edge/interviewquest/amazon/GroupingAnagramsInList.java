package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * <p>
 * Given a set of random strings, write a function that returns a set that groups all the anagrams together.
 * An anagram of a string is another string that contains the same characters, only the order of characters can be different.
 * For example, “abcd” and “dabc” are an Anagram of each other.
 * <p>
 * Ex: star, dog, cars, rats, arsc - > {(star, rats), (arsc, cars), dog)
 * <p>
 * Created by haytham.aldokanji on 7/6/16.
 */
public class GroupingAnagramsInList {

    public static void main(String[] args) {
        List<List<String>> groupings = group(Lists.newArrayList("star", "tsar", "dog", "cars", "rats", "arsc"));
        groupings.sort((g1, g2) -> g2.size() - g1.size());
        System.out.printf("%s%n", groupings);

        assertThat(groupings.size(), is(3));
        assertThat(groupings.get(0), containsInAnyOrder("star", "tsar", "rats"));
        assertThat(groupings.get(1), containsInAnyOrder("arsc", "cars"));
        assertThat(groupings.get(2), containsInAnyOrder("dog"));
    }

    public static List<List<String>> group(List<String> words) {
        Multimap<Integer, String> groups = HashMultimap.create();
        words.forEach(word -> {
            int hash = 0x0000;
            for (char chr : word.toCharArray()) {
                hash ^= chr;
            }
            groups.put(hash, word);
        });

        List<List<String>> result = new ArrayList<>();
        groups.keySet().forEach(key -> {
            result.add(Lists.newArrayList(groups.get(key)));
        });

        return result;
    }
}
