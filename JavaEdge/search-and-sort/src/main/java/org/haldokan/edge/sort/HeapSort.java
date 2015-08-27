package org.haldokan.edge.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Implementation of all heap operations: pop, push, heapify and bubbleup. In the JDK PriorityQueue is implemented using
 * the heap data structure
 *
 * @author haldokan
 */
public class HeapSort {
    private List<Integer> heap = new ArrayList<>();

    public static void main(String[] args) {
        // jdkheap();
        HeapSort h = new HeapSort();
        h.push(1492);
        h.push(1783);
        h.push(1776);
        h.push(1804);
        h.push(1865);
        h.push(1945);
        h.push(1963);
        h.push(1918);
        h.push(2001);
        h.push(1941);

        Integer e = h.pop();
        while (e != null) {
            System.out.println(e);
            e = h.pop();
        }
    }

    // heap maintains weak order: elements are not sorted but we can alway get
    // the min (or max) element in O(1)
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

    public void push(Integer e) {
        heap.add(e);
        bubbleup(heap.size() - 1);
    }

    private void bubbleup(int x) {
        if (x == 0)
            return;
        Integer newelem = heap.get(x);
        int px = (x - 1) / 2;
        if (px >= 0) {
            if (newelem < heap.get(px)) {
                heap.set(x, heap.get(px));
                heap.set(px, newelem);
                bubbleup(px);
            }
        }
    }

    public Integer pop() {
        if (heap.isEmpty())
            return null;
        if (heap.size() == 1)
            return heap.remove(0);

        Integer top = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));

        if (heap.get(0) > top)
            heapify(heap.get(0), 0, 1, 2);
        return top;
    }

    // note I could have (should have) passed only the px (similar to push).
    private void heapify(Integer elem, int px, int lx, int rx) {
        // System.out.println("b4heap: " + "/" + elem + "/" + px + "/" + lx +
        // "/" + rx);
        Integer min = elem;
        int x = -1;
        if (lx < heap.size()) {
            if (heap.get(lx) < min) {
                min = heap.get(lx);
                x = lx;
            }
        }
        if (rx < heap.size()) {
            if (heap.get(rx) < min) {
                min = heap.get(rx);
                x = rx;
            }
        }
        if (x != -1) {
            heap.set(px, min);
            heap.set(x, elem);
            // System.out.println("afheap: " + "/" + min + "/" + x);
            heapify(elem, x, x * 2 + 1, x * 2 + 2);
        }
    }
}