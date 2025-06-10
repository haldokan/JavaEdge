package org.haldokan.edge.interviewquest.bloomberg;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Bloomberg interview question - question could be stated much better...
 * We basically need to concatenate the smallest strings first but since already concatenated strings can still be smaller
 * than remaining strings we need a data structure that will always give us the smallest 2 strings. For that we use a min
 * heap on the string length. We pop the smallest 2 strings at each iteration (log n) and we add the resulting string
 * to the heap in (log n) as well.
 * Space complexity is n for the array holding the strings and n for the heap which leads to O(n).
 * Time complexity (n log n) for heaping the strings + (n log n) for popping the strings from heap + (n/2 log n) for pushing
 * the concatenated strings back to the heap. This yield 2.5n * log n or O(n log n) complexity.
 * <p>
 * The Question: 4_STAR
 * <p>
 * There's a function that concatenates two strings and returns the length of the resultant string. When called upon a
 * series of strings how do we minimize the cost of using this function.
 * <p>
 * Example: Let's say we have 3 strings, A= "abc", B="def", C = "gh".
 * Now cost of merging AB = 6 and cost of merging the resultant string with C is 8. Thus the total cost is 6 + 8 = 14.
 * However, if we merge A and C, then the cost is 5 and then merge B with this, the cost is 8, so the total cost is 13.
 * Find an algorithm that minimizes the cost of adding such a series of strings.
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class ConcatenateStringInMinCost {
    public static void main(String[] args) {
        ConcatenateStringInMinCost driver = new ConcatenateStringInMinCost();
        driver.test();
    }

    private int concatenate(List<String> strings) {
        // min heap on string length
        Queue<String> heap = new PriorityQueue<>((s1, s2) -> s1.length() - s2.length());
        strings.stream().forEach(heap::add);

        int cost = 0;
        for (; ; ) {
            String str1 = null, str2 = null;
            if (!heap.isEmpty()) {
                str1 = heap.poll();
            }
            if (!heap.isEmpty()) {
                str2 = heap.poll();
            }
            if (str1 != null && str2 != null) {
                ConcatResult result = concatenate(str1, str2);
                heap.add(result.str);
                cost += result.cost;
            } else {
                break;
            }
        }
        return cost;
    }

    private ConcatResult concatenate(String str1, String str2) {
        return new ConcatResult(str1 + str2, str1.length() + str2.length());
    }

    private void test() {
        List<String> strings = Lists.newArrayList("abc", "def", "gh");
        int cost = concatenate(strings);
        assertThat(cost, is(13));
    }

    private static class ConcatResult {
        private final String str;
        private final int cost;

        public ConcatResult(String str, int cost) {
            this.str = str;
            this.cost = cost;
        }
    }
}
