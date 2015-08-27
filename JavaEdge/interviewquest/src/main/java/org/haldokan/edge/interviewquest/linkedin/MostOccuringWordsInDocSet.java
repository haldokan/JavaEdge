package org.haldokan.edge.interviewquest.linkedin;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * My solution to a Linkedin interview question.
 * <p>
 * Find the 100 most frequently occuring words in a set of documents. Optimize.
 * <p>
 * I think the key here is that we have a "set of documents" which leads me to think they expect a parallel map-reduce
 * solution. That's what I implemented here concurrent mapping then reducing using java.util.Stream
 *
 * @author haldokan
 */
public class MostOccuringWordsInDocSet {
    private static String beautyDoc = "shape, nice face; shape! arms? legs! cream smooth fit butts hair fit cream arms! legs;";
    private static String fitnessDoc = "shape; abb? legs shape? fit fit arms legs; shape,";
    private ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        MostOccuringWordsInDocSet driver = new MostOccuringWordsInDocSet();
        List<String> docs = Arrays.asList(new String[]{beautyDoc, fitnessDoc});
        System.out.println(Arrays.toString(driver.mostOccuringWords(docs, 3)));
    }

    // assuming the docs are read into strings, reading them from files does not add much to clarify the approach.
    public String[] mostOccuringWords(List<String> docs, int sampleSize) {
        List<Future<Set<Map.Entry<String, Integer>>>> wordCounts = new ArrayList<>();

        for (String doc : docs) {
            wordCounts.add(executor.submit(new Callable<Set<Map.Entry<String, Integer>>>() {
                @Override
                public Set<Map.Entry<String, Integer>> call() throws Exception {
                    return wordCount(doc);
                }
            }));
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
            for (Map.Entry<String, Integer> wordCount : futureCount.get()) {
                words.add(wordCount);
            }
        }

        Collector<Map.Entry<String, Integer>, ?, Integer> countCollector = Collectors.summingInt(Map.Entry::getValue);
        List<String> wordsSortedByCount = words.stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, countCollector)).entrySet().stream()
                .sorted(new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                        return -1 * o1.getValue().compareTo(o2.getValue());
                    }
                }).map(Map.Entry::getKey).collect(Collectors.toList());

        // if list size is less than sampleSize we get an array padded by nulls - going to ignore that for this exercise
        return Arrays.copyOf(wordsSortedByCount.toArray(new String[]{}), sampleSize);
    }
}
