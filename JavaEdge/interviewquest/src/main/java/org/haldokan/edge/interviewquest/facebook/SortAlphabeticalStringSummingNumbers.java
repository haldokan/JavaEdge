package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I support a general use case where we can have signed numbers in
 * the string. In fact the solution works as an addition calculator as well (look at the tests). We also sort the string
 * in O(n) rather than O(nlog(n)) by using the ascii representation of the chars as indexes to an int array that also counts
 * the sequential occurrences of the same char
 * The Question: 4_STAR
 * <p>
 * write a program to print the given string sorted alphabetically but the numbers should be summed and added at the end
 * <p>
 * Examples:
 * CAE2W3A is input and output should be ACDEW5
 * Input -12cza-92ph-10 => Output achpz-114
 * Input -12cza-92ph+10 => Output achpz-94
 * <p>
 * Created by haytham.aldokanji on 7/7/16.
 */
public class SortAlphabeticalStringSummingNumbers {
    public static void main(String[] args) {
        SortAlphabeticalStringSummingNumbers driver = new SortAlphabeticalStringSummingNumbers();

        driver.test();
    }

    public String sort(String str) {
        if (str == null || str.isEmpty() || str.length() == 1) {
            return str;
        }

        int[] ascii = new int[128];
        int grandSum = 0;
        int partialSum = 0;
        int multiplier = 1;
        // neat single loop with the smarts to parse out numbers and sort chars
        for (int i = str.length() - 1; i >= 0; i--) {
            char chr = str.charAt(i);
            int digit = toDigit(chr);

            if (chr == '-' || chr == '+') {
                partialSum *= (chr == '-' ? -1 : 1);
                grandSum += partialSum;
                partialSum = 0;
                multiplier = 1;
            } else if (digit < 0) { // not digit
                ascii[chr]++;
                if (partialSum > 0) {
                    grandSum += partialSum;
                    partialSum = 0;
                    multiplier = 1;
                }
            } else { // digit
                partialSum += digit * multiplier;
                multiplier *= 10;
            }
        }
        grandSum += partialSum;

        StringBuilder result = new StringBuilder(1024);
        for (int i = 0; i < ascii.length; i++) {
            for (int j = 0; j < ascii[i]; j++) {
                result.append((char) i);
            }
        }
        return result.toString() + grandSum;
    }

    private int toDigit(char chr) {
        return "0123456789".indexOf(chr);
    }

    private void test() {
        assertThat(sort("cza92ph"), is("achpz92"));
        assertThat(sort("cczzaa92pphh"), is("aacchhppzz92"));
        assertThat(sort("12cza92ph10"), is("achpz114"));
        assertThat(sort("12cza92ph-10"), is("achpz94"));
        assertThat(sort("12cza-92ph10"), is("achpz-70"));
        assertThat(sort("-12cza92ph10"), is("achpz90"));
        assertThat(sort("-12cza-92ph-10"), is("achpz-114"));
        assertThat(sort("-12cza-92ph+10"), is("achpz-94"));
        assertThat(sort("zhca"), is("achz0"));
        assertThat(sort("1234"), is("1234"));
        assertThat(sort("-1234"), is("-1234"));
        assertThat(sort("-12-34"), is("-46"));
        assertThat(sort("-12+34"), is("22"));
        assertThat(sort("12+34"), is("46"));
        assertThat(sort("-1c-1"), is("c-2"));
        assertThat(sort("-7343+11-377-7C"), is("C" + (-7343 + 11 - 377 - 7)));
    }
}
