package org.haldokan.edge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My Implementation of all the heap data structure operations: pop, push, dribbleDown and bubbleUp. Data stored in the heap
 * is made generic so it can be anything. I use a Java-8 BiFunction to designate a created heap as max or min heap.
 * We can be more specific and make the BiFunction a Comparator but I find the -1, 0, +1 return values of the Comparator
 * to be less expressive than implementing less/greater than in the BiFunction.
 * <p>
 * In the JDK PriorityQueue is implemented using the heap data structure
 * <p>
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
        jdkHeap();
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

        assertThat(heap.peek(), is(1492));

        Integer val;
        List<Integer> result = new ArrayList<>();
        do {
            val = heap.pop();
            if (val != null) {
                result.add(val);
            }
        } while (val != null);
        System.out.printf("%s%n", result);

        assertThat(result.toArray(new Integer[0]), is(result.stream().sorted().toArray(Integer[]::new)));
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

        assertThat(heap.peek(), is(2001));

        Integer val;
        List<Integer> result = new ArrayList<>();
        do {
            val = heap.pop();
            if (val != null) {
                result.add(val);
            }
        } while (val != null);
        System.out.printf("%s%n", result);

        assertThat(result.toArray(new Integer[0]),
                is(result.stream().sorted((v1, v2) -> v2 - v1).toArray(Integer[]::new)));
    }

    // heap maintains weak order: elements are not sorted but we can always get
    // the min (or max) in log(n)
    private static void jdkHeap() {
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
        bubbleUp(heap.size() - 1);
    }

    public T peek() {
        return heap.get(0);
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
    private void bubbleUp(int index) {
        if (index == 0) {
            return;
        }
        T value = heap.get(index);
        int parentIndex = getParentIndex(index);

        if (parentIndex >= 0) {
            if (heapFunc.apply(value, heap.get(parentIndex))) {
                heap.set(index, heap.get(parentIndex));
                heap.set(parentIndex, value);

                bubbleUp(parentIndex);
            }
        }
    }

    // called after popping an element from the top of the heap and replacing it with the element on the bottom
    private void dribbleDown(int index) {
        T currentValue = heap.get(index);
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
        // if swap index is -1 it means that the currentValue has found its proper place and the heap property is established
        if (swapIndex != -1) {
            heap.set(index, swapValue);
            heap.set(swapIndex, currentValue);
            dribbleDown(swapIndex);
        }
    }

    private int getLeftIndex(int parentIndex) {
        return parentIndex * 2 + 1;
    }

    private int getRightIndex(int parentIndex) {
        return parentIndex * 2 + 2;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }
}