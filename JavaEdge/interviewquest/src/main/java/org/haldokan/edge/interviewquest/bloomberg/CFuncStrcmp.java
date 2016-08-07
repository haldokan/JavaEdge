package org.haldokan.edge.interviewquest.bloomberg;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question
 * <p>
 * The Question: 3_STAR
 * <p>
 * Implement strcmp function of stdlib.h library without using any standard library.
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class CFuncStrcmp {

    public static void main(String[] args) {
        CFuncStrcmp driver = new CFuncStrcmp();
        driver.test();
    }

    public int strcmp(String str1, String str2) {
        int minLen = Math.min(str1.length(), str2.length());

        for (int i = 0; i < minLen; i++) {
            char chr1 = str1.charAt(i);
            char chr2 = str2.charAt(i);

            if (chr1 > chr2) {
                return 1;
            }
            if (chr2 > chr1) {
                return -1;
            }
        }

        int lenDiff = str1.length() - str2.length();
        return lenDiff == 0 ? 0 : lenDiff / Math.abs(lenDiff);
    }

    private void test() {
        assertThat(strcmp("foobar", "foobar"), is(0));
        assertThat(strcmp("foobaz", "foobar"), is(1));
        assertThat(strcmp("foobar", "foobaz"), is(-1));
        assertThat(strcmp("foobar", "foo"), is(1));
        assertThat(strcmp("foo", "foobar"), is(-1));
        assertThat(strcmp("g", "foobar"), is(1));
        assertThat(strcmp("f", "f"), is(0));
    }
}
