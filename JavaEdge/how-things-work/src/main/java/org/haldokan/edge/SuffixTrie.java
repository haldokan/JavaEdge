package org.haldokan.edge;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Suffix tries are very space-heavy, hence in practice, suffix trees or suffix automata are.
 * | Suffix Trie                     | Suffix Tree                |
 * | ------------------------------- | -------------------------- |
 * | Explicitly stores all suffixes  | Compresses repeated edges  |
 * | High space usage (`O(n²)`)      | Lower space (`O(n)`)       |
 * | Easier to implement             | More complex               |
 * | Good for education / small data | Used in production systems |
 * <p>
 * banana:
 * (root)
 * ├─ b ─ a ─ n ─ a ─ n ─ a*
 * ├─ a ─ n ─ a ─ n ─ a*
 * ├─ n ─ a ─ n ─ a*
 * ├─ a ─ n ─ a*
 * ├─ n ─ a*
 * └─ a*
 * 06/10/25
 */
public class SuffixTrie {
    private final Node root = new Node();

    public void insert(String word) {
        for (int i = 0; i < word.length(); i++) {
            var current = root;
            var suffix = word.substring(i);
            for (char c : suffix.toCharArray()) {
                current = current.children.computeIfAbsent(c, k -> new Node());
            }
            current.end = true;
        }
    }

    public boolean containsSuffix(String suffix) {
        var current = root;
        for (char c : suffix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return false;
            }
        }
        return true;
    }

    private static final class Node {
        Map<Character, Node> children = new HashMap<>();
        private boolean end;
    }

    @Test
    public void testId() {
        insert("banana");
        System.out.println(containsSuffix("banana"));
        System.out.println(containsSuffix("anana"));
        System.out.println(containsSuffix("nana"));
        System.out.println(containsSuffix("ana"));
        System.out.println(containsSuffix("na"));
        System.out.println(containsSuffix("a"));

        System.out.println(containsSuffix("ab"));
    }

    @Test
    public void detectPlagiarism() {
        var essay1 = "Einstein found out that the speed of light is e = mc2 and nothing can go faster";
        var cheeter = "I discovered that the speed of light is e = mc2";

        insert(essay1);

        String longestSuffix = "";
        for (int i = 0; i < cheeter.length(); i++) {
            var suffix = cheeter.substring(i);
            if (containsSuffix(suffix) && suffix.length() > longestSuffix.length()) {
                longestSuffix = suffix;
            }
        }
        System.out.println(longestSuffix); // that the speed of light is e = mc2
    }
}
