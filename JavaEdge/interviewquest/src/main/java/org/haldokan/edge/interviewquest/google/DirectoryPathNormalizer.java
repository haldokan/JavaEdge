package org.haldokan.edge.interviewquest.google;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;

/**
 * My solution to a Google interview question
 * <p>
 * Given a string representing relative path write a function which normalizes this path (i.e. replaces "..").
 * <p>
 * Example: input: \a\b\..\foo.txt => output: \a\foo.txt
 *
 * @author haldokan
 */
public class DirectoryPathNormalizer {
    private static String SPRTR = File.separator;

    public static void main(String[] args) {
        DirectoryPathNormalizer driver = new DirectoryPathNormalizer();
        System.out.println(driver.normalizePath(SPRTR + "a" + SPRTR + "b" + SPRTR + ".." + SPRTR + "foo.txt"));
        System.out.println(driver.normalizePath(SPRTR + "a" + SPRTR + "b" + SPRTR + ".." + SPRTR + "c" + SPRTR + "d"
                + SPRTR + ".." + SPRTR + ".." + SPRTR + "c" + SPRTR + "foo.txt"));
    }

    public String normalizePath(String path) {
        if (path == null)
            throw new NullPointerException("Null input");

        String[] pathParts = path.split("\\" + SPRTR);
        Deque<String> deck = new LinkedList<>();

        for (String part : pathParts) {
            if (part.equals("..")) {
                if (deck.isEmpty())
                    throw new IllegalArgumentException("Malformatted path");
                deck.pop();
            } else {
                deck.push(part);
            }
        }

        StringBuilder normalizedPath = new StringBuilder();
        while (!deck.isEmpty()) {
            normalizedPath.append(deck.pollLast()).append(SPRTR);
        }
        if (!deck.isEmpty())
            normalizedPath.deleteCharAt(normalizedPath.length() - 1);

        return normalizedPath.toString();
    }

}
