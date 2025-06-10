package org.haldokan.edge.interviewquest.facebook;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * You are given an array of non-negative integers (0, 1, 2 etc). The value in each element represents the
 * number of hops you may take to the next destination. Write a function that determines when you start
 * from the first element whether you will be able to reach the last element of the array.
 * <p>
 * if a value is 3, you can take either 0, 1, 2 or 3 hops.
 * <p>
 * For eg: for the array with elements 1, 2, 0, 1, 0, 1, any route you take from the first element,
 * you will not be able to reach the last element.
 * <p>
 * Created by haytham.aldokanji on 5/11/16.
 */
public class HoppingInArray {
    public static void main(String[] args) {
        HoppingInArray driver = new HoppingInArray();
        driver.test();
    }

    public boolean canHopToEnd(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Input is null or empty");
        }
        Deque<Hop> hopStack = new ArrayDeque<>();
        boolean[] deadends = new boolean[arr.length];

        Hop hop = new Hop(0, arr[0], deadends);
        hopStack.push(hop);

        while (!(hopStack.isEmpty() || hop.atDestination(arr))) {
            Hop currentHop = hopStack.peek();
            if (deadends[currentHop.getIndex()]) {
                hopStack.pop();
                continue;
            }
            Optional<Hop> potentialHop = currentHop.hop(arr, deadends);
            if (potentialHop.isPresent()) {
                hop = potentialHop.get();
                hopStack.push(hop);
            }
        }
        return hop.atDestination(arr);
    }

    // I think this is simpler than the one above
    public boolean canHopToEnd2(int[] arr) {
        List<Integer> stack = new ArrayList<>();
        for (int i = 0; i <= arr[0]; i++) {
            stack.add(i);
        }
        while (!stack.isEmpty()) {
            int index = stack.remove(stack.size() - 1);
            if (index == arr.length - 1) {
                return true;
            }
            for (int i = 1; i <= arr[index]; i++) {
                int index2 = index + i;
                if (index2 < arr.length) {
                    stack.add(index2);
                }
            }
        }
        return false;
    }

    private void test() {
        int[] arr = new int[]{1, 2, 0, 3, 0, 1, 2, 0, 0};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{1, 2, 0, 3, 0, 1, 2, 0, 4};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{7};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{1, 2, 0, 4, 0, 1, 2, 0, 0};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{1, 2, 0, 7, 0, 1, 2, 0, 0};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{1, 2, 0, 4, 0, 2, 1, 1, 0, 3};
        assertThat(canHopToEnd(arr), is(false));

        arr = new int[]{1, 2, 1, 5, 0, 2, 1, 1, 0, 2, 0, 3};
        assertThat(canHopToEnd(arr), is(false));

        arr = new int[]{1, 2, 1, 6, 0, 2, 1, 1, 0, 2, 0, 3};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{1, 3, 1, 6, 0, 2, 1, 1, 0, 2, 0, 3};
        assertThat(canHopToEnd(arr), is(true));

        arr = new int[]{3, 1, 1, 0, 6, 0, 2, 1, 1, 0, 2, 0, 3};
        assertThat(canHopToEnd(arr), is(false));
    }

    private static final class Hop {
        private final int index;
        private final int val;
        private int currentVal;

        public Hop(int index, int val, boolean[] deadends) {
            this.index = index;
            this.val = val;
            this.currentVal = val;
            if (val == 0) {
                deadends[index] = true;
            }
        }

        public int getIndex() {
            return index;
        }

        public int getVal() {
            return val;
        }

        @Override
        public String toString() {
            return "Hop{" +
                    "index=" + index +
                    ", val=" + val +
                    ", currentVal=" + currentVal +
                    '}';
        }

        public Optional<Hop> hop(int arr[], boolean[] deadends) {
            if (currentVal == 0) {
                throw new IllegalStateException(this.toString());
            }
            int nextIndex = index + currentVal;
            currentVal--;
            if (currentVal == 0) {
                deadends[index] = true;
            }
            if (nextIndex < arr.length) {
                return Optional.of(new Hop(nextIndex, arr[nextIndex], deadends));
            } else {
                return Optional.empty();
            }
        }

        private boolean atDestination(int[] arr) {
            return index == arr.length - 1;
        }
    }
}
