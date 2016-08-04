package org.haldokan.edge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Implementation of all heap operations: pop, push, dribbleDown and bubbleup.
 * In the JDK PriorityQueue is implemented using
 * the heap data structure
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 8/4/16.
 */
public class HeapSort<T> {
    private final List<T> heap = new ArrayList<>();
    // the BiFunction allows building min or max  heap
    private final BiFunction<T, T, Boolean> heapFunc;

    public HeapSort(BiFunction<T, T, Boolean> heapFunc) {
        this.heapFunc = heapFunc;
    }

    public static void main(String[] args) {
        // jdkheap();
        testMinHeap();
        testMaxHeap();
    }

    private static void testMinHeap() {
        HeapSort<Integer> heap = new HeapSort<>((v1, v2) -> v1 < v2);
        heap.push(1492);
        heap.push(1783);
        heap.push(1776);
        heap.push(1804);
        heap.push(1865);
        heap.push(1945);
        heap.push(1963);
        heap.push(1918);
        heap.push(2001);
        heap.push(1941);

        Integer val;
        List<Integer> result = new ArrayList<>();
        do {
            val = heap.pop();
            if (val != null) {
                result.add(val);
            }
        } while (val != null);
        System.out.printf("%s%n", result);
        assertThat(result.toArray(new Integer[result.size()]), is(result.stream().sorted().toArray(Integer[]::new)));
    }

    private static void testMaxHeap() {
        HeapSort<Integer> heap = new HeapSort<>((v1, v2) -> v1 > v2);
        heap.push(1492);
        heap.push(1783);
        heap.push(1776);
        heap.push(1804);
        heap.push(1865);
        heap.push(1945);
        heap.push(1963);
        heap.push(1918);
        heap.push(2001);
        heap.push(1941);

        Integer val;
        List<Integer> result = new ArrayList<>();
        do {
            val = heap.pop();
            if (val != null) {
                result.add(val);
            }
        } while (val != null);
        System.out.printf("%s%n", result);
        assertThat(result.toArray(new Integer[result.size()]),
                is(result.stream().sorted((v1, v2) -> v2 - v1).toArray(Integer[]::new)));
    }

    // heap maintains weak order: elements are not sorted but we can always get
    // the min (or max) in log(n)
    private static void jdkheap() {
        Queue<Integer> heap = new PriorityQueue<>();
        heap.add(12);
        heap.add(10);
        heap.add(-3);
        heap.add(4);
        heap.add(24);
        heap.add(0);

        System.out.println(heap.peek());
        System.out.println(heap);
    }

    public void push(T e) {
        heap.add(e);
        bubbleup(heap.size() - 1);
    }


    public T pop() {
        if (heap.isEmpty())
            return null;
        if (heap.size() == 1)
            return heap.remove(0);

        T top = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));

        if (heapFunc.apply(top, heap.get(0))) {
            dribbleDown(0);
        }
        return top;
    }

    // called after adding a new element at the bottom of the heap (index size - 1)
    private void bubbleup(int index) {
        if (index == 0)
            return;
        T newelem = heap.get(index);
        int px = (index - 1) / 2;
        if (px >= 0) {
            if (heapFunc.apply(newelem, heap.get(px))) {
                heap.set(index, heap.get(px));
                heap.set(px, newelem);
                bubbleup(px);
            }
        }
    }

    // called after popping an element from heap top and replacing it with the element on the bottom
    private void dribbleDown(int index) {
        T newEntry = heap.get(index);
        T swapValue = heap.get(index);

        int swapIndex = -1;
        int leftChildIndex = getLeftIndex(index);
        if (leftChildIndex < heap.size() && heapFunc.apply(heap.get(leftChildIndex), swapValue)) {
            swapValue = heap.get(leftChildIndex);
            swapIndex = leftChildIndex;
        }

        int rightChildIndex = getRightIndex(index);
        if (rightChildIndex < heap.size() && heapFunc.apply(heap.get(rightChildIndex), swapValue)) {
            swapValue = heap.get(rightChildIndex);
            swapIndex = rightChildIndex;
        }

        if (swapIndex != -1) {
            heap.set(index, swapValue);
            heap.set(swapIndex, newEntry);
            dribbleDown(swapIndex);
        }
    }

    private int getLeftIndex(int parentIndex) {
        return parentIndex * 2 + 1;
    }

    private int getRightIndex(int parentIndex) {
        return parentIndex * 2 + 2;
    }
}