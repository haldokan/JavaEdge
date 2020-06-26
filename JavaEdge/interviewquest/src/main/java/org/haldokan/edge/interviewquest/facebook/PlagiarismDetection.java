package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I present 2 solutions: one using the String utilities and another that
 * uses char arrays and implements 'substring' and 'contains' methods
 * The Question: 4_STAR
 * A professor wants to see if two students have cheated when writing a paper. Design a function:
 * hasCheated(String s1,String s2, int N) that evaluates to true if two strings have a common substring of length N.
 * Additional question after implementation. Assume you don't have the possibility of using
 * String.contains() and String.substring(). How would you implement this?
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class PlagiarismDetection {

    private static final String doc1 = "Consequences of Global Warming: The consequences of global warming can be drastic. " +
            "It involves meting icecaps that raise the see level and submerge coastal cities";
    private static final String doc2 = "One View of Global Warming: The consequences of global warming can be drastic. " +
            "It involves diminishing rain falls over entire regions making them inhabitable.";

    public static void main(String[] args) {
        PlagiarismDetection driver = new PlagiarismDetection();
        driver.test1();
        driver.test2();
    }

    // 1st solution: using String utilities
    public boolean isPlagiarized1(String doc1, String doc2, int copyPasteLen) {
        String[] docs = new String[]{doc1, doc2};
        Arrays.sort(docs, Comparator.comparingInt(String::length));

        String firstDoc = docs[0];
        String secondDoc = docs[1];

        for (int i = 0; i < firstDoc.length() - copyPasteLen + 1; i++) {
            String currentSegment = firstDoc.substring(i, i + copyPasteLen);
            if (secondDoc.contains(currentSegment)) {
                System.out.println("'" + currentSegment + "'");
                return true;
            }
        }
        return false;
    }

    // 2nd solution using string utilities but I think better than solution 1 since it assumes matches on words reducing
    // run time from being a function of the number of chars to the number of words
    boolean isPlagiarized2(String s1, String s2, int len) {
        List<Integer> separatorIndexes = new ArrayList<>();
        int index = 0;
        separatorIndexes.add(0);

        while (index != -1) {
            index = s1.indexOf(' ', index);
            if (index != -1) {
                separatorIndexes.add(index);
                index++;
            }
        }
        for (int separatorIndex : separatorIndexes) {
            String subString = s1.substring(separatorIndex, Math.min(separatorIndex + len, s1.length()));
            if (s2.contains(subString)) {
                System.out.println(subString);
                return true;
            }
        }
        return false;
    }

    //3rd solution: w/o using String utilities - I think it is of the order of n x m ^ 2 which leads me to think it
    // is not the solution the interview wanted. But there does not seem another way
    public boolean isPlagiarized4(String doc1, String doc2, int copyPasteLen) {
        String[] docs = new String[]{doc1, doc2};
        Arrays.sort(docs, Comparator.comparingInt(String::length));

        char[] smallerDoc = docs[0].toCharArray();
        char[] largerDoc = docs[1].toCharArray();

        for (int i = 0; i < smallerDoc.length - copyPasteLen + 1; i++) {
            int[] segmentBounds = new int[]{i, i + copyPasteLen};
            if (contains(largerDoc, smallerDoc, segmentBounds)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(char[] largerDoc, char[] smallerDoc, int[] segmentBounds) {
        int segmentLen = segmentBounds[1] - segmentBounds[0];

        for (int i = 0; i < largerDoc.length - segmentLen + 1; i++) {
            int segmentIndex = segmentBounds[0];
            boolean contained = true;
            for (int j = i; j < i + segmentLen; j++) {
                if (largerDoc[j] != smallerDoc[segmentIndex++]) {
                    contained = false;
                    break;
                }
            }
            if (contained) {
                printSegment(largerDoc, i, i + segmentLen);
                return true;
            }
        }
        return false;
    }

    private void printSegment(char[] doc, int startIndex, int endIndex) {
        char[] segment = Arrays.copyOfRange(doc, startIndex, endIndex);
        System.out.print("'");
        for (char c : segment) {
            System.out.print(c);
        }
        System.out.print("'");
        System.out.println("");
    }

    private void test1() {
        int n = 50;
        assertThat(isPlagiarized1(doc1, doc2, n), is(true));

        n = 83;
        assertThat(isPlagiarized1(doc1, doc2, n), is(true));

        n = 84;
        assertThat(isPlagiarized1(doc1, doc2, n), is(false));

        n = 700;
        assertThat(isPlagiarized1(doc1, doc2, n), is(false));
    }

    private void test2() {
        int n = 50;
        assertThat(isPlagiarized4(doc1, doc2, n), is(true));

        n = 83;
        assertThat(isPlagiarized4(doc1, doc2, n), is(true));

        n = 84;
        assertThat(isPlagiarized4(doc1, doc2, n), is(false));

        n = 700;
        assertThat(isPlagiarized4(doc1, doc2, n), is(false));
    }
}
