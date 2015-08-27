package org.haldokan.edge.guava;

import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.List;

public class GuavaFluentIterable {
    public static void main(String[] args) {
        List<String> l = new ArrayList<>();
        l.add("Hello");
        l.add("World!!!");
        l.add("World!!!");
        l.add("World!!!");
        l.add("World!!!");

        FluentIterable<String> fi = FluentIterable.from(l);
        System.out.println(fi.filter((String input) -> input.length() > 5)
                .transform((String::length)).toSet());
    }
}
