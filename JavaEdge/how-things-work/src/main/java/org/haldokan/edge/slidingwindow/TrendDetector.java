package org.haldokan.edge.slidingwindow;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Think of detecting trending hash tags on twitter. Here we present the concept using SlidingTimeWindow.
 *
 * @author haldokan
 */
public class TrendDetector {
    private PriorityHeap<TagRank> trendQueue;
    private ConcurrentMap<String, Integer> updatesPerTag;

    public TrendDetector() {
        this.trendQueue = new PriorityHeap<>((TagRank o1, TagRank o2) ->
                Integer.valueOf(o2.getRank()).compareTo(Integer.valueOf(o1.getRank())));
        this.updatesPerTag = new ConcurrentHashMap<>();
    }

    // simply throw the InterruptedException... must be a better option
    public void notify(TagRank tagRank) {
        updatesPerTag.merge(tagRank.getTag(), tagRank.getRank(), (k, v) -> v + tagRank.getRank());
        for (; ; ) {
            try {
                trendQueue.remove(tagRank);
                trendQueue.put(tagRank);
                break;
            } catch (InterruptedException e) {
                // go back and try to put it on the queue
                e.printStackTrace();
            }
        }
    }

    public TagRank getTrendingTag() {
        return trendQueue.peek();
    }

    public void dumpHeap() {
        System.out.println(trendQueue);
    }

    private static class PriorityHeap<E> {
        private BlockingQueue<E> heap;

        public PriorityHeap(Comparator<E> comparator) {
            this.heap = new PriorityBlockingQueue<>(1024, comparator);
        }

        public void put(E e) throws InterruptedException {
            heap.put(e);
        }

        public void remove(E e) {
            heap.remove(e);
        }

        public E peek() {
            return heap.peek();
        }

        @Override
        public String toString() {
            return "PriorityHeap [heap=" + heap + "]";
        }
    }
}
