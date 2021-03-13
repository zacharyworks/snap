package com.company;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representation of a hand of cards
 */
public class Hand {

    public ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    // Referenced this for Random number:
    // https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
    public void shuffle() {
        ArrayList<Card> shuffledCards = new ArrayList<>();
        // Take a random card from cards left in un-shuffled pile
        // and add to new pile.
        for (int i = 0; i < 52; i++) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, this.cards.size());
            shuffledCards.add(this.cards.get(randomInt));
            cards.remove(randomInt);
        }
        cards = shuffledCards;
    }

    /**
     * Play a card from the top of a hand
     */
    public Card play() {
        Card dealCard = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return dealCard;
    }

    /**
     * Add a card to the top of a hand
     */
    public void take(Card card) {
        cards.add(card);
    }

    /**
     * Give up the hand
     */
    public ArrayList<Card> relinquishHand() {
        ArrayList<Card> cards = this.cards;
        this.cards = new ArrayList<>();
        return cards;
    }

    /**
     * Add a set of card to the bottom of a hand
     */
    public void takeAll(ArrayList<Card> cards) {
        this.cards.addAll(0, cards);
    }

    /**
     * Return the top card, useful for comparisons
     */
    public Card topCard() {
        return cards.get(cards.size() - 1);
    }
}
