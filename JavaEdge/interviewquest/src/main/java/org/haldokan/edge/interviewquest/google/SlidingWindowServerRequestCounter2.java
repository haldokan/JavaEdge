package org.haldokan.edge.interviewquest.google;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * A sketch of another approach to a Google interview question that is probably simpler than the one I presented in
 * SlidingWindowServerRequestCounter. No need to have 2 schedulers ticking every second. The same scheduler advance both
 * start and end indexes. Also no need to index a request by mod-ing its time to the array length; it rather can be
 * indexed useing the current end index.
 * <p>
 * The Question: 5_STAR
 * <p>
 * Given a server that has requests coming in. Design a data structure such that you can fetch the count of the
 * requests in the last second, minute and hour.
 * <p>
 * Created by haytham.aldokanji on 6/30/16.
 */
public class SlidingWindowServerRequestCounter2 {
    int[] window = new int[60 * 60];
    int startNdx = 0;
    int endNdx = window.length - 1;

    void tick() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            endNdx = (endNdx + 1) % window.length;
            window[startNdx] = 0;
            startNdx = (startNdx + 1) % window.length;
        }, 0, 1, TimeUnit.SECONDS);
    }

    void request(String req) {
        window[endNdx]++;
    }

    int numRequests(int secs) {
        int count = 0;
        int diff = endNdx - secs;
        if (diff >= 0) {
            for (int i = 0; i <= endNdx; i++) {
                count += window[i];
            }
        }
        if (diff < 0) {
            for (int i = window.length - 1; i > window.length - diff; i--) {
                count += window[i];
            }
        }
        return count;
    }
}
