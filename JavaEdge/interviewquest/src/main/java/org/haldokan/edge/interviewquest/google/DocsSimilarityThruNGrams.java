package org.haldokan.edge.interviewquest.google;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * My solution to a Google interview question. It really all depends on what they mean by "efficient". My sln can be
 * made more efficient in space since we can intersect the 2nd doc on-the-go w/o parsing it to a set, and by removing
 * any matching ngrams from doc1 materialized set.
 *
 * The Question: 3_STAR
 *
 * You are given 2 documents. We want to know how similar they are through N-Grams.
 * <p>
 * Given an input n (n = number of word in the Ngram) and two documents(strings) find the intersection of the NGrams of
 * two document.
 * <p>
 * E.g doc1 = 'This is a dog', doc2 = 'This is a cat', n = 3. Ngrams for doc1 = 'This is a', 'is a dog'. Ngrams for doc2
 * = 'This is a', 'is a cat'
 * <p>
 * Output 'This is a'
 * <p>
 * Find an efficient way and give its complexity.
 *
 * @author haldokan
 */
public class DocsSimilarityThruNGrams {

    public static void main(String[] args) {
        DocsSimilarityThruNGrams driver = new DocsSimilarityThruNGrams();
        String doc1 = "This is a dog that is smart";
        String doc2 = "This is a cat that is smart too";
        System.out.println(driver.ngramIntersect(doc1, doc2, 3));
    }

    public Set<String> ngramIntersect(String doc1, String doc2, int ngramLen) {
        if (doc1 == null || doc2 == null || ngramLen < 1)
            return Collections.emptySet();

        // we can split on \\W but we will keep it simple
        String[] words1 = doc1.split("\\s");
        String[] words2 = doc2.split("\\s");

        if (words1.length < ngramLen || words2.length < ngramLen)
            return Collections.emptySet();

        Set<String> doc1Ngrams = docNgrams(words1, ngramLen);
        Set<String> doc2Ngrams = docNgrams(words2, ngramLen);

        doc1Ngrams.retainAll(doc2Ngrams);
        return doc1Ngrams;
    }

    private Set<String> docNgrams(String[] words, int ngramLen) {
        Set<String> ngrams = new HashSet<>();
        int j = 0;
        for (int i = 0; i < words.length; i++) {
            j = i + ngramLen;
            String ngram = makeNgram(words, i, j);
            if (ngram != null)
                ngrams.add(ngram);
        }
        return ngrams;
    }

    private String makeNgram(String[] words, int start, int end) {
        StringBuilder ngram = new StringBuilder();
        if (end <= words.length) {
            for (int i = start; i < end; i++) {
                ngram.append(words[i]).append(' ');
            }
        }
        if (ngram.length() > 0) {
            ngram.deleteCharAt(ngram.length() - 1);
            return ngram.toString();
        }
        return null;
    }
}
