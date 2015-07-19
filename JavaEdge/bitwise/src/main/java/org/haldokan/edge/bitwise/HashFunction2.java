package org.haldokan.edge.bitwise;

import java.util.function.Function;

/**
 * hash function that can be the target of a lambda in functional Java. Note how longs and strings are hashed
 * 
 * @author haldokan
 *
 */
public class HashFunction2 implements Function<Object, Integer> {

    // TODO not meant to cover every type conversion etc. tho I suspect it does
    @Override
    public Integer apply(Object obj) {
	Class<?> clazz = obj.getClass();

	if (clazz.isAssignableFrom(Long.class))
	    return hashLong((long) obj);
	if (clazz.isAssignableFrom(String.class))
	    return hashString((String) obj);
	if (clazz.isAssignableFrom(Double.class))
	    return hashLong(Double.doubleToLongBits((double) obj));
	if (clazz.isAssignableFrom(Float.class))
	    return 31 * Float.floatToIntBits((float) obj);

	return 31 * (int) obj;
    }

    private int hashLong(long l) {
	// unsigned right shift
	return 31 * (int) (l ^ (l >>> 32));
    }

    // hash a string by getting its bytes and xor'ing them with Long.MAX_VALUE on byte at a time
    private int hashString(String s) {
	byte[] bytes = s.getBytes();
	int hash = Integer.MAX_VALUE;
	int shift = 0;
	// 32-bit word
	int word32 = 0;
	for (byte b : bytes) {
	    word32 = b;
	    hash = hash ^ (word32 << shift);
	    shift = (shift + 8) % 32;
	}
	return hash;
    }
}
