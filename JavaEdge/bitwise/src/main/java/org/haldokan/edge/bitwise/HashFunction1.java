package org.haldokan.edge.bitwise;

import java.nio.ByteBuffer;

/**
 * Hash function that uses our checksum class. I also show how to hash a long to an int value
 *
 * @author haldokan
 */
public class HashFunction1 {
    public static void main(String[] args) {
        ByteBuffer bf = ByteBuffer.allocate(64);
        bf.putLong(Double.doubleToLongBits(73987.3434d));
        System.out.println("0x" + Long.toHexString(hash(bf.array())));

        System.out.println("0x" + Long.toHexString(hash("somestring".getBytes())));
        System.out.println("0x" + Integer.toHexString(hashDouble(4324324.98743d)));
    }

    // java hashCode returns int...
    public static long hash(byte[] ba) {
        return Checksum.checksum(ba);
    }

    // clear that longs can be hashed the same way
    public static int hashDouble(double d) {
        long ln = Double.doubleToLongBits(d);
        System.out.println(ln);
        // hashCode in java is int. A long number size is 2 ints thus the 32 lower
        // order bits can be the same b/w 2 longs so 2 different longs
        // hash to the same value. To remedy that we xor the higer order 4 bytes
        // of the long to the lower order 4 bytes
        // now the higher order 4 bytes are shifted to be lower order 4 bytes
        // and we have 0s for the higher order 4 bytes
        long ln1 = ln >>> 32;
        // xor the the 2 number: the lower order 4 byes now xor with the
        // previous higher order 4 bytes rendering them different for 2
        // different long number.
        // higher order 4 bytes
        long ln3 = ln ^ ln1;
        // casting to int now removes the higher order 4 bytes leaving us with
        // xor'ed lower order bytes integer.
        int in = (int) ln3;
        return in;
    }
}