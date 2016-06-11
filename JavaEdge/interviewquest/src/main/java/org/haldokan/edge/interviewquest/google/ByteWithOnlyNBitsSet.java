package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - I made a general solution for checking if N bits set on a byte
 * Given 1 byte. Write a function that checks that it have exactly 3 set bits.
 * The Question: 3_STAR
 *
 * Created by haytham.aldokanji on 5/21/16.
 */
public class ByteWithOnlyNBitsSet {

    public static void main(String[] args) {
        ByteWithOnlyNBitsSet driver = new ByteWithOnlyNBitsSet();
        assertThat(driver.hasNBitsSet((byte) 1, 1), is(true));
        assertThat(driver.hasNBitsSet((byte) 1, 2), is(false));
        assertThat(driver.hasNBitsSet((byte) 7, 3), is(true));
        assertThat(driver.hasNBitsSet((byte) 6, 3), is(false));
        assertThat(driver.hasNBitsSet((byte) 8, 3), is(false));
        assertThat(driver.hasNBitsSet((byte) 9, 3), is(false));
        assertThat(driver.hasNBitsSet((byte) 10, 3), is(false));
        assertThat(driver.hasNBitsSet((byte) 11, 3), is(true));
    }

    public boolean hasNBitsSet(byte bite, int numBitsSet) {
        int byteLen = 0x8;
        if (numBitsSet > byteLen) {
            throw new IllegalArgumentException("byte has only 8 bits - " + numBitsSet);
        }

        int setBitCounter = 0;
        for (int i = 0x0; i < byteLen; i++) {
            if ((bite >> i & 0x1) == 1) {
                setBitCounter++;
            }
        }
        return setBitCounter == numBitsSet;
    }
}
