package org.haldokan.edge.bitwise;

/**
 * Implement a Bit array that supports some of the java.util.BitSet operations
 * BitSet is a re-sizable array of words with each word being 64-bit long on
 * 64-bit JVMs (32-bit on 32-bit JVMs)
 * 
 * @author haldokan
 *
 */
public class BitArray {
	private final long[] bitarray;
	private final int theorLen;

	public static void main(String[] args) {
		BitArray ba = new BitArray(250);
		int x = 100;
		int x1 = 255;
		ba.setBit(x);
		ba.setBit(x1);
		for (long e : ba.bitarray) {
			System.out.print("0X" + Long.toHexString(e) + ", ");
		}
		System.out.println();

		System.out.println(ba.getBit(x));
		System.out.println(ba.getBit(x1));
	}

	public BitArray(int len) {
		// simplify by making the len a power of 2
		theorLen = pow2Len(len);
		this.bitarray = new long[theorLen / 64];
	}

	private int pow2Len(int len) {
		double log2Len = Math.ceil(32 - Integer.numberOfLeadingZeros(len - 1));
		return (int) Math.pow(2, log2Len);
	}

	public void setBit(int i) {
		int[] index = bitindex(i);
		bitarray[index[0]] = bitarray[index[0]] | (1L << index[1]);
	}

	public boolean getBit(int i) {
		int[] index = bitindex(i);
		// instead of '^' with 0 we can '&' with 1
		long bit = bitarray[index[0]] >> index[1] ^ 0;
		return bit == 1;
	}

	private int[] bitindex(int i) {
		if (i < 0 || i > theorLen - 1)
			throw new IndexOutOfBoundsException("index " + i
					+ " is out of range [0 - " + theorLen + "]");
		int wordindex = i / 64;
		int bitorder = 64 - (i % 64);
		return new int[] { wordindex, bitorder };
	}
}