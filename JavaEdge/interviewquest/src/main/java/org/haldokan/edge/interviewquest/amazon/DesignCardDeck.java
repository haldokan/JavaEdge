package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - I used an array with a current index that points to the top of the deck
 * and moves up and down as the cards are stacked and unstacked. We can shuffle the deck at any point taking into account
 * only the available cards. We can get the available cards and reset the deck to make all cards available (basically
 * starting a new round).
 * <p>
 * The Question: 3_STAR + 1/2
 * <p>
 * Design a deck of cards that can be used for different card game applications.
 * Please code out what you would need for the deck class and a card class.
 * Implement a deal method.
 * <p>
 * Created by haytham.aldokanji on 8/16/16.
 */
public class DesignCardDeck {

    public static void main(String[] args) {
        DesignCardDeck driver = new DesignCardDeck();
        driver.test();
    }

    private void test() {
        int deckSize = 52;
        Card[] cards = new Card[deckSize];
        IntStream.range(0, deckSize).forEach(i -> cards[i] = new Card("card" + i));

        Deck deck = new Deck(cards);
        System.out.printf("%s%n", deck);

        deck.shuffle();
        System.out.printf("%s%n", deck);

        Card[] dealtCards = deck.deal(7);
        assertThat(dealtCards.length, is(7));

        Card[] availableCards = deck.getAvailableCards();
        System.out.printf("Available: %s%n", Arrays.toString(availableCards));
        assertThat(availableCards.length, is(deckSize - 7));

        deck.stackBack(3);
        availableCards = deck.getAvailableCards();
        System.out.printf("Available: %s%n", Arrays.toString(availableCards));
        assertThat(availableCards.length, is(deckSize - 4));

        deck.reset();
        availableCards = deck.getAvailableCards();
        System.out.printf("Available: %s%n", Arrays.toString(availableCards));
        assertThat(availableCards.length, is(deckSize));
    }

    private static class Deck {
        private final Card[] cards;
        private int topCardIndex;

        public Deck(Card[] cards) {
            long distinctCards = Arrays.stream(cards).map(Card::getName).distinct().count();
            if (distinctCards != cards.length) {
                throw new IllegalArgumentException("Some cards are duplicate: " + Arrays.toString(cards));
            }
            this.cards = Arrays.copyOf(cards, cards.length);
            this.topCardIndex = cards.length - 1;
        }

        public void shuffle() {
            Random random = new Random();
            for (int i = 0; i <= topCardIndex; i++) {
                int randIndex = random.nextInt(i + 1);
                Card tmp = cards[i];
                cards[i] = cards[randIndex];
                cards[randIndex] = tmp;
            }
        }

        public Card[] deal(int numCards) {
            if (numCards > topCardIndex + 1) {
                throw new IllegalStateException("Deck has only " + topCardIndex + 1 + ", but tried to deal " + numCards);
            }

            Card[] dealtCards = new Card[numCards];
            for (int i = 0; i < numCards; i++) {
                dealtCards[numCards - i - 1] = cards[topCardIndex--];
            }
            return dealtCards;
        }

        // stack back some cards on deck: assumption is that you stack back the last cards you dealt
        public void stackBack(int numCards) {
            if (numCards > cards.length - topCardIndex - 1) {
                throw new IllegalArgumentException("Too many cards to stack back: " + numCards);
            }
            topCardIndex += numCards;
        }

        public Card[] getAvailableCards() {
            return Arrays.copyOf(cards, topCardIndex + 1);
        }

        public void reset() {
            topCardIndex = cards.length - 1;
        }

        @Override
        public String toString() {
            return "Deck{" +
                    "topCardIndex=" + topCardIndex +
                    ", cards=" + Arrays.toString(cards) +
                    '}';
        }
    }

    // immutable
    private static class Card {
        private final String name;

        public Card(String name) {
            this.name = name;
        }

        public static Card create(String name) {
            return new Card(name);
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
