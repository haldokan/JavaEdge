package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LRU cache. Basically started off with how would I store values and get them from memory for faster access.
 * So I mentioned HashMap. and then interviewer added more info about deleting least recently used element.
 */
public class DesignLRUCache {
    Map<String, byte[]> cache = new ConcurrentHashMap<>();
    PriorityQueue<Key> lru = new PriorityQueue<>((k1, k2) -> k1.time.isBefore(k2.time) ? 1 : -1);

    static class Key {
        String id;
        LocalDateTime time;
    }

}
