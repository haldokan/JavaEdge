package org.haldokan.edge.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class GuavaStringOps {
	
	public static void main(String[] args) {
		
		// Joiner
		Joiner joiner = Joiner.on(';');
		String s = joiner.skipNulls().join(Lists.newArrayList(1, null, 3));
		System.out.println(s);
		
		s = joiner.useForNull("na").join(Lists.newArrayList(1, null, 3));
		System.out.println(s);
		
		// Splitter
		Splitter splitter = Splitter.on(',');
		Iterable<String> itr = splitter.trimResults().omitEmptyStrings().split("a,  b, ,,   c,,");
		System.out.println(itr);
		
		itr = splitter.trimResults().split("a,  b, ,,   c,,");
		System.out.println(itr);
		for (String str : itr) {
			System.out.println("'" + str + "'");
		}
		
		// CharMatcher
		s = CharMatcher.JAVA_DIGIT.or(CharMatcher.JAVA_LOWER_CASE).retainFrom("I Have Earned 70 DOLLARS today!");
		System.out.println(s);
		
		s = CharMatcher.WHITESPACE.collapseFrom("T O  B E  O R  N O T  T O  B E", '-');
		System.out.println(s);
		
		s = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.WHITESPACE).retainFrom("We have made this deal: sell the car, buy a motorcycle. End of Story!");
		System.out.println(s);
	}
}
