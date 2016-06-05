package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * My solution to a Google interview question
 * <p>
 * Implement the interface specified below:
 * <p>
 * interface RateLimit {
 * Sets the rate, from 1 to 1000_000 queries per index
 * void setQPS(int qps);
 * <p>
 * accept or reject a request, called when request is received
 * boolean allowThisRequest();
 * }
 * <p>
 * brief example:
 * server instantiates your object, calls setQPS(1)
 * at at time t, user1 makes a request, allowThisRequest() returns true
 * at time t+0.01 sec, user2 makes a request, allowThisRequest() returns false
 * at at time t+1, user4 makes a request, allowThisRequest() returns true
 * at time t+5 sec, user3 makes a request, allowThisRequest() returns true
 * <p>
 * Created by haytham.aldokanji on 6/4/16.
 */
public class ServerWithAllowableRequestRate {
    private final int[] requestCount = new int[2];
    private int qps;
    private volatile byte index;

    public static void main(String[] args) throws InterruptedException {
        ServerWithAllowableRequestRate server = new ServerWithAllowableRequestRate();

        server.setQPS(100_000);
        for (int i = 0; i < 1000_000; i++) {
            System.out.printf("%s%n", server.request(i));
            System.out.printf("%s%n", Arrays.toString(server.requestCount));
            Thread.sleep(1);
        }
    }

    public void setQPS(int qps) {
        this.qps = qps;
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            requestCount[index] = 0;
            index = (byte) ((index + 1) % 2);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public String request(int request) {
        if (allowThisRequest()) {
            // normally you add to a processing queue
            return "response" + request;
        }
        return "denied" + request;
    }

    private boolean allowThisRequest() {
        if (requestCount[index] < qps) {
            requestCount[index]++;
            return true;
        }
        return false;
    }
}
