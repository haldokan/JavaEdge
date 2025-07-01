package org.haldokan.edge.lunchbox.recursion;

import org.junit.jupiter.api.Test;

/**
 * Reverse a String (Character Array)
 * 👉 Base case: 1-character string. Learn how recursion builds back.
 */
public class ReverseString {
    private String reverse(String input) {
        return reverse(input, 0);
    }

    private String reverse(String input, int pos) {
        if (pos == input.length()) {
            return "";
        }
        pos += 1;
        return reverse(input, pos) + input.charAt(pos-1);
    }

    @Test
    void testIt() {
        System.out.println(reverse("foobar"));
        System.out.println(reverse("1234567890"));
    }
}
