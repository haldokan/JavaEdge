package org.haldokan.edge.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My implementation of a prefix trie inspired by descriptions of the algorithm on-line - chars are not stored explicitely
 * in the trie. They are rather represented by their ascii codes in each node children
 * <p>
 * Created by haytham.aldokanji on 5/18/16.
 */
public class PrefixTrie {
    private final Node root;

    public PrefixTrie() {
        this.root = new Node();
    }

    public static void main(String[] args) {
        PrefixTrie trie = new PrefixTrie();
        trie.test();
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

    private void test() {
        PrefixTrie trie = new PrefixTrie();

        trie.insert("Hello There!");
        assertThat(trie.hasString("H"), is(true));
        assertThat(trie.hasString("Hell"), is(true));
        assertThat(trie.hasString("Hello"), is(true));
        assertThat(trie.hasString("Hello "), is(true));
        assertThat(trie.hasString("Hello There!"), is(true));
        assertThat(trie.hasString("Hello X"), is(false));
        assertThat(trie.hasString("I say Hello There!"), is(false));

        trie.insert("I say Hello There!");
        assertThat(trie.hasString("I say Hello There!"), is(true));
        assertThat(trie.hasString("I say Hello Dear!"), is(false));

        //'I say Hello ' part is already inserted so it is not inserted again (but still the prefix trie
        // is space expensive
        trie.insert("I say Hello Dear!");
        assertThat(trie.hasString("I say Hello Dear!"), is(true));
    }

    private static class Node {
        // all ascii chars
        private final Node[] children = new Node[256];

        public static Node create() {
            return new Node();
        }
    }
}
