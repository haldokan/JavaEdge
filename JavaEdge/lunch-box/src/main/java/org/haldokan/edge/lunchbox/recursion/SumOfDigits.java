package org.haldokan.edge.lunchbox.recursion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Input: 1234 → Output: 10 (1+2+3+4)
 */
public class SumOfDigits {
    private int sum(String num, int pos) {
        if (pos == num.length()) {
            return 0;
        }
        return num.charAt(pos) - '0' + sum(num, pos + 1);
    }

    @Test
    public void testIt() {
        String num = "1234";
        int result = sum(num, 0);
        assertEquals(10, result); // 1 + 2 + 3 + 4 = 10
    }
}
