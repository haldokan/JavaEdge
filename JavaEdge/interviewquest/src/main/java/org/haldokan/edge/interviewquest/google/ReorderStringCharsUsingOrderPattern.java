package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Comparator;

/**
 * My solution to a Google interview question.
 * <p>
 * You are given two strings. String T is a string where characters need to be reordered. String O contains characters
 * (none of them repeated) which defines the order/precendence to be used while reordering the string T.
 * Write an algorithm to do the reordering.
 * <p>
 * The question was purposefully underspecified - upon questioning it was revealed that the string O might not
 * necessarily include all characters used in string T - the characters not included in string O are supposed to be
 * placed at the beginning of the resulting string (in no particular order).
 * Created by haytham.aldokanji on 10/6/15.
 */
public class ReorderStringCharsUsingOrderPattern {

    public static void main(String[] args) {
        ReorderStringCharsUsingOrderPattern driver = new ReorderStringCharsUsingOrderPattern();
        System.out.println(driver.reorder("xfacbgdeedyfghz", "hgfedcba"));
    }

    public String reorder(String s, final String order) {
        Character[] chars = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            chars[i] = s.charAt(i);
        }
        Arrays.sort(chars, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return order.indexOf(o1) - order.indexOf(o2);
            }
        });
        String reordered = "";
        for (Character c : chars) {
            reordered += c;
        }
        return reordered;
    }
}
