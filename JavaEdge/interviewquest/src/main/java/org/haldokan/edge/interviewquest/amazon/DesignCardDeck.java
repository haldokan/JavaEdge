package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;
import java.util.Random;

/**
 * My solution to an Amazon interview question
 * <p>
 * The Question: 3_STAR + 1/2
 * <p>
 * Design a deck of cards that can be used for different card game applications.
 * Please code out what you would need for the deck class and a card class.
 * Implement a deal method.
 * <p>
 * Created by haytham.aldokanji on 8/16/16.
 */
// todo add tests
public class DesignCardDeck {

    private static class Deck {
        private final Card[] cards;
        private int topCardIndex;

        public Deck(Card[] cards) {
            long distinctCards = Arrays.stream(cards).map(Card::getName).distinct().count();
            if (distinctCards != cards.length) {
                throw new IllegalArgumentException("Some cards are duplicate: " + Arrays.toString(cards));
            }
            this.cards = Arrays.copyOf(cards, cards.length);
            this.topCardIndex = cards.length;
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
