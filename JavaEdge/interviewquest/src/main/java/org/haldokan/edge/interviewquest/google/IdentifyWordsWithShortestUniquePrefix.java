package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - used a modified Prefix Trie where nodes keep track of num of passes thru
 * them while constructing the tree
 *
 * The Question: 5_STAR
 *
 * Use the shortest unique prefix to represent each word in the array
 * input: ["zebra", "dog", "duck",”dot”]
 * output: {zebra: z, dog: do, duck: du}
 * <p>
 * [zebra, dog, duck, dove]
 * {zebra:z, dog: dog, duck: du, dove: dov}
 * <p>
 * [bearcat, bear]
 * {bearcat: bearc, bear: ""}
 * <p>
 * Created by haytham.aldokanji on 6/4/16.
 */
public class IdentifyWordsWithShortestUniquePrefix {

    public static void main(String[] args) {
        IdentifyWordsWithShortestUniquePrefix driver = new IdentifyWordsWithShortestUniquePrefix();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
    }

    private void test1() {
        PrefixTrie prefixTrie = new PrefixTrie();
        String[] words = new String[]{"zebra", "dog", "duck"};

        for (String word : words) {
            prefixTrie.insert(word);
        }
        assertThat(prefixTrie.shortestPrefix(words[0]), is("z"));
        assertThat(prefixTrie.shortestPrefix(words[1]), is("do"));
    }

    private void test2() {
        PrefixTrie prefixTrie = new PrefixTrie();
        String[] words = new String[]{"zebra", "dog", "duck", "dove"};

        for (String word : words) {
            prefixTrie.insert(word);
        }
        assertThat(prefixTrie.shortestPrefix(words[0]), is("z"));
        assertThat(prefixTrie.shortestPrefix(words[1]), is("dog"));
        assertThat(prefixTrie.shortestPrefix(words[2]), is("du"));
        assertThat(prefixTrie.shortestPrefix(words[3]), is("dov"));
    }

    private void test3() {
        PrefixTrie prefixTrie = new PrefixTrie();
        String[] words = new String[]{"bearcat", "bear", "bear"};

        for (String word : words) {
            prefixTrie.insert(word);
        }
        assertThat(prefixTrie.shortestPrefix(words[0]), is("bearc"));
        assertThat(prefixTrie.shortestPrefix(words[1]), is(""));
        assertThat(prefixTrie.shortestPrefix(words[2]), is(""));
    }

    private void test4() {
        PrefixTrie prefixTrie = new PrefixTrie();
        String[] words = new String[]{"zebra", "zip", "zigzag", "dog", "duck", "duke", "dove"};

        for (String word : words) {
            prefixTrie.insert(word);
        }
        assertThat(prefixTrie.shortestPrefix(words[0]), is("ze"));
        assertThat(prefixTrie.shortestPrefix(words[1]), is("zip"));
        assertThat(prefixTrie.shortestPrefix(words[2]), is("zig"));
        assertThat(prefixTrie.shortestPrefix(words[3]), is("dog"));
        assertThat(prefixTrie.shortestPrefix(words[4]), is("duc"));
        assertThat(prefixTrie.shortestPrefix(words[5]), is("duk"));
        assertThat(prefixTrie.shortestPrefix(words[6]), is("dov"));
    }

    private static class PrefixTrie {
        private final Node root;

        public PrefixTrie() {
            root = new Node();
        }

        public void insert(String string) {
            if (string == null || string.isEmpty()) {
                return;
            }
            char[] chars = string.toCharArray();
            Node currentNode = root;

            for (char chr : chars) {
                // note how the chr is represented by the index of its ascii code - neat!
                Node currentChild = currentNode.children[chr];
                if (currentChild != null) {
                    currentNode = currentChild;
                    currentNode.numPasses++;
                } else {
                    currentChild = Node.create();
                    currentNode.children[chr] = currentChild;
                    currentNode = currentChild;
                }
            }
        }

        public String shortestPrefix(String string) {
            if (string == null || string.isEmpty()) {
                return "";
            }
            char[] chars = string.toCharArray();
            Node currentNode = root;

            int index = 0;
            for (char chr : chars) {
                currentNode = currentNode.children[chr];
                if (currentNode.numPasses == 0) {
                    return string.substring(0, index + 1);
                }
                index++;
            }
            return "";
        }

        private static class Node {
            // all ascii chars
            private final Node[] children = new Node[256];
            private int numPasses;

            public static Node create() {
                return new Node();
            }
        }
    }
}

