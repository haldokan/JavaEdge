package org.haldokan.edge.lunchbox;

import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Linkedin interview question. Complexity O(n^2). I doubt it can be solved in better time
 * 
 * This class will be given a list of words (such as might be tokenized from a paragraph of text), and will provide a
 * method that takes two words and returns the shortest distance (in words) between those two words in the provided
 * text.
 * 
 * Example: WordDistanceFinder finder = new WordDistanceFinder(Arrays.asList("the", "quick", "brown", "fox", "quick"));
 * 
 * assert(finder.distance("fox","the") == 3);
 * 
 * assert(finder.distance("quick", "fox") == 1);
 */
public class WordsDistance {
    private final String[] text;
    
    public static void main(String[] args) {
	String[] text1 = new String[]{"smart", "the", "quick", "brown", "fox", "quick", "clever", "and", "smart", "clever"};
	WordsDistance wd = new WordsDistance(text1);
	
	System.out.println(wd.distance("fox", "the"));
	System.out.println(wd.distance("quick", "fox"));
	System.out.println(wd.distance("clever", "smart"));
	
	// if same word the dist is 0
	System.out.println(wd.distance("smart", "smart"));
	// if one word or both words are not in the text return -1
	System.out.println(wd.distance("fox", "foo"));
	System.out.println(wd.distance("foo", "bar"));
    }
    
    public WordsDistance(String[] text) {
	this.text = text;
    }
    
    private int distance(String w1, String w2) {
	Integer minDist = null;
	List<Integer> w1Indexes = new ArrayList<>();
	List<Integer> w2Indexes = new ArrayList<>();
	
	for (int i = 0; i < text.length; i++) {
	    if (text[i].equals(w1))
		w1Indexes.add(i);
	    if (text[i].equals(w2))
		w2Indexes.add(i);
	}
	for (Integer i : w1Indexes) {
	    for (Integer j : w2Indexes) {
		int dist = Math.abs(i - j);
		if (minDist == null || dist < minDist)
		    minDist = dist; 
	    }
	}
	return minDist == null ? -1 : minDist;
    }
}
