package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @Pending: Provided solution is faulty. Kept here as base for future attempts if they come to pass
 * <p>
 * This is a tough question to solve in O(n) time and O(1) space - I thought I solved it and tests passed for the array I was
 * working with. But when I tested it on a sorted array in descending order (has the max heap quality) it failed miserably.
 * Using O(n) space it can be copied to another array and quick-sorted in O(nlogn) time.
 * <p>
 * I checked the comments on Careercup, where the question is posted, and I found an interesting idea for solving
 * the problem by turning it into a graph problem. Specifically shortest path using Dijikstra's algorithm. Not quite sure
 * of the merit of the solution and it won't be O(n), let alone O(logn).
 * <p>
 * Given a max-heap represented as an array, return the kth largest element without modifying the heap.
 * I was asked to do it in linear time, but was told it can be done in log time.
 * <p>
 * Created by haytham.aldokanji on 5/20/16.
 */
public class GetKthItemFromHeapWithoutModification {
    public static void main(String[] args) {
//        int[] heap = new int[]{9, 8, 7, 6, 5 ,4, 3, 2, 1};
        test1();
//        test2();
    }

    static void test1() {
        int[] heap = new int[]{9, 8, 3, 6, 7, 2, 1, 4, 5};
        int kthLargest = kthLargestItem(heap, 4);
        assertThat(kthLargest, is(6));
    }

    static void test2() {
        int[] heap = new int[]{9, 8, 3, 6, 7, 2, 1, 4, 5};

        int kthLargest = kthLargestItem(heap, 1);
        assertThat(kthLargest, is(9));

        kthLargest = kthLargestItem(heap, 2);
        assertThat(kthLargest, is(8));

        kthLargest = kthLargestItem(heap, 3);
        assertThat(kthLargest, is(7));

        kthLargest = kthLargestItem(heap, 4);
        assertThat(kthLargest, is(6));

        kthLargest = kthLargestItem(heap, 5);
        assertThat(kthLargest, is(5));

        kthLargest = kthLargestItem(heap, 6);
        assertThat(kthLargest, is(4));

        kthLargest = kthLargestItem(heap, 7);
        assertThat(kthLargest, is(3));

        kthLargest = kthLargestItem(heap, 8);
        assertThat(kthLargest, is(2));

        kthLargest = kthLargestItem(heap, 9);
        assertThat(kthLargest, is(1));
    }

    static int kthLargestItem(int[] heap, int kth) {
        Deque<Integer> evalQueue = new ArrayDeque<>();
        // can use BitSet instead
        boolean[] visited = new boolean[heap.length];

        int activeIndex = 0;
        evalQueue.add(heap[activeIndex]);
        int numEvaluated = 1;
        int kthItem = heap[activeIndex];
        visited[activeIndex] = true;

        while (!evalQueue.isEmpty()) {
            Integer val = evalQueue.pop();

            if (numEvaluated == kth || numEvaluated >= heap.length) {
                kthItem = val;
                break;
            }

            int leftIndex = activeIndex * 2 + 1;
            int rightIndex = leftIndex + 1;

            if (rightIndex < heap.length
                    && !visited[rightIndex]
                    && (visited[leftIndex] || heap[rightIndex] > heap[leftIndex])) {
                activeIndex = rightIndex;
                evalQueue.push(heap[rightIndex]);
                numEvaluated++;
                visited[rightIndex] = true;
            } else if (leftIndex < heap.length && !visited[leftIndex]) {
                activeIndex = leftIndex;
                evalQueue.push(heap[leftIndex]);
                numEvaluated++;
                visited[leftIndex] = true;
            } else {
                activeIndex = (activeIndex - 1) / 2;
                evalQueue.push(heap[activeIndex]);
            }
        }
        return kthItem;
    }
}
