package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * My solution to an Amazon interview question - An array of size 24 * 60 * 60 (seconds in 1 day)
 * can be used to map the order time to an index within the day. We use LongAdder to increase concurrency. I extended
 * the solution to map dates to day of the year with each day serving as an index to the array above. Basically 2
 * dimensional array with day-of-year as the row index and the second of the day as the column index. The solution
 * supports retrieving order volumes b/w any 2 dates (not just times within the same day). Contention b/w threads is limited
 * to updating the LongAdder which is designed specifically to lessen contention (read javadocs for LongAdder).
 * <p>
 * The Question: 5_STAR
 * <p>
 * Write an app that stores total order numbers arrived at different times. For example, at 1.15 pm the app got 15 order, at
 * 1.30 pm, the app got 20 order and so on. Now we need to design the data structures so that we can query the total
 * orders we got in a time range efficiently. For this example, we can query how many orders we got
 * between 1 and 2 pm; answer will be 15+ 20 = 35
 * <p>
 * Created by haytham.aldokanji on 7/22/16.
 */
public class QueryNumOfReceivedOrdersInTimeBracket {
    private static final int SECONDS_IN_DAY = 24 * 60 * 60 + 1;
    private static final int NUM_DAYS = 366;
    // resolution to the second; and extra index to keep track of number of orders for the whole day
    private final LongAdder[][] orderVolumeTracker = new LongAdder[NUM_DAYS][SECONDS_IN_DAY + 1];

    public QueryNumOfReceivedOrdersInTimeBracket() {
        IntStream.range(0, orderVolumeTracker.length).forEach(index -> {
            LongAdder[] day = orderVolumeTracker[index];
            for (int i = 0; i < day.length; i++) {
                day[i] = new LongAdder();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        QueryNumOfReceivedOrdersInTimeBracket driver = new QueryNumOfReceivedOrdersInTimeBracket();
        driver.test1();
    }

    public void onOrder(LocalTime time) {
        int day = LocalDate.now().getDayOfYear() - 1;
        orderVolumeTracker[day][time.toSecondOfDay() - 1].increment();
        // keep track of orders for the whole day in the extra slot/column we added to each row
        orderVolumeTracker[day][SECONDS_IN_DAY].increment();
    }

    public long getNumberOfOrders(LocalDateTime start, LocalDateTime end) {
        int startDay = start.getDayOfYear() - 1;
        int endDay = end.getDayOfYear() - 1;
        int startSecond = start.toLocalTime().toSecondOfDay() - 1;
        int endSecond = end.toLocalTime().toSecondOfDay() - 1;

        long numberOrders = 0;

        if (startDay == endDay) {
            for (int i = startSecond; i < endSecond; i++) {
                numberOrders += orderVolumeTracker[startDay][i].longValue();
            }
        } else {
            for (int i = startSecond; i < SECONDS_IN_DAY; i++) {
                numberOrders += orderVolumeTracker[startDay][i].longValue();
            }
            for (int i = 0; i < endSecond; i++) {
                numberOrders += orderVolumeTracker[endDay][i].longValue();
            }
            for (int i = startDay + 1; i < endDay; i++) {
                // sums for the whole day are stored in the extra row slot so we don't have to do that repeatedly
                numberOrders += orderVolumeTracker[i][SECONDS_IN_DAY].longValue();
            }
        }
        return numberOrders;
    }

    private void test1() throws InterruptedException {
        Random random = new Random();
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);

        for (int i = 0; i < 500; i++) {
            onOrder(LocalTime.now());
            Thread.sleep(random.nextInt(100) + 1);
        }
        Arrays.stream(orderVolumeTracker).forEach(day -> {
            if (Arrays.stream(day).filter(v -> v.longValue() != 0).toArray().length > 0) {
                System.out.printf("%s%n", Arrays.toString(Arrays.stream(day)
                        .filter(v -> v.longValue() != 0)
                        .toArray()));
            }
        });
        LocalDateTime endTime = startTime.plusDays(1).plusSeconds(4);

        long numberOfOrders = getNumberOfOrders(startTime, endTime);
        System.out.printf("numberOfOrders: %s%n", numberOfOrders);
    }
}
