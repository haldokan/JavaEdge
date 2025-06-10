package org.haldokan.edge.executorservice2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExecutorService2Test {
    private static Random rand = new Random();
    private static List<String> states = Arrays.asList(new String[]{"NewYork", "NewJersey", "Maine", "Pennsylvania",
            "Florida", "NewHampshire", "Connecticut", "California", "Texas", "Washignton"});

    private static Callable<String> getCallable(String s, int delay) {
        return () -> {
            Thread.sleep(delay);
            return reverse(s);
        };
    }

    private static String reverse(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    @Test
    public void testSubmit() throws Exception {
        int poolSize = 3;
        FixedThreadPoolExecutorService<String> es = new FixedThreadPoolExecutorService<>(poolSize);

        List<Future<String>> futureResults = new ArrayList<>();

        for (String state : states) {
            futureResults.add(es.submit(getCallable(state, 100 + rand.nextInt(50))));
        }
        for (int i = 0; i < states.size(); i++) {
            String state = states.get(i);
            String reverse = futureResults.get(i).get();
            System.out.println(state + " <> " + reverse);
            assertThat(reverse, is(reverse(states.get(i))));
        }
    }

    // TODO this test hangs sometimes before all input is processed
    @Test
    public void testSubmitWithLargeNumberOfThreads() throws Exception {
        int inputSize = 700;
        int poolSize = 10;

        FixedThreadPoolExecutorService<String> es = new FixedThreadPoolExecutorService<>(poolSize);

        List<String> input = new ArrayList<>();
        for (int i = 0; i < inputSize; i++) {
            input.add(states.get(rand.nextInt(states.size())) + rand.nextInt(5000));
        }

        List<Future<String>> futureResults = new ArrayList<>();

        for (String state : input) {
            futureResults.add(es.submit(getCallable(state, 10 + rand.nextInt(10))));
        }
        for (int i = 0; i < input.size(); i++) {
            String state = input.get(i);
            String reverse = futureResults.get(i).get();
            System.out.println(i + ". " + state + " <> " + reverse);
            assertThat(reverse, is(reverse(input.get(i))));
        }
    }

}