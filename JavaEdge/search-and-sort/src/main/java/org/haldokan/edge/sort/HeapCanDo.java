package org.haldokan.edge.sort;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class HeapCanDo {
    private final List<Integer> heap = new ArrayList<>();

    private void add(Integer item) {
        heap.add(item);
        bubbleUp();
    }

    private Integer pop() {
        if (!heap.isEmpty()) {
            var top = heap.getFirst();
            var tail = heap.getLast();

            heap.set(0, tail);
            heap.removeLast();
            dribbleDown();
            return top;
        }
        return null;
    }

    private int peek() {
        return heap.getFirst();
    }

    private int parentIndex(int index) {
        return (index - 1) / 2;
    }

    private int leftChildIndex(int index) {
        return 2 * index + 1;
    }

    private int rightChildIndex(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        var temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void bubbleUp() {
        if (heap.isEmpty() || heap.size() == 1) {
            return;
        }
        int index = heap.size() - 1;
        while (index > 0) {
            int pix = parentIndex(index);
            if (pix >= 0 && heap.get(index) > heap.get(pix)) {
                int tmp = heap.get(index);
                heap.set(index, heap.get(pix));
                heap.set(pix, tmp);
            }
            index = pix;
        }
    }

    private void dribbleDown() {
        if (heap.isEmpty() || heap.size() == 1) {
            return;
        }

        int index = 0;
        while (index < heap.size()) { // It can be optimized (?) to get the largest of children and swap
            var leftx = leftChildIndex(index);
            if (leftx < heap.size()) {
                if (heap.get(index) < heap.get(leftx)) {
                    swap(index, leftx);
                }
                var rightx = rightChildIndex(index);
                if (rightx < heap.size()) {
                    if (heap.get(index) < heap.get(rightx)) {
                        swap(index, rightx);
                    }
                }
            }
            index = leftx;
        }
    }

    @Test
    public void testIt() {
        int[] arr = new int[] {12, 5, 20, 8, 15, 3};
        for (int j : arr) {
            add(j);
        }
        //        20 (Index 0)
        //       /  \
        //      /    \
        //     15     12 (Index 1, 2)
        //    /  \   /
        //   /    \ /
        //  5     8  3 (Index 3, 4, 5)
        System.out.println(heap); // [20, 15, 12, 5, 8, 3]
        pop();
        //        15
        //       /  \
        //      /    \
        //     8      12
        //    / \
        //   /   \
        //  5     3

        // or this:
        //        15
        //       /  \
        //      /    \
        //     12     3
        //    /  \
        //   /    \
        //  5      8
        // or this:
        //         15 (Index 0)
        //       /  \
        //      /    \
        //     8      12 (Index 1, 2)
        //    / \
        //   /   \
        //  3     5 (Index 3, 4)
        System.out.println(heap); // [15, 8, 12, 5, 3] or [[15, 12, 3, 5, 8] or [15, 8, 12, 3, 5]
    }

    @Test
    public void testIt2() {
        PriorityQueue<Token> pq = new PriorityQueue<>(Comparator.comparingInt(o -> -o.val));
        Token[] arr = new Token[]{
                new Token("id1", 12),
                new Token("id2", 5),
                new Token("id3", 20),
                new Token("id4", 8),
                new Token("id5", 15),
                new Token("id5", 3)
        };
        pq.addAll(Arrays.asList(arr));
        //        20 (Index 0)
        //       /  \
        //      /    \
        //     15     12 (Index 1, 2)
        //    /  \   /
        //   /    \ /
        //  5     8  3 (Index 3, 4, 5)
        System.out.println(pq); // [20, 15, 12, 5, 8, 3]

        // This does not work bcz heapify will not produce a heap out of invalid heap. We have to find the node in O(n), the
        // rmv it and add it aftewards
        arr[1].val = 25;
        var head = pq.remove();
        pq.add(head);
        System.out.println(pq);
    }

    private static final class Token {
        private final String id;
        private int val;

        public Token(String id, int val) {
            this.id = id;
            this.val = val;
        }

        public void upd(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.format("[%s,%d]", id, val);
        }
    }
}
