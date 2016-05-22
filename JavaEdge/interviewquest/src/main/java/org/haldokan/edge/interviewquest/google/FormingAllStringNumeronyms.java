package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * My solution to a Google interview question
 * <p>
 * i18n (where 18 stands for the number of letters between the first i and the last n in the word
 * “internationalization,”) Wiki it.
 * <p>
 * Generate all such possible i18n strings for any given string. for eg. "careercup"=>"c7p","ca6p","c6up","car5p","ca5up",
 * "care4p","car4up","caree3p","care3up"..till the count is 0 which means its the complete string again.
 * <p>
 * Created by haytham.aldokanji on 5/21/16.
 */
public class FormingAllStringNumeronyms {

    public static void main(String[] args) {
        FormingAllStringNumeronyms driver = new FormingAllStringNumeronyms();

        driver.testGetNumeronym();
        driver.testAllNumeronyms();
    }

    public List<String> allNumeronyms(String string) {
        List<String> numeronyms = new ArrayList<>();

        if (string.length() < 3) {
            numeronyms.add(string);
            return numeronyms;
        }
        boolean[] mask = new boolean[string.length()];
        numeronyms.add(getNumeronym(string, mask, 0));

        int internalLen = string.length() - 1;
        for (int numMasked = 1; numMasked < internalLen; numMasked++) {
            setupMask(mask, numMasked);
            numeronyms.add(getNumeronym(string, mask, numMasked));

            int maskNewStart = 2;
            int maskNewEnd = maskNewStart + numMasked - 1;

            while (maskNewEnd < internalLen) {
                mask[maskNewStart - 1] = false;
                mask[maskNewEnd] = true;
                numeronyms.add(getNumeronym(string, mask, numMasked));

                maskNewStart++;
                maskNewEnd++;
            }
        }
        return numeronyms;
    }

    private void setupMask(boolean[] mask, int numMasked) {
        Arrays.fill(mask, false);
        if (numMasked + 2 > mask.length) {
            throw new IllegalArgumentException("Too many masked letters: " + numMasked);
        }
        for (int i = 1; i <= numMasked; i++) {
            mask[i] = true;
        }
    }

    private String getNumeronym(String string, boolean[] mask, int numMasked) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < mask.length) {
            if (!mask[i]) {
                sb.append(string.charAt(i));
                i++;
            } else {
                sb.append(numMasked);
                i += numMasked;
            }
        }
        return sb.toString();
    }

    private void testGetNumeronym() {
        String string = "Careercup";

        boolean[] mask = new boolean[string.length()];
        assertThat(getNumeronym(string, mask, 0), is(string));

        setupMask(mask, 1);
        assertThat(getNumeronym(string, mask, 1), is("C1reercup"));

        setupMask(mask, 2);
        assertThat(getNumeronym(string, mask, 2), is("C2eercup"));

        setupMask(mask, 3);
        assertThat(getNumeronym(string, mask, 3), is("C3ercup"));

        setupMask(mask, 4);
        assertThat(getNumeronym(string, mask, 4), is("C4rcup"));

        setupMask(mask, 5);
        assertThat(getNumeronym(string, mask, 5), is("C5cup"));

        setupMask(mask, 6);
        assertThat(getNumeronym(string, mask, 6), is("C6up"));

        setupMask(mask, 7);
        assertThat(getNumeronym(string, mask, 7), is("C7p"));

        try {
            setupMask(mask, 8);
            fail("should have failed for long mask");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected testing exception: " + e);
        }
    }

    private void testAllNumeronyms() {
        String string = "Careercup";
        List<String> allNumeronyms = allNumeronyms(string);
        assertThat(allNumeronyms, contains("Careercup",
                "C1reercup", "Ca1eercup", "Car1ercup", "Care1rcup", "Caree1cup", "Career1up", "Careerc1p",
                "C2eercup", "Ca2ercup", "Car2rcup", "Care2cup", "Caree2up", "Career2p",
                "C3ercup", "Ca3rcup", "Car3cup", "Care3up", "Caree3p",
                "C4rcup", "Ca4cup", "Car4up", "Care4p",
                "C5cup", "Ca5up", "Car5p",
                "C6up", "Ca6p",
                "C7p"));
    }
}
