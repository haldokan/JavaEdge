package org.haldokan.edge.bitwise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 64-bit checksum algorithm. It can be extended to any length easily
 * @author haldokan
 *
 */
public class Checksum {

    public static void main(String[] args) throws IOException {
	String s = "practice makes perfect!";
	long cs = checksum(s.getBytes());
	// System.out.println("0X" + Long.toHexString(cs));
	test();
    }

    public static long checksum(byte[] data) {
	long cs = Long.MAX_VALUE;
	int shift = 0;
	long word = 0L;
	for (byte b : data) {
	    word = b;
	    cs = cs ^ (word << shift);
	    shift = (shift + 8) % 64;
	}
	return cs;
    }

    public static void test() throws IOException {
	byte[] f1 = Files.readAllBytes(Paths.get("c:\\temp\\test1"));
	long cs1 = checksum(f1);

	byte[] f2 = Files.readAllBytes(Paths.get("c:\\temp\\test2"));
	long cs2 = checksum(f2);

	byte[] f3 = Files.readAllBytes(Paths.get("c:\\temp\\test3"));
	long cs3 = checksum(f3);

	System.out.println("cs1/cs2/cs3: " + Long.toHexString(cs1) + "/" + Long.toHexString(cs2) + "/"
		+ Long.toHexString(cs3));

	System.out.println("equals? " + (cs1 == cs2));
	System.out.println("equals? " + (cs1 == cs3));
	System.out.println("equals? " + (cs2 == cs3));
    }
}