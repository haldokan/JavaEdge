package org.haldokan.edge.interviewquest.bloomberg;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Bloomberg interview question
 * <p>
 * The Question: 3_STAR + 1/2
 * <p>
 * Royal titles consist of name followed by space and a Roman numeral. Example: Richard IV. The Roman numeral in the title
 * can go to L (50). You are given the roman numerals from 1 to 10:
 * I II III IV V VI VII VIII IX X. And you are given the 10 multiples up to 50: XX XXX IL L. Numbers between 10 and 50 that
 * are not given can be formed from 10 multiples and a numeral b/w 1 and 9. Example: 48 is XLVIII which is XL (40) plus
 * VIII (8).
 * <p>
 * You are given an array of Roman titles sort it as follows: sort it on the name unless the names are equal, in which
 * case you have to sort it on the ordinal of the numerals.
 * Examples:
 * Henry II, Edward VIII => Eward VII, Henry II
 * Richard V, Richard II, Richard X => Richard II, Richard V, Richard X
 * <p>
 * Created by haytham.aldokanji on 8/9/16.
 */
public class SortingRoyaltyTitles {
    private static ImmutableMap<String, Integer> ROMAN_NUMS_1_TO_9 = new ImmutableMap.Builder<String, Integer>()
            .put("I", 1)
            .put("II", 2)
            .put("III", 3)
            .put("IV", 4)
            .put("V", 5)
            .put("VI", 6)
            .put("VII", 7)
            .put("VIII", 8)
            .put("IX", 9)
            .build();

    private static ImmutableMap<String, Integer> ROMAN_NUMS_10_MULTIPLES = new ImmutableMap.Builder<String, Integer>()
            .put("X", 10)
            .put("XX", 20)
            .put("XXX", 30)
            .put("XL", 40)
            .put("L", 50)
            .build();

    public static void main(String[] args) {
        SortingRoyaltyTitles driver = new SortingRoyaltyTitles();

        driver.testFromRomanToInt();
        driver.testSortTitles();
    }

    public void sortRomanTitles(String[] titles) {
        Arrays.sort(titles, this::compare);

    }

    private int compare(String title1, String title2) {
        String[] title1Parts = title1.split(" ");
        String name1 = title1Parts[0];
        String order1 = title1Parts[1];

        String[] title2Parts = title2.split(" ");
        String name2 = title2Parts[0];
        String order2 = title2Parts[1];

        int sortOrder = name1.compareTo(name2);
        if (sortOrder != 0) {
            return sortOrder;
        }

        Optional<Integer> order1AsInt = fromRomanToInteger(order1);
        Optional<Integer> order2AsInt = fromRomanToInteger(order2);

        if (order1AsInt.isPresent() && order2AsInt.isPresent()) {
            return order1AsInt.get() - order2AsInt.get();
        }
        throw new IllegalArgumentException("Invalid Roman titles: " + title1 + "\n" + title2);
    }

    private Optional<Integer> fromRomanToInteger(String romanNum) {
        Integer intNum = ROMAN_NUMS_1_TO_9.get(romanNum);
        if (intNum == null) {
            intNum = ROMAN_NUMS_10_MULTIPLES.get(romanNum);
        }
        if (intNum != null) {
            return Optional.of(intNum);
        }

        Set<String> multiplesOf10 = ROMAN_NUMS_10_MULTIPLES.keySet();
        for (String multiple : multiplesOf10) {
            if (romanNum.startsWith(multiple)) {
                String lessThan10RomanNum = romanNum.substring(multiple.length(), romanNum.length());
                Integer lessThan10IntNum = ROMAN_NUMS_1_TO_9.get(lessThan10RomanNum);

                if (lessThan10IntNum != null) {
                    return Optional.of(ROMAN_NUMS_10_MULTIPLES.get(multiple) + lessThan10IntNum);
                }
            }
        }
        return Optional.empty();
    }

    private void testFromRomanToInt() {
        String[] romanNumerals = new String[]{
                "1=I",
                "2=II",
                "3=III",
                "4=IV",
                "5=V",
                "6=VI",
                "7=VII",
                "8=VIII",
                "9=IX",
                "10=X",
                "11=XI",
                "12=XII",
                "13=XIII",
                "14=XIV",
                "15=XV",
                "16=XVI",
                "17=XVII",
                "18=XVIII",
                "19=XIX",
                "20=XX",
                "21=XXI",
                "22=XXII",
                "23=XXIII",
                "24=XXIV",
                "25=XXV",
                "26=XXVI",
                "27=XXVII",
                "28=XXVIII",
                "29=XXIX",
                "30=XXX",
                "31=XXXI",
                "32=XXXII",
                "33=XXXIII",
                "34=XXXIV",
                "35=XXXV",
                "36=XXXVI",
                "37=XXXVII",
                "38=XXXVIII",
                "39=XXXIX",
                "40=XL",
                "41=XLI",
                "42=XLII",
                "43=XLIII",
                "44=XLIV",
                "45=XLV",
                "46=XLVI",
                "47=XLVII",
                "48=XLVIII",
                "49=XLIX",
                "50=L"
        };

        for (String numeralPair : romanNumerals) {
            String[] parts = numeralPair.split("=");

            int intValue = Integer.valueOf(parts[0]);
            String romanValue = parts[1];

            assertThat(fromRomanToInteger(romanValue).get(), is(intValue));
        }
    }

    private void testSortTitles() {
        String[] titles = new String[]{"Henry XLIX", "Henry XL", "Henry V", "Henry III", "Edward I", "Richard II"};
        sortRomanTitles(titles);
        assertThat(titles, is(new String[]{"Edward I", "Henry III", "Henry V", "Henry XL", "Henry XLIX", "Richard II"}));
    }
}
