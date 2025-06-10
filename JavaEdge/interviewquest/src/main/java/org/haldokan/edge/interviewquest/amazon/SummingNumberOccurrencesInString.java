package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to an Amazon interview question - this is a simpler version of this Facebook question that I resolved here:
 * org.haldokan.edge.interviewquest.facebook.SortAlphabeticalStringSummingNumbers
 * The Question: 3_STAR
 * <p>
 * How to calculate sum of all numbers in a string. Example 11aa22bb33dd44 =110
 * Note: Should not use Regex and replace
 * <p>
 * Created by haytham.aldokanji on 7/22/16.
 */
public class SummingNumberOccurrencesInString {

    public static void main(String[] args) {
        SummingNumberOccurrencesInString driver = new SummingNumberOccurrencesInString();
        assertThat(driver.sum("11aa22bb33dd44"), is(110));
        assertThat(driver.sum("11aa-22bb33dd44"), is(66));
    }

    public int sum(String str) {
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
        return grandSum;
    }

    private int toDigit(char chr) {
        return "0123456789".indexOf(chr);
    }
}
