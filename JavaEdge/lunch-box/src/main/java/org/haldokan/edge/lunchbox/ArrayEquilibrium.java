package org.haldokan.edge.lunchbox;

/**
 * A point of equilibrium at index p in a 0-based array of length N is a point 0 >= p < N where the sum of elements b/w
 * 0 and p - 1 is equal to that b/w p+1 and N-1. assum that the sum of elements to the left of index 0 is 0. write a
 * program that finds all such equi-points in an array
 */

public class ArrayEquilibrium {

    public static void main(String[] args) {
        int[] a = new int[]{2, 2, 2, 77, 12, -1, 0, -2, -3, 0};
        if (a.length == 0)
            return;

        int sum = 0;
        for (int e : a)
            sum += e;

        int sumleft = 0;
        int sumright = sum - a[0];
        if (sumleft == sumright)
            System.out.println("p/eqlsum: " + 0 + "/" + sumleft);

        if (a.length > 1) {
            sumright = 0;
            sumleft = sum - a[a.length - 1];
            if (sumleft == sumright)
                System.out.println("p/eqlsum: " + (a.length - 1) + "/" + sumleft);
        }

        sumleft = 0;
        sumright = sum;
        for (int i = 1; i < a.length - 1; i++) {
            sumleft += a[i - 1];
            sumright = sum - (a[i] + sumleft);
            // System.out.println("e/sl/sr: " + a[i] + "/" + sumleft + "/" +
            // sumright);
            if (sumleft == sumright)
                System.out.println("p/eqlsum: " + i + "/" + sumleft);
        }

    }
}