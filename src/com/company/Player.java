package com.company;

/**
 * Representation of a player
 */
public class Player {
    public int id;
    public Hand hand;
    public Hand playedCards;

    public Player(int id) {
        this.id = id;
        hand = new Hand();
        playedCards = new Hand();
    }

    /**
     * Take a card from hand and add to played cards
     */
    public void playCard() {
        playedCards.take(hand.play());
    }

    /**
     * Refresh cards in hand with all the cards the player
     * has previously played.
     */
    public void replenishHand() {
        hand.takeAll(playedCards.relinquishHand());
    }

    public String toString() {
        String pString = String.format("Player %d: hand: %d cards, played: %d cards",
                id,
                hand.cards.size(),
                playedCards.cards.size());
        System.out.println(pString);
        return null;
    }
}
