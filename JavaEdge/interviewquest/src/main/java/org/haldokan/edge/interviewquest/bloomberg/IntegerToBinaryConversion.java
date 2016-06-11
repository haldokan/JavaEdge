package org.haldokan.edge.interviewquest.bloomberg;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

/**
 * My solution to a Bloomberg interview question
 * The Question: 3_STAR
 * convert an Integer to binary
 */
public class IntegerToBinaryConversion {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(17));

        IntegerToBinaryConversion driver = new IntegerToBinaryConversion();
        System.out.println(driver.convert(17));
    }

    public String convert(Integer num) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < 32; i++) {
            if (((num >> i) & 1) == 1) {
                stack.push(1);
            } else {
                stack.push(0);
            }
        }
        return stack.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
