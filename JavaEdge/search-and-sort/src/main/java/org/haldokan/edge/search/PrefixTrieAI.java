package org.haldokan.edge.search;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PrefixTrieAI {
    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        char[] chars = word.toCharArray();
        TrieNode current = root;
        for (char c : chars) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return false;
        }
        return current.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return false;
        }
        return true;
    }

    private static final class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord = false;

        @Override
        public String toString() {
            return String.format("eow:%s-keys:%s", isEndOfWord, children.keySet());
        }
    }

    @Test
    public void testInsert() {
        insert("Cartoon");
        insert("Cart");
        insert("Cartoons");
        insert("Carpool");
        insert("Aztec");
        System.out.println(root);
        // * indicates isEndOfWord=true
        //                           (root)
        //                         /     \
        //                        A       C
        //                        |       |
        //                        Z       A
        //                        |       |
        //                        T       R
        //                        |      / \
        //                        E     T*  P
        //                        |     |    \
        //                        C*    O     O
        //                              |      \
        //                              O       O
        //                              |        \
        //                              N*        L*
        //                              |
        //                              S*
    }

    @Test
    public void testSearch() {
        insert("Cartoon");
        insert("Cart");
        insert("Cartoons");
        insert("Carpool");
        insert("Aztec");
        System.out.println(root);

        System.out.println(search("Cartoon"));
        System.out.println(search("Cart"));
        System.out.println(search("Cartoons"));
        System.out.println(search("Carpool"));
        System.out.println(search("Aztec"));
        System.out.println(search("Carts"));
        System.out.println(search("Romans"));

        System.out.println(search("Car")); // My search covers prefixes
        System.out.println(search("Car")); // The AI's makes sure the 'word' is there
    }

    @Test
    public void testStartsWith() {
        insert("Cartoon");
        insert("Cart");
        insert("Cartoons");
        insert("Carpool");
        insert("Aztec");

        System.out.println(startsWith("Car"));
        System.out.println(startsWith("Car"));
    }
}
