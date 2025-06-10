package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - the dance of the arrays!
 * <p>
 * The Question: 3_STAR + 1/2
 * <p>
 * List of string that represent class names in CamelCaseNotation.
 * <p>
 * Write a function that given a List and a pattern returns the matching elements.
 * <p>
 * ['HelloMars', 'HelloWorld', 'HelloWorldMars', 'HiHo']
 * <p>
 * H -> [HelloMars, HelloWorld, HelloWorldMars, HiHo]
 * HW -> [HelloWorld, HelloWorldMars]
 * Ho -> []
 * HeWorM -> [HelloWorldMars]
 * Created by haytham.aldokanji on 9/15/16.
 */
public class FindingPatternMatchesInCamelCaseNotations {
    private static final String ALPHABET_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        FindingPatternMatchesInCamelCaseNotations driver = new FindingPatternMatchesInCamelCaseNotations();
        driver.test1();
        driver.test2();
        driver.test3();
    }

    public List<String> findMatches(String[] input, String pattern) {
        List<String> matches = new ArrayList<>();
        for (String item : input) {
            if (isMatch(item, pattern)) {
                matches.add(item);
            }
        }
        return matches;
    }

    private boolean isMatch(String input, String pattern) {
        int index = 0;
        if (input.charAt(index) != pattern.charAt(index)) {
            return false;
        }

        index++;
        for (int i = 1; i < pattern.length(); i++) {
            char patternChar = pattern.charAt(i);

            if (ALPHABET_CAPS.indexOf(patternChar) < 0) {
                if (patternChar != input.charAt(index)) {
                    return false;
                }
                index++;
            } else {
                boolean found = false;
                while (index < input.length()) {
                    char inputChar = input.charAt(index);
                    if (ALPHABET_CAPS.indexOf(inputChar) >= 0) {
                        if (inputChar != patternChar) {
                            return false;
                        } else {
                            index++;
                            found = true;
                            break;
                        }
                    }
                    index++;
                }
                if (!found) {
                    return false;
                }
            }
        }
        return true;
    }

    private void test1() {
        String input = "HelloWorldMars";
        assertThat(isMatch(input, "H"), is(true));
        assertThat(isMatch(input, "HW"), is(true));
        assertThat(isMatch(input, "HWM"), is(true));
        assertThat(isMatch(input, "BWM"), is(false));
        assertThat(isMatch(input, "HM"), is(false));
        assertThat(isMatch(input, "HMW"), is(false));
        assertThat(isMatch(input, "WM"), is(false));
    }

    private void test2() {
        String input = "HelloWorldMars";
        assertThat(isMatch(input, "Hell"), is(true));
        assertThat(isMatch(input, "HelWor"), is(true));
        assertThat(isMatch(input, "HelWorMar"), is(true));
        assertThat(isMatch(input, "HilWorMar"), is(false));
        assertThat(isMatch(input, "HelWorMir"), is(false));
        assertThat(isMatch(input, "HelWirMar"), is(false));
    }

    private void test3() {
        String[] input = new String[]{"HelloMars", "HelloWorld", "HelloWorldMars", "HiHo"};
        assertThat(findMatches(input, "H").toArray(), is(input));
        assertThat(findMatches(input, "HW").toArray(), is(new String[]{"HelloWorld", "HelloWorldMars"}));
        assertThat(findMatches(input, "HeWorM").toArray(), is(new String[]{"HelloWorldMars"}));
        assertThat(findMatches(input, "Ho").toArray(), is(new String[0]));
    }
}
