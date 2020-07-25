package org.haldokan.edge.interviewquest.bloomberg;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * My implementation to a Bloomberg interview question - hard to tell what the question is looking for. The solution I
 * present here in O(n^2) in the lenght of input. Alternative solutions include using a special prefix tree to represent
 * the dictionary marking words boundaries with special char. W/o context it is hard to to figure out what solutions to favor.
 *
 * The Question: 3-STAR
 *
 * <p>
 * Given a string and a dictionary divide the string into parts that all exist in the dictionary
 * <p>
 * Example:
 * string: applepiepeach
 * dictionary: app apple pie each peach hello pea
 * <p>
 * output should be: app apple pie pea peach each
 * <p>
 * 07/25/20
 */
public class SegmentStringBasedOnDictionary {
    public static void main(String[] args) {
        System.out.println(segment("applepiepeach", Sets.newHashSet("app", "apple", "pie", "each", "peach", "hello", "pea")));
    }

    static List<String> segment(String input, Set<String> dict) {
        List<String> segments = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            StringBuilder word = new StringBuilder();
            word.append(input.charAt(i));

            for (int j = i + 1; j < input.length(); j++) {
                if (dict.contains(word.toString())) {
                    segments.add(word.toString());
                }
                word.append(input.charAt(j));
            }
            // this is to add words at the end of input (is there a better way to do the input?)
            if (dict.contains(word.toString())) {
                segments.add(word.toString());
            }
        }
        return segments;
    }
}
