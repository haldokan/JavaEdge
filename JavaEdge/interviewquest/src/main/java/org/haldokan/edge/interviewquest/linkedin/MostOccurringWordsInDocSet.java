package org.haldokan.edge.interviewquest.linkedin;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * My solution to a Linkedin interview question.
 * NOTE: I don't think the intent of this question was to do a concurrent map reduce (like I did here). I think they wanted
 * to see a Map<word, count> and a heap of size 100 that keeps track of the most frequent words
 * The Question: 4_STAR
 * Find the 100 most frequently occurring words in a set of documents. Optimize.
 * <p>
 * I think the key here is that we have a "set of documents" which leads me to think they expect a parallel map-reduce
 * solution. That's what I implemented here concurrent mapping then reducing using java.util.Stream
 *
 * @author haldokan
 */
public class MostOccurringWordsInDocSet {
    private static String beautyDoc = "shape, nice face; shape! arms? legs! cream smooth fit butts hair fit cream arms! legs;";
    private static String fitnessDoc = "shape; abb? legs shape? fit fit arms legs; shape,";
    private ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        MostOccurringWordsInDocSet driver = new MostOccurringWordsInDocSet();
        List<String> docs = Arrays.asList(new String[]{beautyDoc, fitnessDoc});
        System.out.println(Arrays.toString(driver.mostOccuringWords(docs, 3)));
    }

    // assuming the docs are read into strings, reading them from files does not add much to clarify the approach.
    public String[] mostOccuringWords(List<String> docs, int sampleSize) {
        List<Future<Set<Map.Entry<String, Integer>>>> wordCounts = new ArrayList<>();

        for (String doc : docs) {
            wordCounts.add(executor.submit(() -> wordCount(doc)));
        }
        executor.shutdown();
        try {
            return reduce(wordCounts, sampleSize);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed ", e);
        }
    }

    public Set<Map.Entry<String, Integer>> wordCount(String doc) {
        Map<String, Integer> countByWord = new HashMap<>();
        // splitting on non-word chars; not exactly what we want but good enough
        String[] words = doc.split("\\W");
        for (String word : words) {
            // splitting by \\W produces 'space' words so lets ignore them
            if (!word.trim().isEmpty())
                countByWord.compute(word, (k, v) -> v == null ? 1 : v + 1);
        }
        return countByWord.entrySet();
    }

    public String[] reduce(List<Future<Set<Map.Entry<String, Integer>>>> wordCounts, int sampleSize)
            throws InterruptedException, ExecutionException {
        List<Map.Entry<String, Integer>> words = new ArrayList<>();
        for (Future<Set<Map.Entry<String, Integer>>> futureCount : wordCounts) {
            words.addAll(futureCount.get());
        }

        Collector<Map.Entry<String, Integer>, ?, Integer> countCollector = Collectors.summingInt(Map.Entry::getValue);
        List<String> wordsSortedByCount = words.stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, countCollector)).entrySet().stream()
                .sorted((o1, o2) -> -1 * o1.getValue().compareTo(o2.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

        // if list size is less than sampleSize we get an array padded by nulls - going to ignore that for this exercise
        return Arrays.copyOf(wordsSortedByCount.toArray(new String[]{}), sampleSize);
    }
}
