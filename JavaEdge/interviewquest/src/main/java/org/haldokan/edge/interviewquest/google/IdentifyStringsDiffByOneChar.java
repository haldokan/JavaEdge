package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - used a Prefix Trie. The matching algorithm is not as powerful as the dynamic
 * programming solution I present here: org.haldokan.edge.dynamicprog.ApproximateStringMatcher
 * <p>
 * The Question (4_STARS):
 *
 * Given a string and an array of strings, find whether the array contains a string with one character difference from the
 * given string. Array may contain strings of different lengths.
 * <p>
 * Ex: Given string
 * banana
 * and array is
 * [bana, apple, banaba, bonanza, banamf]
 * </p>
 * he output should be true as banana and banaba are one character difference.
 * Created by haytham.aldokanji on 5/18/16.
 */
public class IdentifyStringsDiffByOneChar {
    private final PrefixTrie prefixTrie = new PrefixTrie();

    public static void main(String[] args) {
        IdentifyStringsDiffByOneChar driver = new IdentifyStringsDiffByOneChar();
        driver.test();
    }

    public boolean differentByNChars(String string, int maxDiff) {
        return prefixTrie.approxMatch(string, maxDiff);
    }

    private void test() {
        prefixTrie.insert("bana");
        prefixTrie.insert("apple");
        prefixTrie.insert("banaba");
        prefixTrie.insert("bonanza");
        prefixTrie.insert("banamf");

        assertThat(differentByNChars("banana", 1), is(true));
        assertThat(differentByNChars("banano", 1), is(false));
        assertThat(differentByNChars("banano", 2), is(true));
    }

    private static class PrefixTrie {
        private final Node root;

        public PrefixTrie() {
            this.root = new Node();
        }

        public PrefixTrie insert(String string) {
            if (string == null || string.isEmpty()) {
                return this;
            }
            char[] chars = string.toCharArray();
            Node currentNode = root;

            for (char chr : chars) {
                // note how the chr is represented by the index of its ascii code - neat!
                Node currentChild = currentNode.children[chr];
                if (currentChild != null) {
                    currentNode = currentChild;
                } else {
                    currentChild = Node.create();
                    currentNode.children[chr] = currentChild;
                    currentNode = currentChild;
                }
            }
            return this;
        }

        public boolean exactMatch(String string) {
            if (string == null || string.isEmpty()) {
                return true;
            }
            char[] chars = string.toCharArray();
            Node currentNode = root;

            for (char chr : chars) {
                Node currentChild = currentNode.children[chr];
                if (currentChild != null) {
                    currentNode = currentChild;
                } else {
                    return false;
                }
            }
            return true;
        }

        public boolean approxMatch(String string, int maxDiff) {
            return dfs(root, string, 0, 0, maxDiff);
        }

        // the DFS code in this method is mostly copied from code I found online in response to a Google interview question
        // Note that this works only if the search string shares prefixes and of length equal or greater than the strings
        // in the prefix tree (have a look at the provided test).
        private boolean dfs(Node node, String string, int position, int count, int maxDiff) {
            if (count > maxDiff) {
                return false;
            }
            //otherwise shorter strings match any other string that has the same prefix
            if (position == string.length()) {
                return count == maxDiff;
            }
            for (int i = 0; i < 256; ++i) {
                Node currentChild = node.children[i];
                if (currentChild != null) {
                    if (string.charAt(position) == i
                            && dfs(currentChild, string, position + 1, count, maxDiff)
                            || string.charAt(position) != i
                            && dfs(currentChild, string, position + 1, count + 1, maxDiff)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class Node {
        // all ascii chars
        private final Node[] children = new Node[256];

        public static Node create() {
            return new Node();
        }
    }
}
