package com.company;

import java.util.ArrayList;
import com.company.Card.*;

public class Dealer {
    public Hand hand;

    public Dealer() {
        hand = new Hand();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                hand.take(new Card(suit, rank));
            }
        }
    }

    private Card dealCard() {
        Card dealCard = hand.cards.get(0);
        hand.cards.remove(0);
        return dealCard;
    }

    public void deal(ArrayList<Player> players) {
        int i = 0;
        while(!hand.cards.isEmpty()) {
            players.get(i).hand.take(dealCard());
            i++;
            // If we've dealt to all players once, start from first player.
            if (i >= players.size()) {
                i = 0;
            }
        }
    }
}
