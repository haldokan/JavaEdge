package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - completely in situ w/o using any of Java Character methods to identify
 * alphabet or letters, etc.
 * The Question: 4_STAR
 * Given a string containing letter, digit, and other characters, write a function to check palindrome for only
 * letter and digit. The implementation need to be in-place, no extra memory is allowed to create another string or array.
 * For example:
 * <p>
 * "ABA" is palindrome
 * "A!#A" is palindrome
 * "A man, a plan, a canal, Panama!" is palindrome
 * <p>
 * Created by haytham.aldokanji on 5/13/16.
 */
public class PalindromeDetectionInSitu {
    // in Java Character provides methods to check whether a char is alphabet of a digit but I am going to ignore that
    // and code it
    private static final int[] CAPS_RANGE = new int[]{'A', 'A' + 25};
    private static final int[] LOWERCASE_RANGE = new int[]{'a', 'a' + 25};
    private static final int[] DIGIT_RANGE = new int[]{'0', '0' + 8};

    public static void main(String[] args) {
        PalindromeDetectionInSitu driver = new PalindromeDetectionInSitu();
        driver.test();
    }

    public boolean isPalindrome(String string) {
        if (string == null) {
            throw new NullPointerException("Invalid input");
        }
        int forward = 0;
        int backward = string.length() - 1;

        while (forward <= backward) {
            char c1 = string.charAt(forward);
            while (notLetterOrDigit(c1)) {
                c1 = string.charAt(++forward);
            }
            char c2 = string.charAt(backward);
            while (notLetterOrDigit(c2)) {
                c2 = string.charAt(--backward);
            }
            if (!equals(c1, c2)) {
                return false;
            }
            forward++;
            backward--;
        }
        return true;
    }

    private boolean notLetterOrDigit(char c) {
        return !isCaps(c) && !isLowerCase(c) && !isDigit(c);
    }

    // account for ignore-case equality - I am assuming that String.toLower breaks the in-situ condition
    private boolean equals(char c1, char c2) {
        if (isCaps(c1) && isLowerCase(c2)) {
            return c1 - CAPS_RANGE[0] == c2 - LOWERCASE_RANGE[0];
        } else if (isCaps(c2) && isLowerCase(c1)) {
            return c2 - CAPS_RANGE[0] == c1 - LOWERCASE_RANGE[0];
        }
        return c1 == c2;
    }

    private boolean isCaps(char c) {
        return c >= CAPS_RANGE[0] && c <= CAPS_RANGE[1];
    }

    private boolean isLowerCase(char c) {
        return c >= LOWERCASE_RANGE[0] && c <= LOWERCASE_RANGE[1];
    }

    private boolean isDigit(char c) {
        return c >= DIGIT_RANGE[0] && c <= DIGIT_RANGE[1];
    }

    private void test() {
        assertThat(isPalindrome("ABA"), is(true));
        assertThat(isPalindrome("A1$@1A"), is(true));
        assertThat(isPalindrome("A1$@1A!%%%"), is(true));
        assertThat(isPalindrome("$A^1$@1A!%%%"), is(true));
        assertThat(isPalindrome("$0A^1$@1A0"), is(true));
        assertThat(isPalindrome("$9A^1$@1A9"), is(true));
        assertThat(isPalindrome("{{A man, a plan, a canal, %%Panama!"), is(true));
    }
}
