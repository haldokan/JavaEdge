package org.haldokan.edge.interviewquest.google;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - I assumed that we can start at the first letter of the word
 * Solving for a general case of any set of letters with a board of any dimensions can be based on the solution
 * presented here w/o too much effort
 *
 * The Question: 4-STAR
 *
 * Given the English alphabet, 'a' through 'z' (lowercase), and an imaginary onscreen keyboard with the letters laid out
 * in 6 rows and 5 columns:
 * a b c d e
 * f g h i j
 * k l m n o
 * p q r s t
 * u v w x y
 * z
 * Using a remote control - (up - 'u', down 'd', left 'l', right 'r' and enter '!'), write a function that given a word
 * will produce the sequence of key presses required to type out the word on the onscreen keyboard. The function should
 * return the sequence string.
 *
 * 10/25/22
 */
public class MovesSequenceForWordEntryInLetterMatrix {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int COLS = 5;

    public List<Character> keyClicks(String word) {
        List<Character> clicks = new ArrayList<>();
        char currentLetter = word.charAt(0);
        clicks.add('!');

        for (int i = 1; i < word.length(); i++) {
            char targetLetter = word.charAt(i);
            clicks.addAll(keyClicks(currentLetter, targetLetter));
            currentLetter = targetLetter;
        }
        return clicks;
    }

    private List<Character> keyClicks(char currentLetter, char targetLetter) {
        if (currentLetter == targetLetter) {
            return Collections.singletonList('!');
        }

        int[] currentPos = calculateLetterPosition(currentLetter);
        int[] targetPos = calculateLetterPosition(targetLetter);

        int verticalMoves = targetPos[0] - currentPos[0];
        List<Character> verticalClicks = keyClicks(verticalMoves, new char[]{'u', 'd'});

        int horizontalMoves = targetPos[1] - currentPos[1];
        List<Character> horizontalClicks = keyClicks(horizontalMoves, new char[]{'l', 'r'});

        // going to stick to the question and skirt handling the general case where many values can be on the bottom row
        // handling the general case is easy: if currentLetter col is greater than the col of the last letter go back
        // horizontal to the column of the targetLetter then down vertical
        List<Character> clicks;
        if (targetLetter == 'z') {
            horizontalClicks.addAll(verticalClicks);
            clicks = horizontalClicks;
        } else {
            verticalClicks.addAll(horizontalClicks);
            clicks = verticalClicks;
        }
        clicks.add('!');
        return clicks;
    }

    List<Character> keyClicks(int numOfMove, char[] directions) {
        List<Character> clicks = new ArrayList<>();
        for (int i = 0; i < Math.abs(numOfMove); i++) {
            clicks.add(numOfMove < 0 ? directions[0] : directions[1]);
        }
        return clicks;
    }

    private int[] calculateLetterPosition(char letter) {
        int letterOrder = ALPHABET.indexOf(letter);
        int letterRow = letterOrder / COLS;
        int letterCol = letterOrder % COLS;

        return new int[]{letterRow, letterCol};
    }

    @Test
    public void letterPosition() {
        assertThat(calculateLetterPosition('a'), is(new int[]{0, 0}));
        assertThat(calculateLetterPosition('c'), is(new int[]{0, 2}));
        assertThat(calculateLetterPosition('e'), is(new int[]{0, 4}));
        assertThat(calculateLetterPosition('f'), is(new int[]{1, 0}));
        assertThat(calculateLetterPosition('j'), is(new int[]{1, 4}));
        assertThat(calculateLetterPosition('u'), is(new int[]{4, 0}));
        assertThat(calculateLetterPosition('y'), is(new int[]{4, 4}));
        assertThat(calculateLetterPosition('z'), is(new int[]{5, 0}));
    }

    @Test
    public void clicksBetween2Letters() {
        List<Character> clicks = keyClicks('a', 'e');
        System.out.println(clicks);
        assertThat(clicks, hasSize(5));
        assertThat(clicks, contains('r', 'r', 'r', 'r', '!'));

        clicks = keyClicks('e', 'a');
        System.out.println(clicks);
        assertThat(clicks, hasSize(5));
        assertThat(clicks, contains('l', 'l', 'l', 'l', '!'));

        clicks = keyClicks('a', 'z');
        System.out.println(clicks);
        assertThat(clicks, hasSize(6));
        assertThat(clicks, contains('d', 'd', 'd', 'd', 'd', '!'));

        clicks = keyClicks('z', 'a');
        System.out.println(clicks);
        assertThat(clicks, hasSize(6));
        assertThat(clicks, contains('u', 'u', 'u', 'u', 'u', '!'));

        clicks = keyClicks('a', 'f');
        System.out.println(clicks);
        assertThat(clicks, hasSize(2));
        assertThat(clicks, contains('d', '!'));

        clicks = keyClicks('c', 'j');
        System.out.println(clicks);
        assertThat(clicks, hasSize(4));
        assertThat(clicks, contains('d', 'r', 'r', '!'));

        clicks = keyClicks('f', 'a');
        System.out.println(clicks);
        assertThat(clicks, hasSize(2));
        assertThat(clicks, contains('u', '!'));

        clicks = keyClicks('j', 'c');
        System.out.println(clicks);
        assertThat(clicks, hasSize(4));
        assertThat(clicks, contains('u', 'l', 'l', '!'));

        clicks = keyClicks('y', 'z');
        System.out.println(clicks);
        assertThat(clicks, hasSize(6));
        assertThat(clicks, contains('l', 'l', 'l', 'l', 'd', '!'));

        clicks = keyClicks('q', 'z');
        System.out.println(clicks);
        assertThat(clicks, hasSize(4));
        assertThat(clicks, contains('l', 'd', 'd', '!'));

        clicks = keyClicks('z', 'q');
        System.out.println(clicks);
        assertThat(clicks, hasSize(4));
        assertThat(clicks, contains('u', 'u', 'r', '!'));
    }

    @Test
    public void wordClicks() {
        List<Character> clicks = keyClicks("robust");
        System.out.println(clicks);
        assertThat(clicks, hasSize(24));
        assertThat(clicks, contains('!', 'u', 'r', 'r', '!', 'u', 'u', 'l', 'l', 'l', '!', 'd', 'd', 'd', 'd', 'l', '!', 'u', 'r', 'r', 'r', '!', 'r', '!'));

        clicks = keyClicks("jazz");
        System.out.println(clicks);
        assertThat(clicks, hasSize(14));
        assertThat(clicks, contains('!', 'u', 'l', 'l', 'l', 'l', '!', 'd', 'd', 'd', 'd', 'd', '!', '!'));

        clicks = keyClicks("doze");
        System.out.println(clicks);
        assertThat(clicks, hasSize(23));
        assertThat(clicks, contains('!', 'd', 'd', 'r', '!', 'l', 'l', 'l', 'l', 'd', 'd', 'd', '!', 'u', 'u', 'u', 'u', 'u', 'r', 'r', 'r', 'r', '!'));
    }
}
