package org.haldokan.edge.sort;

import java.util.Arrays;

import org.junit.Test;

public class MergesortTest {
	@Test
	public void test1() throws Exception {
		Integer[] c1 = new Integer[] { 3, 4, 7, 8 };
		Integer[] c2 = new Integer[] { 1, 2, 5, 6, 9, 10 };
		Integer[] c = new Integer[c1.length + c2.length];
		Mergesort1.mergeSubArrays(c, c1, c2);
		System.out.println(Arrays.toString(c));
	}

	@Test
	public void test2() throws Exception {
		String[] c1 = new String[] { "bar", "baz", "foo" };
		String[] c2 = new String[] { "abu3bdo", "allah", "rabbo", "yahwah" };
		String[] c = new String[c1.length + c2.length];
		Mergesort1.mergeSubArrays(c, c1, c2);
		System.out.println(Arrays.toString(c));
	}

	@Test
	public void test3() throws Exception {
		// Integer[] c = new Integer[] { 2, 5, 7, 9, 10, 1, 3, 4, 6, 8, 11 };
		Integer[] d = new Integer[] { 11, 13, 14, 16, 20, 2, 3, 5, 7, 8, 10 };
		Mergesort1.mergeArrays(d, 0, d.length / 2, d.length);
		System.out.println(Arrays.toString(d));
	}

	@Test
	public void test4() throws Exception {
		Integer[] c = new Integer[] { 7, 4, 1, 3, 9, 3, 2, 8, 0, 5, 6 };
		System.out.println("input: " + Arrays.toString(c));
		Mergesort1.mergesortWithArrays("start", c, 0, c.length);
		System.out.println(Arrays.toString(c));
	}

	@Test
	public void test5() throws Exception {
		Integer[] c = new Integer[] { 7, 4, 1, 3, 9, 3, 2, 8, 0, 5, 6 };
		// Integer[] c = new Integer[] { 2, 5, 7, 9, 10, 1, 3, 4, 6, 8, 11 };
		Mergesort2.mergesort(c, 0, c.length);
		System.out.println(Arrays.toString(c));
	}

	@Test
	public void test6() throws Exception {
		Integer[] c = new Integer[] { 7, 10, 1, 3, 9, 3, 2, 8, 0, 5, 6 };
		Mergesort1.mergeWithSubArrays("start", c);
		System.out.println(Arrays.toString(c));
	}
}