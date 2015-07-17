package org.haldokan.edge.sort;

import java.util.Arrays;

import org.junit.Test;

public class QuicksortTest {

    @Test
    public void test1() {
	int[] d = new int[] { 7, 10, 1, 3, 9, 3, 2, 8, 0, 5, 6 };
	System.out.println("input: " + Arrays.toString(d));
	Quicksort.sort(d, 0, d.length - 1);
	System.out.println("sorted: " + Arrays.toString(d));
    }
}