package com.company;

/**
 * Representation of a card from a typical deck, Ace is low.
 */
public class Card {
    enum Suit {
        Diamonds,
        Clubs,
        Hearts,
        Spades
    }

    enum Rank {
        Ace,
        Two,
        Three,
        Four,
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Jack,
        Queen,
        King
    }

    public Suit suit;
    public Rank rank;

    Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
}
