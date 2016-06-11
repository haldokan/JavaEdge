package org.haldokan.edge.dynamicprog;

/**
 * Solution is based on the dynamic programming implementation presented by Steven S. Skiena in his book Algorithm
 * Design Manual
 * The Question: 3_STAR
 * @author haldokan
 */
public class DynamicFibonacci {
    public static void main(String[] args) {
        int num = 50;

        // calling fibRecursive with the values greater than 20 can take hours;
        // not with dynamic programming
        // System.out.println("fib(" + num + "): " + fibRecursive(num));
        System.out.println("dynfib with storage (" + num + "): " + dynfibWithStorage(num));
        System.out.println("dynfib w/o storage (" + num + "): " + dynfibWithoutStorage(num));
        System.out.println("dynfib w/o storage2 (" + num + "): " + dynfibWithoutStorage2(num));
    }

    // stores the whole sequence
    public static long dynfibWithStorage(int num) {
        long[] fib = new long[num];

        fib[0] = 0;
        fib[1] = 1;

        for (int i = 2; i < num; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib[num - 1];
    }

    // stores the last 2 numbers which is what is needed to compute the next
    // number in the sequence
    // we can also replace the array fib of length 2 with 2 vars
    public static long dynfibWithoutStorage(int num) {
        if (num == 0)
            return 0;

        long[] fib = new long[2];

        fib[0] = 0;
        fib[1] = 1;

        for (int i = 2; i < num; i++) {
            long tmp = fib[1];
            fib[1] = fib[1] + fib[0];
            fib[0] = tmp;
        }
        return fib[1];
    }

    // w/o using arrays
    public static long dynfibWithoutStorage2(long num) {
        long r = 0;
        if (num == 0 || num == 1)
            r = 0;
        if (num == 2)
            r = 1;
        long n1 = 0;
        long n2 = 1;

        for (int i = 3; i <= num; i++) {
            r = n1 + n2;
            n1 = n2;
            n2 = r;
        }
        return r;
    }

    // recursive fib has exponential complexity
    public static long fibRecursive(int num) {
        if (num == 0 || num == 1)
            return 0;
        if (num == 2)
            return 1;
        return fibRecursive(num - 1) + fibRecursive(num - 2);
    }
}