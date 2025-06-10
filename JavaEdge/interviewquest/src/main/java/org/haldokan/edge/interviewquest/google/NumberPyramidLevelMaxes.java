package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * My solution to a Google interview question - ignored the size limitation and assumed that only positive digits are used.
 * I ignored the limitations because they are not clear enough. For example the max string length is 10^10 which is greater
 * than Integer.MAX_VALUE. This means we cannot use String.length or convert the string into an array. But how about
 * String.split("#") which will produce an array of length < Integer.MAX_VALUE. There is no way to tell if split will work
 * on such a giant string. Also where does the string live? All in memory? How about serializing it to disk then streaming
 * it back to memory? Why was it read whole into memory in the first place? All these questions indicate that problem is
 * not well defined.
 *
 * The Question: 3_STAR
 *
 * You have a row of numbers like below(a triangle).
 * By starting at the top of the triangle find the maximum number in each line and sum them up example below
 * 5
 * 9 6
 * 4 6 8
 * 8 7 1 5
 * <p>
 * Answer I.e. 5+9+8+7 = 29
 * write a code to find the maximum total from top to bottom. Assume triangle can have at most 100000 rows.
 * <p>
 * Input Output specifications
 * Input Specification
 * A string of n numbers (where 0<=n<=10^10)
 * eg.5#9#6#4#6#8#0#7#1#5
 * <p>
 * Output Specification
 * A sum of the max numbers in each line (as string ) or Output invalid in case of invalid input/triangle
 * <p>
 * Examples
 * eg1.
 * Input :5#9#6#4#6#8#0#7#1#5
 * Output:29
 * <p>
 * eg 2 .
 * Input :5#9#6#4#6#8#0#7#1
 * Output:invalid
 * <p>
 * eg 2 .
 * Input :5#9##4#6#8#0#7#1
 * Output:invalid
 * Created by haytham.aldokanji on 5/17/16.
 */
public class NumberPyramidLevelMaxes {
    private static final String DIGITS = "0123456789";

    public static void main(String[] args) {
        NumberPyramidLevelMaxes driver = new NumberPyramidLevelMaxes();

        driver.testCanFormTriangle();
        driver.testPyramidRowMaxesSum();


    }

    public int sum(String string) {
        if (!canFormTriangle(string)) {
            throw new IllegalArgumentException("Numbers in string cannot form triangle");
        }

        int index = 0;
        int countOfNumsAtStep = 1;
        int maxSum = 0;

        //ignoring the fact that 10^10 > Integer.MAX_VALUE for this version of the solution
        while (index < string.length()) {
            Integer maxRowSum = null;
            for (int i = index; i < index + countOfNumsAtStep * 2; i += 2) {
                char chr = string.charAt(i);
                if (!Character.isDigit(chr)) {
                    throw new IllegalArgumentException("Invalid input format");
                }

                int digit = DIGITS.indexOf(chr);
                if (maxRowSum == null || maxRowSum < digit) {
                    maxRowSum = digit;
                }
            }
            maxSum += maxRowSum;
            index += countOfNumsAtStep * 2;
            countOfNumsAtStep++;
        }
        return maxSum;
    }

    private boolean canFormTriangle(String string) {
        int stringLen = (string.length() + 1) / 2;

        int sum = 0;
        int step = 0;
        while (sum < stringLen) {
            step += 1;
            sum += step;
        }
        return sum == stringLen;
    }

    private void testCanFormTriangle() {
        assertThat(canFormTriangle("0#7#6"), is(true));
        assertThat(canFormTriangle("1#0#3#8#2#7#6#2#3#1"), is(true));
        assertThat(canFormTriangle(("5#9#6#4#6#8#0#7#1#5")), is(true));
        assertThat(canFormTriangle(("5#9#6#4#6#8#0#7#1")), is(false));
        assertThat(canFormTriangle("3#4#5#1"), is(false));
    }

    private void testPyramidRowMaxesSum() {
        assertThat(sum("7"), is(7));
        assertThat(sum("7#2#6"), is(13));
        assertThat(sum("1#3#1#4#6#2"), is(10));
        assertThat(sum("5#9#6#4#6#8#0#7#1#5"), is(29));
        try {
            assertThat(sum("1#3#1##4#6#2"), is(10));
            fail("should not come here");
        } catch (IllegalArgumentException e) {
            System.out.println("expected testing exception: " + e);
        }
    }
}