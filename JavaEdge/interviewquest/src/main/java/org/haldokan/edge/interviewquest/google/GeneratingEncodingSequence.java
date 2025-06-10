package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question
 *
 * The Question: 3_STAR
 * 
 * Look at the sequence below:
 * 1, 11, 21, 1211, 111221, 312211, ...
 * Starting at index 2 each element in the sequence describes the previous element. the 21 at index there means that
 * there are two ones at index 2. Element 1211 at index 3 means that there are one two and one one at element 2... and so
 * forth.
 * <p>
 * Write a code that receives n and returns the nth element of the sequence.
 * If it gets 4 as input of the method it should return 1211.
 * <p>
 * Created by haytham.aldokanji on 5/22/16.
 */
public class GeneratingEncodingSequence {

    public static void main(String[] args) {
        GeneratingEncodingSequence driver = new GeneratingEncodingSequence();

        String element = driver.generate(1, new ArrayDeque<>());
        assertThat(element, is("1"));

        element = driver.generate(2, new ArrayDeque<>());
        assertThat(element, is("11"));

        element = driver.generate(3, new ArrayDeque<>());
        assertThat(element, is("21"));

        element = driver.generate(4, new ArrayDeque<>());
        assertThat(element, is("1211"));

        element = driver.generate(5, new ArrayDeque<>());
        assertThat(element, is("111221"));

        element = driver.generate(6, new ArrayDeque<>());
        assertThat(element, is("312211"));

        element = driver.generate(7, new ArrayDeque<>());
        assertThat(element, is("13112221"));
    }

    public String generate(int ordinal, Deque<String> evalQueue) {
        if (ordinal == 1) {
            return "1";
        }
        if (ordinal == 2) {
            return "11";
        }
        // recursively build the sequence up to the given ordinal
        String element = generate(ordinal - 1, evalQueue);

        String[] parts = element.split("");
        Arrays.stream(parts).forEach(evalQueue::add);

        StringBuilder sequenceAtOrdinal = new StringBuilder();
        String lastDigit = null;
        int count = 0;
        while (!evalQueue.isEmpty()) {
            String digit = evalQueue.remove();
            if (lastDigit == null || lastDigit.equals(digit)) {
                lastDigit = digit;
                count++;
            } else {
                sequenceAtOrdinal.append(count).append(lastDigit);
                lastDigit = digit;
                count = 1;
            }
        }
        // residue left in the queue
        sequenceAtOrdinal.append(count).append(lastDigit);
        // return new element in the sequence
        return sequenceAtOrdinal.toString();
    }
}
