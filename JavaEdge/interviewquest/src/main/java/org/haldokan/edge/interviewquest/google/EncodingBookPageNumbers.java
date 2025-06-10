package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
 * A book contains pages numbered from 1 - N. Imagine now that you concatenate all page numbers in the book such
 * that you obtain a sequence of numbers which can be represented as a string. You can compute number of occurrences 'k'
 * of certain digit 'd' in this string.
 * <p>
 * For example, let N=12, d=1, hence
 * <p>
 * s = '123456789101112' => k=5
 * <p>
 * since digit '1' occurs five times in that string.
 * <p>
 * Problem: Write a method that, given a digit 'd' and number of its occurrences 'k', returns a number of pages N. More
 * precisely, return a lower and upper bound of this number N.
 * <p>
 * Example:
 * input: d=4, k=1;
 * output {1, 13} - the book has 4-13 pages
 * <p>
 * input d=4 k=0;
 * output {1, 3} - the book has 1-3 pages
 * <p>
 * Created by haytham.aldokanji on 5/30/16.
 */
public class EncodingBookPageNumbers {

    public static void main(String[] args) {
        EncodingBookPageNumbers driver = new EncodingBookPageNumbers();

        driver.test();
    }

    public int[] pageRange(int digit, int occurrences) {
        int[] pages = new int[2];

        if (occurrences == 0) {
            if (digit == 1) {
                pages[0] = pages[1] = 0;
            } else {
                pages[0] = 1;
                pages[1] = digit - 1;
            }
            return pages;
        }

        pages[0] = digit;
        int currentCount = 1;
        int currentNumber = digit;

        while (currentCount < occurrences + 1) {
            currentNumber++;

            char[] currentNumDigits = String.valueOf(currentNumber).toCharArray();
            for (char digitChar : currentNumDigits) {
                if (toDigit(digitChar) == digit) {
                    currentCount++;
                }
            }
            if (currentCount >= occurrences + 1) {
                currentNumber--;
            }
        }
        pages[1] = currentNumber;
        return pages;
    }

    private int toDigit(char digitChar) {
        return digitChar - '0';
    }

    //123456789 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
    private void test() {
        int[] pages = pageRange(4, 1);
        assertThat(pages, is(new int[]{4, 13}));

        pages = pageRange(4, 0);
        assertThat(pages, is(new int[]{1, 3}));

        pages = pageRange(1, 5);
        assertThat(pages, is(new int[]{1, 12}));

        pages = pageRange(1, 13);
        assertThat(pages, is(new int[]{1, 30}));

        pages = pageRange(2, 12);
        assertThat(pages, is(new int[]{2, 28}));

        pages = pageRange(2, 13);
        assertThat(pages, is(new int[]{2, 31}));

        pages = pageRange(9, 1000);
        assertThat(pages, is(new int[]{9, 3508}));
    }
}
