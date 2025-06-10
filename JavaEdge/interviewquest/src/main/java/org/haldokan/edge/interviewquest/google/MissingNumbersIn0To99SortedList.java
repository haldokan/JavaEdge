package org.haldokan.edge.interviewquest.google;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 *
 * You are given a sorted list of distinct integers from 0 to 99, for instance [0, 1, 2, 50, 52, 75]. Your task is to
 * produce a string that describes numbers missing from the list; in this case "3-49,51,53-74,76-99".
 * <p>
 * Examples:
 * <p>
 * [] “0-99”
 * [0] “1-99”
 * [3, 5] “0-2,4,6-99”
 */
public class MissingNumbersIn0To99SortedList {

    public static void main(String[] args) throws Exception {
        MissingNumbersIn0To99SortedList driver = new MissingNumbersIn0To99SortedList();
        driver.test();
    }

    public String missingNumbers(int[] arr, int min, int max) {
        if (arr == null) {
            throw new IllegalArgumentException("Null input");
        }
        if (arr.length == 0) {
            return min + "-" + max;
        }

        StringBuilder result = new StringBuilder();
        if (arr[0] != min) {
            Optional<String> missing = makeMissing(min - 1, arr[0]);
            if (missing.isPresent()) {
                result.append(missing.get());
            } else {
                result.append(min);
            }
            result.append(",");
        }
        for (int i = 0; i < arr.length; i++) {
            if (i + 1 < arr.length) {
                Optional<String> missing = makeMissing(arr[i], arr[i + 1]);
                if (missing.isPresent()) {
                    result.append(missing.get()).append(",");
                }
            }
        }

        int lastNum = arr[arr.length - 1];
        if (lastNum != max) {
            Optional<String> missing = makeMissing(lastNum, max + 1);
            if (missing.isPresent()) {
                result.append(missing.get());
            } else {
                result.append(max);
            }
            result.append(",");
        }

        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    private Optional<String> makeMissing(int num1, int num2) {
        int diff = num2 - num1 - 1;

        String missing = null;
        if (diff == 1) {
            missing = String.valueOf(num2 - diff);
        } else if (diff > 1) {
            missing = (num2 - diff) + "-" + (num2 - 1);
        }
        return Optional.ofNullable(missing);
    }

    private void test() throws Exception {
        String missing = missingNumbers(new int[]{5}, 0, 99);
        assertThat(missing, is("0-4,6-99"));

        missing = missingNumbers(new int[]{0}, 0, 99);
        assertThat(missing, is("1-99"));

        missing = missingNumbers(new int[]{99}, 0, 99);
        assertThat(missing, is("0-98"));

        missing = missingNumbers(new int[]{3, 5}, 0, 99);
        assertThat(missing, is("0-2,4,6-99"));

        missing = missingNumbers(new int[]{3, 5, 10, 11, 13, 17}, 0, 99);
        assertThat(missing, is("0-2,4,6-9,12,14-16,18-99"));
    }

}
