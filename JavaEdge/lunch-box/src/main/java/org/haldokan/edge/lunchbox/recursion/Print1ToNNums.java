package org.haldokan.edge.lunchbox.recursion;

import org.junit.jupiter.api.Test;

/**
 * Print Numbers from 1 to N / N to 1
 * Input: n = 5
 * Helps to understand pre-recursion and post-recursion actions.
 */
public class Print1ToNNums {

    private void print1toN(int num) {
        if (num == 0) {
            return;
        }
        print1toN(num - 1);
        System.out.println(num); // post-recursion action
    }

    private void printNto1(int num) {
        if (num == 0) {
            return;
        }
        System.out.println(num); // pre-recursion action
        printNto1(num - 1);
    }

    @Test
    void test1toN() {
        print1toN(5);
    }

    @Test
    void testNto1() {
        printNto1(5);
    }
}

