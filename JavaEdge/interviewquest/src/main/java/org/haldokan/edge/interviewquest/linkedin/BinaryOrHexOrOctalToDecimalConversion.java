package org.haldokan.edge.interviewquest.linkedin;

import java.util.Iterator;

/**
 * My solution to a Linkedin interview question.
 * 
 * 
 * Convert a character string into a value based on the radix or base.
 * 
 * Input parameters:
 * 
 * 1. char *str such as "3523" (in decimal), "11001" (in binary), "a1f38c" (in hex)
 * 
 * 2. radix or base(such as decimal:10, binary:2, hex:16, octal:8)
 * 
 * Return: integer value (not unsigned) that is calculated based on the input string and radix or base.
 * 
 * for example: string is "343432" (base 10): return value 343432 string is "10010" (base 2): return value is 18 string
 * is "a1b" (base 16): return value is 187.
 * 
 * @author haldokan
 *
 */
public class BinaryOrHexOrOctalToDecimalConversion {
    public static void main(String[] args) {
	BinaryOrHexOrOctalToDecimalConversion driver = new BinaryOrHexOrOctalToDecimalConversion();
	System.out.println(driver.toDecimalBase("49bfaff", 16)); // in decimal 77331199
	System.out.println(driver.toDecimalBase("100100110111111101011111111", 2)); // in decimal 77331199
    }

    // implemented for hex and bin; octal is similar and decimal is the trivial case
    public int toDecimalBase(String val, int radix) {
	switch (radix) {
	case 16:
	    return fromHexToDecimal(val);
	case 2:
	    return fromBinaryToDecimal(val);
	default:
	    throw new IllegalArgumentException("Radix not supported " + radix);
	}
    }

    private int fromHexToDecimal(String val) {
	int power = 0;
	int rslt = 0;
	for (int i = val.length(); i > 0; i--) {
	    rslt += fromHexToDecimalDigit(val.substring(i - 1, i)) * Math.pow(16, power);
	    power++;
	}
	return rslt;
    }
    
    private int fromBinaryToDecimal(String val) {
	int power = 0;
	int rslt = 0;
	for (int i = val.length(); i > 0; i--) {
	    rslt += Integer.valueOf(val.substring(i - 1, i)) * Math.pow(2, power);
	    power++;
	}
	return rslt;
    }

    private int fromHexToDecimalDigit(String hex) {
	switch (hex.toUpperCase()) {
	case "A":
	    return 10;
	case "B":
	    return 11;
	case "C":
	    return 12;
	case "D":
	    return 13;
	case "E":
	    return 14;
	case "F":
	    return 15;
	default:
	    return Integer.valueOf(hex);
	}
    }
}
