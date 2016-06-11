package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * Convert a string of Roman numerals to an integer in O(n) time
 * <p>
 * Created by haytham.aldokanji on 4/29/16.
 */
public class ConvertRomanNumsToIntegers {
    private static final Map<Character, Integer> ROMAN_UNIT_TO_INT = ImmutableMap.<Character, Integer>builder()
            .put('I', 1)
            .put('V', 5)
            .put('X', 10)
            .put('L', 50)
            .put('C', 100)
            .put('D', 500)
            .put('M', 1000)
            .build();

    public static void main(String[] args) {
        Map<String, Integer> sample = sample();

        sample.keySet().stream().forEach(romanNum -> {
            int intNum = fromRomanNumeralToInt(romanNum);
            System.out.println(romanNum + " = " + intNum);

            assertThat(intNum, is(sample.get(romanNum)));
        });
    }

    // assuming well formatted Roman numerals
    public static int fromRomanNumeralToInt(String romanNumeral) {
        char[] romanChars = romanNumeral.toUpperCase().toCharArray();

        int previousNum = 0;
        int intNumber = 0;

        for (int i = romanChars.length - 1; i >= 0; i--) {
            int currentNum = ROMAN_UNIT_TO_INT.get(romanChars[i]);
            if (currentNum < previousNum) {
                intNumber -= currentNum;
            } else {
                intNumber += currentNum;
            }
            previousNum = currentNum;
        }
        return intNumber;
    }

    private static Map<String, Integer> sample() {
        String sample = "861=DCCCLXI," +
                "862=DCCCLXII," +
                "863=DCCCLXIII," +
                "864=DCCCLXIV," +
                "865=DCCCLXV," +
                "866=DCCCLXVI," +
                "867=DCCCLXVII," +
                "868=DCCCLXVIII," +
                "869=DCCCLXIX," +
                "870=DCCCLXX," +
                "871=DCCCLXXI," +
                "872=DCCCLXXII," +
                "873=DCCCLXXIII," +
                "874=DCCCLXXIV," +
                "875=DCCCLXXV," +
                "876=DCCCLXXVI," +
                "877=DCCCLXXVII," +
                "878=DCCCLXXVIII," +
                "879=DCCCLXXIX," +
                "880=DCCCLXXX," +
                "881=DCCCLXXXI," +
                "882=DCCCLXXXII," +
                "883=DCCCLXXXIII," +
                "884=DCCCLXXXIV," +
                "885=DCCCLXXXV," +
                "886=DCCCLXXXVI," +
                "887=DCCCLXXXVII," +
                "888=DCCCLXXXVIII," +
                "889=DCCCLXXXIX," +
                "890=DCCCXC," +
                "891=DCCCXCI," +
                "892=DCCCXCII," +
                "893=DCCCXCIII," +
                "894=DCCCXCIV," +
                "895=DCCCXCV," +
                "896=DCCCXCVI," +
                "897=DCCCXCVII," +
                "898=DCCCXCVIII," +
                "899=DCCCXCIX," +
                "900=CM," +
                "901=CMI," +
                "902=CMII," +
                "903=CMIII," +
                "904=CMIV," +
                "905=CMV," +
                "906=CMVI," +
                "907=CMVII," +
                "908=CMVIII," +
                "909=CMIX," +
                "910=CMX," +
                "911=CMXI," +
                "912=CMXII," +
                "913=CMXIII," +
                "914=CMXIV," +
                "915=CMXV," +
                "916=CMXVI," +
                "917=CMXVII," +
                "918=CMXVIII," +
                "919=CMXIX," +
                "920=CMXX," +
                "921=CMXXI," +
                "922=CMXXII," +
                "923=CMXXIII," +
                "924=CMXXIV," +
                "925=CMXXV," +
                "926=CMXXVI," +
                "927=CMXXVII," +
                "928=CMXXVIII," +
                "929=CMXXIX," +
                "930=CMXXX," +
                "931=CMXXXI," +
                "932=CMXXXII," +
                "1=I," +
                "2=II," +
                "3=III," +
                "4=IV=IIII," +
                "5=V," +
                "6=VI," +
                "7=VII," +
                "8=VIII=IIX," +
                "9=IX=VIIII," +
                "10=X," +
                "11=XI," +
                "12=XII," +
                "13=XIII," +
                "14=XIV," +
                "15=XV," +
                "16=XVI," +
                "17=XVII," +
                "18=XVIII," +
                "19=XIX," +
                "20=XX," +
                "21=XXI," +
                "22=XXII," +
                "23=XXIII," +
                "24=XXIV," +
                "25=XXV," +
                "26=XXVI," +
                "27=XXVII," +
                "28=XXVIII," +
                "29=XXIX," +
                "30=XXX," +
                "31=XXXI," +
                "32=XXXII," +
                "33=XXXIII," +
                "34=XXXIV," +
                "35=XXXV," +
                "36=XXXVI," +
                "37=XXXVII," +
                "38=XXXVIII," +
                "39=XXXIX," +
                "40=XL," +
                "41=XLI," +
                "42=XLII," +
                "43=XLIII," +
                "44=XLIV," +
                "45=XLV," +
                "46=XLVI," +
                "47=XLVII," +
                "48=XLVIII," +
                "49=XLIX," +
                "50=L," +
                "51=LI," +
                "52=LII," +
                "53=LIII," +
                "54=LIV," +
                "55=LV," +
                "56=LVI," +
                "57=LVII," +
                "58=LVIII," +
                "59=LIX," +
                "60=LX," +
                "61=LXI," +
                "62=LXII," +
                "63=LXIII," +
                "64=LXIV," +
                "65=LXV," +
                "66=LXVI," +
                "67=LXVII," +
                "68=LXVIII," +
                "69=LXIX," +
                "70=LXX," +
                "71=LXXI," +
                "72=LXXII";
        String[] data = sample.split(",");

        Map<String, Integer> mapping = new HashMap<>();
        Arrays.stream(data).forEach(pair -> {
            String[] parts = pair.split("=");
            mapping.put(parts[1], Integer.valueOf(parts[0]));
        });

        return mapping;
    }
}


