package org.haldokan.edge.interviewquest.microsoft;

import java.util.HashMap;
import java.util.Map;

/**
 * My solution to a Microsoft interview question
 *
 * The Question: 3.5-STAR
 *
 * Use synchronized, wait() and notify() to write a program such that below mentioned conditions are fulfilled while
 * reading and writing data to a shared resource.
 *
 * When a write is happening, no read or other write should be allowed(read and write threads should wait)
 *
 * When a read is happening, no write should be allowed (write threads should wait) but. other read threads should be
 * able to read.
 *
 * Do not use any API classes e.g. ReadWriteLock, AtomicInteger etc..
 */
public class ImplementReadWriteLock {
    private static final Object MUTEX =  new Object();
    private final Map<String, Object> resource = new HashMap<>();
    private volatile int readThreadCount = 0;
    private volatile boolean threadWriting = false;

    public void update(String key, Object update) throws InterruptedException {
        if (readThreadCount > 0 || !threadWriting) {
            synchronized (MUTEX) {
                MUTEX.wait();
            }
        } else {
            synchronized (MUTEX) {
                threadWriting = true;
                resource.put(key, update);
                threadWriting = false;
                MUTEX.notifyAll();
            }
        }
    }

    public Object access(String key) throws InterruptedException {
        Object value = null;
        if (threadWriting) {
            synchronized (MUTEX) {
                MUTEX.wait();
            }
        }
        else {
            synchronized (MUTEX) {
                readThreadCount++;
            }
            value =  resource.get(key);
            synchronized (MUTEX) {
                readThreadCount--;
            }
        }
        return value;
    }
}
