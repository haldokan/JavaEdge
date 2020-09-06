package org.haldokan.edge;

/**
 * My implementation of Rabin-Karp algorithm for pattern matching in a text using rolling hash
 *
 * The Question: 5-STAR
 *
 * From Wikipedia (https://en.wikipedia.org/wiki/Rabin%E2%80%93Karp_algorithm):
 * The Rabin–Karp algorithm proceeds by computing, at each position of the text, the hash value of a string starting at
 * that position with the same length as the pattern. If this hash value equals the hash value of the pattern, it performs
 * a full comparison at that position.
 *
 * In order for this to work well, the hash function should be selected randomly from a family of hash functions that are
 * unlikely to produce many false positives, positions of the text which have the same hash value as the pattern but do
 * not actually match the pattern. These positions contribute to the running time of the algorithm unnecessarily,
 * without producing a match. Additionally, the hash function used should be a rolling hash, a hash function whose value
 * can be quickly updated from each position of the text to the next. Recomputing the hash function from scratch at each
 * position would be too slow.
 */
public class RabinKarpPatternSearch {
    private final int HASH_SEED = 31;
    private final long MODULO = Double.doubleToLongBits(Math.pow(2, 32));

    public static void main(String[] args) {
        RabinKarpPatternSearch driver = new RabinKarpPatternSearch();
        driver.test1();
    }

    void test1() {
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "InThis".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "ThisP".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "tern".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "find".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "ndT".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "ring".toCharArray()));
        System.out.println(findPatternIndex("findThisPatternInThisString".toCharArray(), "String".toCharArray()));
    }

    public int findPatternIndex(char[] text, char[] pattern){
        long patternHash = createHash(pattern, pattern.length);
        long textSnippetHash = createHash(text, pattern.length); // HAS to be the length of the pattern

        for (int i = 0; i < text.length - pattern.length + 1; i++) {
            // mostly when the hash is the same the strings should match (for good hash func)
            if(patternHash == textSnippetHash && assertEqual(text, i, i + pattern.length, pattern)) {
                return i;
            }
            if (i < text.length - pattern.length) {
                textSnippetHash = updateHash(text, i, i + pattern.length, textSnippetHash, pattern.length);
            }
        }
        return -1;
    }

    // rolling hash function - check docs at the top
    // this hash func enables us to remove a char from the begin of the hash string and add a char to the end of it and
    // the resulting hash is equal to the hash of the new string.
    private long createHash(char[] input, int patternLen){
        long hash = 0;
        for (int i = 0; i < patternLen; i++) {
            hash += input[i] * Math.pow(HASH_SEED, i) % MODULO; // use modulo arithmetics to avoid working with huge numbers
        }
        return hash;
    }

    // that's why we need rolling hash functions: the hash can be updated cheaply when we replace a char in the string we are hashing
    // oldIndex: is the text index that increases by one every time the snippet does not match the pattern
    // newIndex is the text index at the end of the snippet that increases by 1 every time there is no match
    private long updateHash(char[] text, int oldIndex, int newIndex, long currentHash, int patternLen) {
        long newHash = (currentHash - text[oldIndex]) / HASH_SEED; // this removes the hash of the first char and reduce the power factor of the hash seeds by one
        newHash += text[newIndex] * Math.pow(HASH_SEED, patternLen - 1) % MODULO; // add the hash of the new char
        return newHash;
    }

    private boolean assertEqual(char[] text, int start, int end, char[] pattern){
        if(end - start != pattern.length) {
            return false;
        }
        int index = 0;
        for (int i = start; i < end; i++) {
            if (text[i] != pattern[index++]) {
                return false;
            }
        }
        return true;
    }
}
