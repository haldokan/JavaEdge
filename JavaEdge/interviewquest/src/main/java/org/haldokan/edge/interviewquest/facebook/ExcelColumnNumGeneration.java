package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * Microsoft Excel numbers cells as 1...26 and after that AA, AB.... AAA, AAB...BBA, BBB,...., ZZA....ZZZ.
 * Given a number, convert it to that format and vice versa.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class ExcelColumnNumGeneration {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    public static void main(String[] args) {
        ExcelColumnNumGeneration driver = new ExcelColumnNumGeneration();
        driver.testNumerialNumbering();
        driver.testFromExcelNumsOfLen2();
        driver.testFromExcelNumsOfLen3();
        driver.testToExcelNumsOfLen2();
        driver.testToExcelNumsOfLen3();
    }

    // Assume valid excel number format so no sanity checks for brevity
    public int fromExcelNumbering(String excelNum) {
        if (isNumerical(excelNum)) {
            return Integer.valueOf(excelNum);
        }
        if (excelNum.length() == 2) {
            return ALPHABET.length() + letterOrder(excelNum.charAt(1));
        } else {
            return ALPHABET.length()
                    + letterOrder(excelNum.charAt(0)) * ALPHABET.length()
                    + letterOrder(excelNum.charAt(2));
        }
    }

    public String toExcelNumbering(int number) {
        if (number <= ALPHABET.length()) {
            return String.valueOf(number);
        }

        int numPart1 = number / ALPHABET.length();
        int numPart2 = number % ALPHABET.length();
        StringBuilder result = new StringBuilder(3);

        if (numPart1 == 1) {
            result.append(letterAt(numPart1)).append(letterAt(numPart2));
        } else if (numPart1 == 2 && numPart2 == 0) {
            result.append(letterAt(numPart1 - 1)).append(letterAt(ALPHABET.length()));
        } else if (numPart2 == 0) {
            char headLetter = letterAt(numPart1 - 2);
            result.append(headLetter).append(headLetter).append(letterAt(ALPHABET.length()));
        } else {
            char headLetter = letterAt(numPart1 - 1);
            result.append(headLetter).append(headLetter).append(letterAt(numPart2));
        }
        return result.toString();
    }

    private boolean isNumerical(String excelNum) {
        char[] parts = excelNum.toCharArray();
        for (char part : parts) {
            if (DIGITS.indexOf(part) < 0) {
                return false;
            }
        }
        return true;
    }

    private int letterOrder(char letter) {
        return ALPHABET.indexOf(letter) + 1;
    }

    private char letterAt(int oneBasedIndex) {
        return ALPHABET.charAt(oneBasedIndex - 1);
    }

    private void testNumerialNumbering() {
        assertThat(isNumerical("26"), is(true));
        assertThat(isNumerical("AAB"), is(false));
    }

    private void testFromExcelNumsOfLen2() {
        assertThat(fromExcelNumbering("1"), is(1));
        assertThat(fromExcelNumbering("26"), is(26));
        assertThat(fromExcelNumbering("AA"), is(27));
        assertThat(fromExcelNumbering("AH"), is(34));
        assertThat(fromExcelNumbering("AZ"), is(52));
    }

    private void testFromExcelNumsOfLen3() {
        assertThat(fromExcelNumbering("AAA"), is(53));
        assertThat(fromExcelNumbering("AAZ"), is(78));
        assertThat(fromExcelNumbering("BBA"), is(79));
        assertThat(fromExcelNumbering("BBZ"), is(104));
    }

    private void testToExcelNumsOfLen2() {
        assertThat(toExcelNumbering(1), is("1"));
        assertThat(toExcelNumbering(26), is("26"));
        assertThat(toExcelNumbering(27), is("AA"));
        assertThat(toExcelNumbering(34), is("AH"));
        assertThat(toExcelNumbering(52), is("AZ"));
    }

    private void testToExcelNumsOfLen3() {
        assertThat(toExcelNumbering(53), is("AAA"));
        assertThat(toExcelNumbering(78), is("AAZ"));
        assertThat(toExcelNumbering(79), is("BBA"));
        assertThat(toExcelNumbering(104), is("BBZ"));
    }
}
