package org.haldokan.edge.guava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

/**
 * @author haldokan
 *
 */
public class GuavaOptional {
    public static void main(String[] args) {
	String str = "foobar";
	Optional<String> ostr = Optional.of(str);

	System.out.println(ostr.isPresent());

	str = null;
	Optional<String> ostr2 = Optional.fromNullable(str);
	System.out.println(ostr2.isPresent());
	System.out.println(ostr2.orNull());
	System.out.println(ostr2.or("NA"));
	System.out.println(ostr2.or(new StringSupplier()));

	Optional<String> ostr3 = Optional.of("to be or not to be that is the question bro!");
	System.out.println(ostr3);
	Optional<String> fstr = (ostr3.transform(new StringFormatFunc()));
	System.out.println(fstr.get());

	Set<Optional<String>> oset = new HashSet<>();
	oset.add(ostr);
	oset.add(ostr2);
	oset.add(ostr3);
	Iterable<String> presentOset = Optional.presentInstances(oset);
	for (String s : presentOset) {
	    System.out.println(s);
	}

	List<String> list = new ArrayList<>();
	list.add("haldokan");
	list.add("go");
	list.add("for");
	list.add("it");
	Optional<List<String>> olist = Optional.of(list);
	System.out.println(olist);
	System.out.println(olist.transform(new ListElementsLengthFunc()));

	System.out.println(ostr2.get());
    }

    private static class StringSupplier implements Supplier<String> {
	public String get() {
	    return "str" + new Random().nextInt();
	}
    }

    // func args can be of different types
    private static class StringFormatFunc implements Function<String, String> {

	public String apply(String str) {
	    String fstr = Preconditions.checkNotNull(str, "str cannot be null");
	    fstr = fstr.toUpperCase();

	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < fstr.length(); i++) {
		sb.append(fstr.charAt(i)).append(" ");
	    }
	    return sb.toString();
	}
    }

    private static class ListElementsLengthFunc implements Function<Collection<String>, Map<String, Integer>> {

	@Override
	public Map<String, Integer> apply(Collection<String> list) {
	    Preconditions.checkNotNull(list);
	    Map<String, Integer> m = new HashMap<>();
	    for (String s : list)
		m.put(s, s.length());
	    return m;
	}
    }
}
