package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * My solution to an Amazon interview question
 *
 * The Question: 3.5-STAR
 *
 * LRU cache. Basically started off with how would I store values and get them from memory for faster access.
 * So I mentioned HashMap. and then interviewer added more info about deleting least recently used element.
 */
public class DesignLRUCache {
    Map<String, byte[]> cache = new ConcurrentHashMap<>();
    PriorityQueue<Key> lru = new PriorityQueue<>((k1, k2) -> k1.time.isBefore(k2.time) ? 1 : -1); // min-heap on time

    int maxSize;
    BlockingQueue<Key>[] stagingQueues;

    public DesignLRUCache(int maxSize, int parallelLevel) {
        this.maxSize = maxSize;
        this.stagingQueues = new ArrayBlockingQueue[parallelLevel]; // example of where Java sucks since array are always different (no easy way to use generics here)

    }

    byte[] put(String id, byte[] val) {
        byte[] prevVal = cache.put(id, val);

        Key key = new Key(id);
        stagingQueues[id.hashCode() % stagingQueues.length].offer(key); // reduce contention by using a number of blocking queues
        return prevVal;
    }

    // uncommon operation to be initiated by users generally
    void remove(String id) {
        cache.remove(id);
        // alternatively we can let the key age in the lru heap so and removed later by the cache maintainer thread
        lru.remove(new Key(id)); // note that Key equality is done on the id only so it can be found in the lru heap (this is O(n) operation).
    }

    // move keys from blocking queues to lru min-heap - need to run on a different thread
    void addToLru() {
        for (; ; ) {
            for (BlockingQueue<Key> queue : stagingQueues) {
                try {
                    Key key = queue.poll();
                    if (key != null) {
                        lru.add(key);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // we should handle exceptions differently
                }
            }
        }
    }

    // runs periodically on a (lower priority) thread and remove the least recently accessed entries from the cache
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(id, key.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
