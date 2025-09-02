package org.haldokan.edge.lunchbox.recursion;

public class Factorial {
    public static void main(String[] args) {
        System.out.println(new Factorial().fac(5));
        System.out.println(new Factorial().fac(4));
    }

    private int fac(int n) {
        if (n == 0)  {
            return 1;
        }
        return n * fac(n - 1);
    }
}
