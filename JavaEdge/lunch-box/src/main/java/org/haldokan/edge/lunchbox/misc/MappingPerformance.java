package org.haldokan.edge.lunchbox.misc;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MappingPerformance {
    private static Random rand = new Random();

    @Test
    public void testPerformance_MapComputeApi() {
        List<Quote> l = new ArrayList<>();
        int sampleSize = 10_000_000;
        for (int i = 0; i < sampleSize; i++) {
            l.add(new Quote("q" + rand.nextInt(100000), rand.nextInt(sampleSize)));
        }
        // Map new API
        Stopwatch watch = Stopwatch.createStarted();
        Map<String, Quote> mapComputeApi = new HashMap<>();
        for (Quote q : l) {
            mapComputeApi.compute(q.getSymbol(), (k, v) -> v == null ? q : q.getTime() > v.getTime() ? q : v);
        }
        // use map so compiler does not optimize away!
        System.out.println(mapComputeApi.size());
        System.out.println("map compute api: " + watch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPerformance_MapReduce() {
        List<Quote> l = new ArrayList<>();
        int sampleSize = 10_000_000;
        for (int i = 0; i < sampleSize; i++) {
            l.add(new Quote("q" + rand.nextInt(100000), rand.nextInt(sampleSize)));
        }
        // MapReduce
        Stopwatch watch = Stopwatch.createStarted();
        Map<String, Optional<Quote>> mapReduce = l.stream().collect(
                Collectors.groupingByConcurrent(Quote::getSymbol, Collectors.maxBy(new Comparator<Quote>() {
                    @Override
                    public int compare(Quote o1, Quote o2) {
                        return o1.getTime() > o2.getTime() ? 1 : -1;
                    }
                })));
        // use map so compiler does not optimize away!
        System.out.println(mapReduce.size());
        System.out.println("map reduce time: " + watch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPerformance_Manual() {
        List<Quote> l = new ArrayList<>();
        int sampleSize = 10_000_000;
        for (int i = 0; i < sampleSize; i++) {
            l.add(new Quote("q" + rand.nextInt(100000), rand.nextInt(sampleSize)));
        }
        // Manual
        Stopwatch watch = Stopwatch.createStarted();
        Map<String, Quote> manual = new HashMap<>();
        Quote oldQuote = null;
        for (Quote newQuote : l) {
            oldQuote = manual.get(newQuote.getSymbol());
            if (oldQuote == null || newQuote.getTime() > oldQuote.getTime())
                manual.put(newQuote.getSymbol(), newQuote);
        }

        // use map so compiler does not optimize away!
        System.out.println(manual.size());
        System.out.println("manual time: " + watch.elapsed(TimeUnit.MILLISECONDS));
    }
}
