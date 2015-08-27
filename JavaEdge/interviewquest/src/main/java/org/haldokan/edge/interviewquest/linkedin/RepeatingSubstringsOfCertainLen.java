package org.haldokan.edge.interviewquest.linkedin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * My solution to a Linkedin interview question
 * <p>
 * Find all the repeating sub-string sequence of specified length in a large string sequence. The sequences returned
 * i.e. the output must be sorted alphabetically.
 * <p>
 * For e.g.
 * <p>
 * Input String: "ABCACBABC" repeated sub-string length: 3
 * <p>
 * Output: ABC
 * <p>
 * Input String: "ABCABCA" repeated sub-string length: 2
 * <p>
 * Output: AB, BC, CA
 *
 * @author haldokan
 */
public class RepeatingSubstringsOfCertainLen {
    public static void main(String[] args) {
        RepeatingSubstringsOfCertainLen driver = new RepeatingSubstringsOfCertainLen();
        System.out.println(driver.rsubstring("ABCABCA", 2));
        System.out.println(driver.rsubstring("ABCABCAABCABCAABCABCA", 2));
        System.out.println(driver.rsubstring("ABCACBABC", 3));
    }

    public List<String> rsubstring(String s, int len) {
        if (s == null || len == 0 || s.length() < len * 2)
            return Collections.emptyList();

        Map<String, Integer> countBySubstring = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            int ssEndIndex = i + len;
            if (ssEndIndex <= s.length()) {
                countBySubstring.compute(s.substring(i, ssEndIndex), (k, v) -> v == null ? 1 : v + 1);
            }
        }
        return countBySubstring.entrySet().stream().filter(e -> e.getValue() > 1).map(e -> e.getKey()).sorted()
                .collect(Collectors.toList());
    }
}
