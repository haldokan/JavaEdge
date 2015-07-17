package org.haldokan.edge.guava;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class GuavaFluentIterable {
    public static void main(String[] args) {
	List<String> l = new ArrayList<>();
	l.add("Hello");
	l.add("World!!!");
	l.add("World!!!");
	l.add("World!!!");
	l.add("World!!!");

	FluentIterable<String> fi = FluentIterable.from(l);
	System.out.println(fi.filter(new Predicate<String>() {
	    @Override
	    public boolean apply(String input) {
		return input.length() > 5;
	    }
	}).transform(new Function<String, Integer>() {
	    @Override
	    public Integer apply(String input) {
		return input.length();
	    }
	}).toSet());
    }
}
