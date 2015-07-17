package org.haldokan.edge.bitwise;

import java.util.Deque;
import java.util.LinkedList;

public class JdkBitSet {
    public static void main(String[] args) {
	int bitmask = 0x000F;
	int val = 0x2222;
	// prints "2"
	System.out.println(val & bitmask);
	System.out.println(bitmask & val);
	System.out.println(val | bitmask);
	System.out.println(val ^ bitmask);
	System.out.println(0x2 ^ 0x2);
	System.out.println(0x2 ^ 0x1);

	int num = 76543210;
	String hxnum = Integer.toHexString(num);
	System.out.println(hxnum);
	System.out.println(Integer.parseInt(hxnum, 16));

	System.out.println(num ^ 0);

	System.out.println("****");
	System.out.println(10 >> 1);
	System.out.println(10 >> 2);
	System.out.println(10 >> 3);
	System.out.println(10 >> 4);
	System.out.println("----");
	System.out.println(1 << 1);
	System.out.println(1 << 2);
	System.out.println(1 << 3);
	System.out.println(1 << 4);

	System.out.println("*** want to know if a bit is set on a number");
	long lnum = Long.parseUnsignedLong("10");
	System.out.println(Long.toUnsignedString(lnum));

	while (lnum > 0) {
	    // this prints the bit from low to high order
	    System.out.print((lnum & 1) + ", ");
	    lnum = lnum >> 1;
	}

	// implement unix ugo rwx
	System.out.println("---");
	long perm = 0x0000;
	long bmask = 0x01C0; // u:rwx
	long urwx = perm | bmask;
	printBits("urwx", urwx);

	// give read perm to group
	perm = urwx | 0x01E0;
	printBits("rwxr", perm);

	// remove w from u
	perm = perm & 0x0160;
	printBits("r_xr", perm);

    }

    public static void printBits(String name, long n) {
	Deque<Long> bits = new LinkedList<>();
	long num = n;
	while (num > 0) {
	    bits.addFirst(num & 1);
	    num = num >> 1;
	}
	System.out.println(name + "(" + n + ") " + bits);
    }
}