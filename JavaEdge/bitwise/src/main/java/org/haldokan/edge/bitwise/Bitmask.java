package org.haldokan.edge.bitwise;

/**
 * how to find out if a bit is set? How to apply a bitmask; for example Linux file permission rwx
 * @author haldokan
 *
 */
public class Bitmask {

    public static void main(String[] args) {
	findSetBits();
	bitmask();
    }

    public static void findSetBits() {
	long lnum = Long.parseUnsignedLong("10");
	System.out.println("print the bits of unsigned " + lnum);

	while (lnum > 0) {
	    // this prints the bit from low to high order
	    System.out.print((lnum & 1));
	    lnum = lnum >> 1;
	}
	System.out.println();
    }

    // play on Linux file permissions rwx for user and group
    public static void bitmask() {
	// permission u: rwx
	long perm = 0x0000;
	long bmask = 0x01C0; // u:rwx
	long rwx = perm | bmask;
	System.out.println("rwx " + Long.toBinaryString(rwx));

	// add read perm to group
	long rwxr = rwx | 0x01E0;
	System.out.println("rwxr " + Long.toBinaryString(rwxr));

	// remove w from u
	long r_xr = rwxr & 0x0160;
	System.out.println("r_xr " + Long.toBinaryString(r_xr));
    }
}