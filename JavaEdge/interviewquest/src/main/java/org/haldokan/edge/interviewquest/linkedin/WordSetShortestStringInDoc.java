package org.haldokan.edge.interviewquest.linkedin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * My solution to a Linkedin interview question
 *
 * Given a large document and a short pattern consisting of a few words (eg. W1 W2 W3), find the shortest string that
 * has all the words in any order (for eg. W2 foo bar dog W1 cat W3 -- is a valid pattern)
 *
 * I think I went on the wrong track and solved a different problem. The solution gets the shortest sentence (v.s.
 * string) that has the word set. It also needs to know how to cross-product a number of arrays known only at run time.
 * I assumed 3 which means we can only search for 3 words; Lame but I am done hoarding array indexes.
 *
 *
 *
 * @author haldokan
 *
 */
public class WordSetShortestStringInDoc {
    public static void main(String[] args) {
	String doc = "In the history of mankind there is no problem more puzzled about than the hen or the smooth elliptical egg theorem. "
		+ "At stake is the important question of who came first? the hen , which lied down the egg ? or the egg "
		+ "that hatched to hen . Up till now the question is not settled: hen first or the egg first, scratching head continues.";
	String[] searchWords = new String[] { "hen", "or", "egg" };

	WordSetShortestStringInDoc driver = new WordSetShortestStringInDoc();
	System.out.println(driver.shortestString(doc, searchWords));
    }

    private String shortestString(String doc, String[] searchWords) {
	if (searchWords == null || searchWords.length < 2)
	    throw new IllegalArgumentException("Must have 2 or more search words");

	String[] text = doc.split(" ");
	List<List<Integer>> textIndexBySearchWordIndex = indexText(text, searchWords);
	int[] indexes = getSearchWordsIndexesInShortestString(searchWords, textIndexBySearchWordIndex);
	System.out.println(Arrays.toString(indexes));
	return inferStringFromIndexes(text, indexes);
    }

    private int[] getSearchWordsIndexesInShortestString(String[] searchWords,
	    List<List<Integer>> textIndexBySearchWordIndex) {
	List<Integer> indexes = new ArrayList<>(Arrays.asList(new Integer[] { 0, 0, 0 }));
	int[] shortestIndexes = new int[searchWords.length];
	int shortest = -1;
	// TODO IMPL cross-product a dynamic number of arrays
	// three loops assuming we have 3 search words
	for (int i = 0; i < textIndexBySearchWordIndex.get(0).size(); i++) {
	    indexes.set(0, textIndexBySearchWordIndex.get(0).get(i));
	    for (int j = 0; j < textIndexBySearchWordIndex.get(1).size(); j++) {
		indexes.set(1, textIndexBySearchWordIndex.get(1).get(j));
		for (int k = 0; k < textIndexBySearchWordIndex.get(2).size(); k++) {
		    indexes.set(2, textIndexBySearchWordIndex.get(2).get(k));
		    List<Integer> indexesCopy = new ArrayList<>(indexes);
		    Collections.sort(indexesCopy);
		    int len = indexesCopy.get(indexesCopy.size() - 1) - indexesCopy.get(0);
		    if (shortest == -1 || len < shortest) {
			shortest = len;
			shortestIndexes[0] = indexesCopy.get(0);
			shortestIndexes[1] = indexesCopy.get(1);
			shortestIndexes[2] = indexesCopy.get(2);
		    }
		}
	    }
	}
	return shortestIndexes;
    }

    private String inferStringFromIndexes(String[] text, int[] indexes) {
	StringBuilder str = new StringBuilder();
	// printIndexedText(text);
	for (int i = indexes[0]; i <= indexes[indexes.length - 1]; i++) {
	    str.append(text[i]).append(" ");
	}
	return str.toString();
    }

    private List<List<Integer>> indexText(String[] text, String[] searchWords) {
	// we did not use a map bcz we want to support the case where the search words have identical subset of words
	List<List<Integer>> textIndexBySearchWordIndex = new ArrayList<>();
	for (int i = 0; i < searchWords.length; i++)
	    textIndexBySearchWordIndex.add(new ArrayList<Integer>());

	for (int textIndex = 0; textIndex < text.length; textIndex++) {
	    List<Integer> indexes = matchIndexes(text[textIndex], textIndex, searchWords);
	    for (Integer swIndex : indexes) {
		textIndexBySearchWordIndex.get(swIndex).add(textIndex);
	    }
	}
	return textIndexBySearchWordIndex;
    }

    private void printIndexedText(String[] text) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < text.length; i++) {
	    sb.append(text[i]).append("(" + i + "), ");
	}
	System.out.println(sb);
    }

    private List<Integer> matchIndexes(String word, int wordIndex, String[] searchWords) {
	List<Integer> swIndexes = new ArrayList<>();
	for (int i = 0; i < searchWords.length; i++) {
	    if (searchWords[i].equals(word)) {
		swIndexes.add(i);
	    }
	}
	return swIndexes;
    }
}
