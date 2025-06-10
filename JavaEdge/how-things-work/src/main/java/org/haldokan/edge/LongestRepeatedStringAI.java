package org.haldokan.edge;

import java.util.*;

/**
 * Do a depth-first search (DFS) from the root:
 *
 * Keep track of the current path (characters).
 *
 * At each node:
 *
 * If it has 2 or more children, it's a repeated prefix.
 *
 * Update the longest path found so far.
 * 06/10/25
 */
public class LongestRepeatedStringAI {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        int count = 0;  // How many suffixes visit this node
    }

    static class SuffixTrie {
        private final TrieNode root = new TrieNode();
        private String lrs = "";

        public SuffixTrie(String text) {
            build(text);
        }

        // Build the suffix trie from all suffixes of the string
        private void build(String text) {
            for (int i = 0; i < text.length(); i++) {
                insertSuffix(text.substring(i));
            }
        }

        private void insertSuffix(String suffix) {
            TrieNode current = root;
            for (char ch : suffix.toCharArray()) {
                current = current.children.computeIfAbsent(ch, c -> new TrieNode());
                current.count++; // This node has been visited by one more suffix
            }
        }

        // Public method to get the longest repeated substring
        public String getLongestRepeatedSubstring() {
            dfs(root, new StringBuilder());
            return lrs;
        }

        // DFS to find the longest path visited by more than 1 suffix
        private void dfs(TrieNode node, StringBuilder path) {
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                char ch = entry.getKey();
                TrieNode child = entry.getValue();

                if (child.count > 1) { // This path appears in multiple suffixes
                    path.append(ch);
                    if (path.length() > lrs.length()) {
                        lrs = path.toString();
                    }
                    System.out.println("path-recur: " + path);
                    dfs(child, path);
                    System.out.println("path-backtrack: " + path);
                    path.deleteCharAt(path.length() - 1); // backtrack
                }
            }
        }
    }

    public static void main(String[] args) {
        String input = "banana";
        SuffixTrie trie = new SuffixTrie(input);
        String lrs = trie.getLongestRepeatedSubstring();
        System.out.println("lrs: " + lrs);
    }
}
