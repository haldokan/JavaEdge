package org.haldokan.edge.lunchbox.recursion;

import java.util.HashMap;
import java.util.Map;

/**
 * >>> Fibonacci (Naive Recursion)
 * Input: n → Output: nth Fibonacci number
 * The Fibonacci sequence is defined as follows:
 * 0,1,1,2,3,5,8,13,21,34,...
 */
public class Fibonacci {
    private static final Map<Integer, Integer> memorization = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(silly(9));
        System.out.println(memo(9));
        System.out.println(memo_AI(9));
    }

    // Exponential time complexity (O(2^n )) bcz it recurse over all perviously computed Fibonacci numbers multiple times
    private static int silly(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return silly(n - 1) + silly(n - 2);
    }

    // This dynamic-programming approach with memorization ensures that each Fibonacci number is computed only once,
    // dramatically reducing the number of calls and improving performance from an exponential time complexity (O(2^n ))
    // to a linear one (O(n)).
    private static int memo(int n) {
        if (n == 0) { // these could be combined as if n <= 1 return n;
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (memorization.containsKey(n)) {
            return memorization.get(n);
        } else {
            memorization.put(n, memo(n - 1) + memo(n - 2));
            return memorization.get(n);
        }
    }

    private static int memo_AI(int n) {
        // 1. Check if the result is already in the cache
        if (memorization.containsKey(n)) {
            return memorization.get(n);
        }

        // 2. Handle the base cases
        if (n <= 1) {
            return 1;
        }
        // 3. Compute the result and store it in the cache before returning
        int result = memo_AI(n - 1) + memo_AI(n - 2);
        memorization.put(n, result);

        return result;
    }
}
