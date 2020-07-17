package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - for handling strings that include numbers I used a metadata section of the
 * compressed string that tracks the indexes of the the numbers that don't repeat
 *
 * The Question: 4-STAR
 *
 * Implement a method to perform basic string compression using the counts of repeated characters.
 * The twist: the string can also contain digits. Think of encoding and decode protocol. How the compression can be
 * reversed properly?
 *
 * For example, ab2ccccd -> ab24cd
 *
 * 07/17/20
 */
public class StringCompression {
    static String digits = "0123456789";

    public static void main(String[] args) {
        StringCompression driver = new StringCompression();

        driver.test1();
        driver.test2();
    }
    
    void test1() {
        System.out.println(compress("ab2ccccd"));
        System.out.println(compress("ab2ccccdd"));
        System.out.println(compress("aab2ccccddd"));
        System.out.println(compress("aabb"));
        System.out.println(compress("abbb"));
        System.out.println(compress("aa"));
        System.out.println(compress("a"));
    }

    void test2() {
        String input = "ab7222ccccd8339";
        String compressed = compress(input);
        System.out.println(compressed);

        String decompressed = decompress(compressed);
        System.out.println(decompressed);

        assertThat(decompressed, equalTo(input));
    }


    void doCompress(StringBuilder compressed, char chr, int count, StringBuilder metadata) {
        if (count > 1) {
            compressed.append(count);
        }
        compressed.append(chr);

        if (count == 1 && digits.indexOf(chr) >= 0) {
            // actual numbers in the string and not repeat counts
            metadata.append(compressed.length() - 1).append(',');
        }
    }

    String compress(String input) {
        StringBuilder compressed = new StringBuilder();
        StringBuilder metadata = new StringBuilder();

        char[] chars = input.toCharArray();
        char chr = chars[0];
        int index = 1;
        int count = 1;

        while (index < chars.length) {
            if (chars[index] == chr) {
                count++;
            } else {
                doCompress(compressed, chr, count, metadata);
                chr = chars[index];
                count = 1;
            }
            index++;
        }
        doCompress(compressed, chr, count, metadata);
        return String.format("%s-%s", metadata.toString(), compressed.toString());
    }

    String decompress(String input) {
        int separatorIndex = input.indexOf('-');
        String data = input.substring(separatorIndex + 1);
        String metadata = input.substring(0, separatorIndex); // can map it to a set but want to keep low-level api for such interview questions

        char[] chars = data.toCharArray();
        int index = 0;

        StringBuilder decompressed = new StringBuilder();
        while (index < data.length()) {
            char chr = chars[index];

            if (digits.indexOf(chr) >= 0) {
                // actual numbers in the string and not repeat counts
                if (metadata.contains(String.valueOf(index))) {
                    decompressed.append(chr);
                } else {
                    char repeatedChar = chars[index + 1];
                    decompressed.append(String.valueOf(repeatedChar).repeat(Math.max(0, Integer.parseInt(String.valueOf(chr)))));
                    index++;
                }
            } else {
                decompressed.append(chr);
            }
            index++;
        }
        return decompressed.toString();
    }
}
