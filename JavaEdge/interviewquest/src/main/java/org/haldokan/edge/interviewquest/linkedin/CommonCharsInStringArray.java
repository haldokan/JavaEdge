package org.haldokan.edge.interviewquest.linkedin;

import java.util.HashSet;
import java.util.Set;

/**
 * Here are my 2 solutions to a Linkedin interview question
 * The Question: 3_STAR
 * Write a program that gives count of common characters presented in an array of strings..(or array of character
 * arrays)
 * <p>
 * For eg.. for the following input strings..
 * <p>
 * aghkafgklt dfghako qwemnaarkf
 * <p>
 * The output should be 3. because the characters a, f and k are present in all 3 strings.
 * <p>
 * Note: The input strings contains only lower case alphabets
 *
 * @author haldokan
 */
public class CommonCharsInStringArray {

    public static void main(String[] args) {
        CommonCharsInStringArray commonChars = new CommonCharsInStringArray();
        String[] input = new String[]{"aghkafgklt", "dfghako", "qwemnaarkf"};

        System.out.println(commonChars.commonCharCount_usingSets(input));
        System.out.println(commonChars.commonCharCount_usingStrings(input));
    }

    public int commonCharCount_usingSets(String[] input) {
        if (input == null || input.length == 0)
            return 0;
        if (input.length == 1)
            return input[0].length();

        Set<Character> rslt = makeCharSet(input[0]);

        for (String s : input) {
            Set<Character> set = makeCharSet(s);
            rslt.retainAll(set);
        }
        System.out.println(rslt);
        return rslt.size();
    }

    private Set<Character> makeCharSet(String s) {
        char[] chararr = new char[s.length()];
        s.getChars(0, s.length(), chararr, 0);

        Set<Character> set = new HashSet<>();
        for (char c : chararr) {
            set.add(c);
        }
        return set;
    }

    public int commonCharCount_usingStrings(String[] input) {
        if (input == null || input.length == 0)
            return 0;
        if (input.length == 1)
            return input[0].length();

        String first = input[0];
        String commonChars = "";

        for (int i = 0; i < first.length(); i++) {
            if (commonChars.indexOf(first.charAt(i)) != -1) {
                continue;
            }
            boolean common = true;
            for (int j = 1; j < input.length; j++) {
                if (input[j].indexOf(first.charAt(i)) == -1) {
                    common = false;
                    break;
                }
            }
            if (common) {
                commonChars += first.charAt(i);
            }
        }
        System.out.println(commonChars);
        return commonChars.length();
    }
}
