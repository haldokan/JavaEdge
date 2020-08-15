package org.haldokan.edge;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * My basic implementation of a Token-Bucket-Rate-Limiter. Note that AtomicInteger methods are synchronized which can pause
 * problems for very high level of request rates. How to improve the implementation? In general how rate limiting is designed
 * in distributed systems with millions of requests per second?
 * todo: update with answers to the 2 questions above
 */
public class TokenBucketRateLimiter {
    private final AtomicInteger tokenBucket;
    private final int bucketMaxSize;

    public TokenBucketRateLimiter(int bucketMaxSize, int timeWindowSecs) {
        tokenBucket = new AtomicInteger(bucketMaxSize);
        this.bucketMaxSize = bucketMaxSize;
        bucketMaintainer(timeWindowSecs);
    }
    // client classes allow or drop the request based on the returned value
    boolean hasToken() {
        return tokenBucket.getAndDecrement() > 0;
    }

    private void bucketMaintainer(int timeWindowSecs) {
        ScheduledExecutorService maintainer = Executors.newSingleThreadScheduledExecutor();
        maintainer.schedule(() -> {
            tokenBucket.set(bucketMaxSize);
        }, timeWindowSecs, TimeUnit.SECONDS);
    }
}
