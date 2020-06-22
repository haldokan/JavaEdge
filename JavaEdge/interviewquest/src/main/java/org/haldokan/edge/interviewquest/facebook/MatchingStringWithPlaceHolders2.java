package org.haldokan.edge.interviewquest.facebook;

/**
 * My solution to a Facebook interview question - Use a trie with dfs recursive calls
 * The Question: 4_STAR
 * Write a program that answers YES/NO search queries containing * placeholders (this is not regex)
 * Example: if the data you have is (hazem, ahmed, moustafa, fizo), then you should answer as follows for:
 *  ahmed: YES
 *  m**stafa: YES
 *  fizoo: NO
 *  fizd: NO
 *  *****: YES
 *  ****: YES
 * *: NO
 *  Your program should be able to answer each search query in O(1).
 * <p>
 * Created by haytham.aldokanji on 5/13/16.
 */
public class MatchingStringWithPlaceHolders2 {

    public static void main(String[] args) {
        PrefixTrie trie = new PrefixTrie();
        trie.insert("Saturday");
        System.out.println(trie.approxMatch("S**urday"));
        System.out.println(trie.approxMatch("S**urkay"));
        System.out.println(trie.approxMatch("S**urdayx"));
        System.out.println(trie.approxMatch("S*turd*y"));
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

        public boolean approxMatch(String string) {
            return dfs(root, string, 0);
        }

        // the DFS code in this method is mostly copied from code I found online in response to a Google interview question
        // Note that this works only if the search string shares prefixes and of length equal or greater than the strings
        // in the prefix tree (have a look at the provided test).
        private boolean dfs(Node node, String string, int position) {
            //otherwise shorter strings match any other string that has the same prefix
            if (position == string.length()) {
                return true;
            }
            for (int i = 0; i < 128; ++i) {
                Node currentChild = node.children[i];
                if (currentChild != null) {
                    if (string.charAt(position) == i
                        && dfs(currentChild, string, position + 1)
                        || string.charAt(position) == '*'
                        && dfs(currentChild, string, position + 1)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class Node {
        // all ascii chars
        private final Node[] children = new Node[128];

        public static Node create() {
            return new Node();
        }
    }
}
