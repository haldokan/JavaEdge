package org.haldokan.edge.interviewquest.google;

/**
 * My boss's solution to a Google interview question which is more elegant than mine (AggregatingDataFromLargeLogFiles)!
 * <p>
 * <p>
 * You are given large numbers of logs, each one of which contains a start time (long), end time (long) and memory usage (int).
 * The time is recorded as MMDDHH (100317 means October 3rd 5PM) Write an algorithm that returns a specific time (hour)
 * when the memory usage is the highest. If there are multiple answers, return the first hour.
 * <p>
 * e.g.
 * 100207 100208 2
 * 100305 100307 5
 * 100207 100209 4
 * 100209 100211 5
 * 100208 100210 3
 * 111515 121212 1
 * Answer: 100209 8
 * <p>
 * (Need to consider different scenarios like the time slots could be very sparse)
 * <p>
 * Created by haytham.aldokanji on 10/7/15.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;

import static java.nio.file.Files.list;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

public class AggregatingDataFromLargeLogFiles2 {

    public static <T, R> Function<T, R> uncheck(UncheckedFunction<T, R> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static void main(String[] args) throws IOException {
        List<LogRecord> logRecords = list(get("/Users/haytham.aldokanji/misc/logs"))
                .flatMap(uncheck(Files::lines))
                .map(LogRecord::new)
                .collect(toList());

        NavigableMap<Long, Integer> loadsByEvent = new TreeMap<>();
        logRecords.forEach(lr -> {
            loadsByEvent.put(lr.startTime, 0);
            loadsByEvent.put(lr.endTime, 0);
        });
        logRecords.stream().forEach(lr -> {
            loadsByEvent.subMap(lr.getStartTime(), true, lr.getEndTime(), false).replaceAll((k, v) -> v + lr.load);
        });
        System.out.println(loadsByEvent);
    }

    @FunctionalInterface
    public static interface UncheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }


//    public static void mainPureFunctional(String[] args) throws IOException {
//        List<LogRecord> logRecords = list(get("C:\\logs")).flatMap(uncheck(Files::lines)).map(LogRecord::new).collect(toList());
//        Set<Long> startEvents = logRecords.parallelStream().map(LogRecord::getStartTime).collect(toSet());
//        Set<Long> endEvents = logRecords.parallelStream().map(LogRecord::getEndTime).collect(toSet());
//        NavigableMap<Long, Integer> loadsByEvent = new TreeMap<>(Stream.concat(startEvents.stream(), endEvents.stream()).collect(toMap(identity(), e -> Integer.valueOf(0))));
//        logRecords.stream().forEach( lr -> {
//            loadsByEvent.subMap(lr.getStartTime(), true, lr.getEndTime(), true).replaceAll( (k, v) -> v + lr.load);
//        });
//    }

    public static class LogRecord {
        Long startTime;
        Long endTime;
        Integer load;

        LogRecord(String r) {
            System.out.println(r);
            String[] record = r.trim().split(" ");
            this.startTime = Long.valueOf(record[0]);
            this.endTime = Long.valueOf(record[1]);
            this.load = Integer.valueOf(record[2]);
        }

        public Long getStartTime() {
            return startTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            return "LogRecord{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", load=" + load +
                    '}';
        }
    }
}

//Sample input and output
/*
100207 100208 2
100305 100307 5
100207 100209 4
100209 100211 5
100208 100210 3
111515 121212 1
{100207=6, 100208=7, 100209=8, 100210=5, 100211=0, 100305=5, 100307=0, 111515=1, 121212=0}

* */