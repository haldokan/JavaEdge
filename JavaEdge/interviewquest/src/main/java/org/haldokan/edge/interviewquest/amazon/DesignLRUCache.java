package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * LRU cache. Basically started off with how would I store values and get them from memory for faster access.
 * So I mentioned HashMap. and then interviewer added more info about deleting least recently used element.
 */
public class DesignLRUCache {
    Map<String, byte[]> cache = new ConcurrentHashMap<>();
    PriorityQueue<Key> lru = new PriorityQueue<>((k1, k2) -> k1.time.isBefore(k2.time) ? 1 : -1); // min-heap on time

    int maxSize;
    int parallelLevel;


    public DesignLRUCache(int maxSize, int parallelLevel) {
        this.maxSize = maxSize;
        this.parallelLevel = parallelLevel;
    }

    byte[] put(String key, byte[] val) {
        byte[] prevVal = cache.put(key, val);
        lru.add(new Key(key)); // can be made to be deposited first on a number of blocking queues to lessen contention and then added to the lru min-heap.
        return prevVal;
    }

    // runs periodically on a lower priority thread
    void maintainCache() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            while (lru.size() > maxSize) {
                Key leastRecentlyUpdated = lru.remove();
                cache.remove(leastRecentlyUpdated.id);
            }
        }, 10, TimeUnit.SECONDS);
    }

    static class Key {
        String id;
        LocalDateTime time;

        public Key(String id) {
            this.id = id;
            this.time = LocalDateTime.now();
        }
    }
}
