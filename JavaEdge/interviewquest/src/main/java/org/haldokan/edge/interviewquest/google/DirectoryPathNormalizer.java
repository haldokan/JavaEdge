package org.haldokan.edge.interviewquest.google;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
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

        Deque<String> deck = new ArrayDeque<>();
        String[] parts = path.split(SPRTR);
        for (String part : parts) {
            if (part.equals("..")) {
                deck.removeLast();
            } else {
                deck.addLast(part);
            }
        }
        return String.join("/", deck);
    }

}
