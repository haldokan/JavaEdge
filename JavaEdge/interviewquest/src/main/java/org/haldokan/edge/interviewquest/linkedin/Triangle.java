package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;

/**
 * My solution to a Linkedin interview question. I present 2 solutions: general sln, and another sln when we can assume
 * that triangle sides are adjacent in the segments array
 * 
 * Three segments of lengths A, B, C form a triangle iff
 *
 * A + B > C
 * 
 * B + C > A
 * 
 * A + C > B
 *
 * e.g. 6, 4, 5 can form a triangle, but 10, 2, 7 can't
 *
 * Given a list of segments lengths algorithm should find at least one triplet of segments that form a triangle (if
 * any). Method should return an array of either:
 * 
 * - 3 elements: segments that form a triangle (i.e. satisfy the condition above)
 * 
 * - empty array if there are no such segments
 */
public class Triangle {

    public static void main(String[] args) {
	Triangle t = new Triangle();
	// int[] segs = new int[]{6, 4, 5};
	// int[] segs = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	// int [] segs = new int[]{10 , 2, 7}; // can't form triangle
	// int[] segs = new int[] {6, 4}; // can't form triangle
	int[] segs = new int[] { 0, 0, 0, 0, 6, 0, 0, 4, 0, 0, 0, 5 };

	System.out.println(Arrays.toString(t.getTriangleSides(segs)));
    }

    /**
     * The general case is for the sides not to be adjacent which makes complexity O(n^3)
     * 
     * @param s
     * @return
     */
    public int[] getTriangleSides(int[] s) {
	for (int i = 0; i < s.length; i++) {
	    for (int j = i + 1; j < s.length; j++) {
		for (int k = j + 1; k < s.length; k++) {
		    int a = s[i];
		    int b = s[j];
		    int c = s[k];
		    if (a + b > c && b + c > a && a + c > b)
			return new int[] { a, b, c };
		}
	    }
	}
	return new int[] {};
    }

    /**
     * assuming the sides are adjacent the problem can be solved in O(n^2)
     * 
     * @param s
     * @return
     */
    public int[] getTriangleSides_sidesAdjacent(int[] s) {
	int[] notfound = new int[] {};

	for (int i = 0; i < s.length; i++) {
	    if (s.length - i < 3)
		return notfound;
	    for (int j = i + 1; j < i + 3; j += 2) {
		if (s.length - j < 2)
		    return notfound;
		int a = s[i];
		int b = s[j];
		int c = s[j + 1];
		if (a + b > c && b + c > a && a + c > b)
		    return new int[] { a, b, c };

	    }
	}
	return notfound;
    }
}