package org.haldokan.edge.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My implementation of a prefix trie inspired by descriptions of the algorithm on-line - chars are not stored explicitly
 * in the Trie. They are rather represented by their ascii codes in each node children
 * The Question: 4_STAR
 * Created by haytham.aldokanji on 5/18/16.
 */
public class PrefixTrie {
    private final Node root;

    public PrefixTrie() {
        this.root = new Node();
    }

    public static void main(String[] args) {
        PrefixTrie trie = new PrefixTrie();
        trie.insert("Hello There!");
        trie.testExactMatches();
        trie.testApproximateMatches();
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

    public boolean hasString(String string) {
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

    // the DFS code in this method is copied from code I found online in response to a Google interview question
    // Note that this works only if the search string shares prefixes and of length equal or greater than the strings
    // in the prefix tree (look at the test case to see).
    private boolean approximateMatch(Node node, String string, int position, int count, int maxDiff) {
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
                        && approximateMatch(currentChild, string, position + 1, count, maxDiff)
                        || string.charAt(position) != i
                        && approximateMatch(currentChild, string, position + 1, count + 1, maxDiff)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void testExactMatches() {
        assertThat(hasString("H"), is(true));
        assertThat(hasString("Hell"), is(true));
        assertThat(hasString("Hello"), is(true));
        assertThat(hasString("Hello "), is(true));
        assertThat(hasString("Hello There!"), is(true));
        assertThat(hasString("Hello X"), is(false));
        assertThat(hasString("I say Hello There!"), is(false));

        insert("I say Hello There!");
        assertThat(hasString("I say Hello There!"), is(true));
        assertThat(hasString("I say Hello Dear!"), is(false));

        //'I say Hello ' part is already inserted so it is not inserted again (but still the prefix trie
        // is space expensive
        insert("I say Hello Dear!");
        assertThat(hasString("I say Hello Dear!"), is(true));
    }

    private void testApproximateMatches() {
        assertThat(approximateMatch(root, "Hallo", 0, 0, 1), is(true));
        assertThat(approximateMatch(root, "Holla", 0, 0, 2), is(true));
        assertThat(approximateMatch(root, "Howdy", 0, 0, 4), is(true));

        assertThat(approximateMatch(root, "Hell", 0, 0, 1), is(false));
        assertThat(approximateMatch(root, "xHello", 0, 0, 1), is(false));
        assertThat(approximateMatch(root, "Helloxx", 0, 0, 2), is(true));
    }


    private static class Node {
        // all ascii chars
        private final Node[] children = new Node[256];

        public static Node create() {
            return new Node();
        }
    }
}
