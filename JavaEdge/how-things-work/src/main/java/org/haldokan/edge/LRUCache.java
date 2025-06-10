package org.haldokan.edge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * My implementation of an LRU cache - R/W locked Map and doubly linked list. The implementation is thread safe since
 * I maintain the doubly linked list inside Map.compute which is synchronized by R/W locks
 * <p>
 * In fact LRU caches (like the Guava cache) are not usually implemented this way. Instead of eagerly removing the aged map
 * elements at inserts that exceed the map's configured capacity, a background low priority thread scans the map looking for
 * the most aged elements and removes them.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 9/14/16.
 */
public class LRUCache<K, V> {
    private final Map<K, Node<K, V>> cacheMap;
    private final int maxSize;
    private Node<K, V> cacheListHead;
    private Node<K, V> cacheListTail;
    private int currentSize;


    public LRUCache(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Invalid max cache size: " + maxSize);
        }
        this.maxSize = maxSize;
        cacheMap = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        testPut1();
        System.out.printf("----------------%n");
        testPut2();
        System.out.printf("----------------%n");
        testRemoveFromCacheList();
    }

    private static void testPut1() {
        int maxSize = 5;
        LRUCache<String, Integer> cache = new LRUCache<>(maxSize);

        for (int i = 0; i < maxSize * 2; i++) {
            String key = "k" + i;
            cache.put(key, i);
            cache.printCacheList();

            assertThat(cache.cacheListHead.value, is(i));
            assertThat(cache.get(key), notNullValue());
            assertThat(cache.findInCacheList(key), notNullValue());

            if (i >= maxSize) {
                String key1 = "k" + (i - maxSize);
                assertThat(cache.get(key1), nullValue());
                assertThat(cache.findInCacheList(key1), nullValue());
            }
        }
    }

    private static void testPut2() {
        int maxSize = 5;
        LRUCache<String, Integer> cache = new LRUCache<>(maxSize);

        for (int i = 0; i < maxSize * 2; i++) {
            String key = "k" + (i % maxSize);
            cache.put(key, i);
            cache.printCacheList();

            assertThat(cache.cacheListHead.value, is(i));
            assertThat(cache.findInCacheList(key).value, is(i));
        }
    }

    private static void testRemoveFromCacheList() {
        int maxSize = 5;
        LRUCache<String, Integer> cache = new LRUCache<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            cache.put("k" + i, i);
            cache.printCacheList();
        }

        List<String> keys = new ArrayList<>(cache.cacheMap.keySet());
        Collections.shuffle(keys);

        for (String key : keys) {
            assertThat(cache.findInCacheList(key), notNullValue());
            cache.remove(key);
            cache.printCacheList();
            assertThat(cache.findInCacheList(key), nullValue());
        }
    }

    public V put(K key, V val) {
        Node<K, V> oldValue = cacheMap.compute(key, (k, v) -> {
            if (v == null) {
                currentSize++;
                addNodeAtHead(new Node<>(key, val));

                if (currentSize > maxSize) {
                    remove(cacheListTail.key);
                    currentSize--;
                }
            } else {
                v.value = val;
                moveNodeToHead(v);
            }
            return cacheListHead;
        });
        return oldValue.value;
    }

    public V get(K key) {
        Node<K, V> node = cacheMap.get(key);
        return node != null ? node.value : null;
    }

    public V remove(K key) {
        Node<K, V> node = cacheMap.remove(key);
        if (node != null) {
            removeNode(node);
            return node.value;
        }
        return null;
    }

    private void addNodeAtHead(Node<K, V> node) {
        if (cacheListHead == null || cacheListTail == null) {
            cacheListHead = cacheListTail = node;
        } else {
            node.next = cacheListHead;
            cacheListHead.prev = node;
            cacheListHead = node;
        }
    }

    private void moveNodeToHead(Node<K, V> node) {
        if (node.prev != null) {
            removeNode(node);
            addNodeAtHead(node);
        }
    }

    private void removeNode(Node<K, V> node) {
        Node<K, V> prev = node.prev;
        Node<K, V> next = node.next;

        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }

        if (node.key.equals(cacheListTail.key)) {
            cacheListTail = prev;
        }
        if (node.key.equals(cacheListHead.key)) {
            cacheListHead = next;
        }
        node.prev = node.next = null;
    }

    private Node<K, V> findInCacheList(K key) {
        Node<K, V> cursor = cacheListHead;
        while (cursor != null) {
            if (cursor.key.equals(key)) {
                return cursor;
            }
            cursor = cursor.next;
        }
        return null;
    }

    private void printCacheList() {
        Node<K, V> cursor = cacheListHead;
        while (cursor != null) {
            System.out.printf("%s, ", cursor);
            cursor = cursor.next;
        }
        System.out.printf("%n");
    }

    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next, prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "->" + value;
        }
    }
}
