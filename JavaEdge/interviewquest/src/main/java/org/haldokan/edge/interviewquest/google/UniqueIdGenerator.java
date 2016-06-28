package org.haldokan.edge.interviewquest.google;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * My solution to a Google interview question - it generates unique ids among multiple instances on the same machine or
 * several machines.
 * The Question: 3_STAR
 * <p>
 * design a system to return an unique ID for each request. For most of requests, the ID value should increase
 * as time goes, the system should handle 1000 requests per second at least.
 * timestamps alone is not valid since there might be multiple requests with same timestamps.
 * <p>
 * Created by haytham.aldokanji on 6/28/16.
 */
public class UniqueIdGenerator {
    private final AtomicLong[] idRanges;
    private final String idPrefix;

    public UniqueIdGenerator(int averageRequestRate) throws Exception {
        String ipAddress = Inet4Address.getLocalHost().getHostAddress();
        idPrefix = ipAddress + "-" + getPid() + "-" + System.currentTimeMillis();

        idRanges = new AtomicLong[averageRequestRate];
        Arrays.fill(idRanges, new AtomicLong(0));
    }

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        int numThreads = 1024;
        UniqueIdGenerator generator = new UniqueIdGenerator(numThreads);
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

        int numRequests = numThreads * 10;
        Future<String>[] ids = new Future[numRequests];
        for (int i = 0; i < numRequests; i++) {
            ids[i] = threadPool.submit(generator::generateId);
        }

        for (Future<String> id : ids) {
            System.out.printf("%s%n", id.get());
        }
        threadPool.shutdown();
    }

    public String generateId() {
        int rangeIndex = (int) (System.nanoTime() % idRanges.length);
        long counter = idRanges[rangeIndex].getAndIncrement();
        return idPrefix + "-" + rangeIndex + "-" + counter;
    }

    // mocked: could be read from a file or obtained via jmx
    private String getPid() {
        return String.valueOf(new Random().nextInt(1024));
    }
}
