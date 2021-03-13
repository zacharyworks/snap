package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GameEngine {
    private final Config gameConfig;

    public GameEngine(Config gameConfig) {
        this.gameConfig = gameConfig;
    }

    /**
     * Print a string representation of how many cards the players currently hold
     * and what the current round is.
     */
    private void printRoundState(ArrayList<Player> players, int rounds) {
        if (!gameConfig.getPrintState()) {
            return;
        }
        System.out.printf("\nRound %d:\n", rounds);
        for (Player p : players) {
            p.toString();
        }
    }

    /**
     * For a list of players, check if the card at the top of the 'playedCard'
     * hand matches the current last played card.
     *
     * Returns the players who hand satisfies the snap condition.
     */
    private Player checkSnapped(ArrayList<Player> players, Player lastPlayed) {
        for(Player player : players) {
            if (!player.playedCards.cards.isEmpty()
                    && !lastPlayed.playedCards.cards.isEmpty()
                    && player != lastPlayed) {

                // The two cards we're comparing
                Card playerCard = player.playedCards.topCard();
                Card playedCard = lastPlayed.playedCards.topCard();

                boolean sameRank = playerCard.rank == playedCard.rank;
                boolean sameSuit = playerCard.suit == playedCard.suit;

                // Override actual values with true if user has configured
                // not to check for rank and/or suit. (If one value is always
                // true, then effectively only the other value is checked).
                if (gameConfig.dontCheckRank()) {
                    sameRank = true;
                }
                if (gameConfig.dontCheckSuit()) {
                    sameSuit = true;
                }

                return sameRank && sameSuit ? player : null;
            }
        }
        return null;
    }

    // I've not worked much with threads since uni, but thought it would be fun
    // to use race conditions to emulate two players checking if the snap
    // condition is met.
    // As such this method is heavily influenced by https://stackoverflow.com/questions/3376586/how-to-start-two-threads-at-exactly-the-same-time/3376628
    // ... As it turns out a particular thread seems to win repeatedly, hence I re-introduced
    // a random mechanic the user can choose to use instead during config.

    /**
     * Assign each player a thread which races to find the snap condition
     */
    private void raceSnap(ArrayList<Player> players, Player lastPlayed) {

        // Players + 1 threads, the excess thread 'controls' the others (with the desire to
        // start all threads at roughly the same time)
        final CyclicBarrier gate = new CyclicBarrier(players.size() + 1);
        final Player[] winner = {null};
        final Player[] snapped = {null};

        ArrayList<Thread> threads = new ArrayList<>();

        for (Player playAs : players) {
            Thread playerThread = new Thread(){
                public void run(){
                    try {
                        gate.await();
                        Player snappedPlayer = checkSnapped(players, lastPlayed);

                        // Help simulate race condition, effectively makes approach
                        // random instead of race condition.
                        // https://stackoverflow.com/questions/7446052/provoking-a-race-condition-in-java
                        if (gameConfig.spoofReactionDelays()) {
                            Thread.sleep(new Random().nextInt(25));
                        }

                        // First thread to set themselves as winner wins
                        synchronized(winner) {
                            if (snappedPlayer != null && winner[0] == null) {
                                winner[0] = playAs;
                                snapped[0] = snappedPlayer;
                            }
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }};
            threads.add(playerThread);
        }

        for (Thread player : threads) {
            player.start();
        }

        // Now the player threads are waiting on the final thread
        // to call gate.await() before they will commence.
        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Now wait until all threads are finished (thread.Join blocks the current
        // thread).
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // If we have a winner, they take cards from the snapped player,
        // and the player who last played a card.
        if (snapped[0] != null && winner[0] != null) {
            winner[0].hand.takeAll(snapped[0].playedCards.relinquishHand());
            winner[0].hand.takeAll(lastPlayed.playedCards.relinquishHand());
        }
    }

    /**
     * Check if a player is eligible to play in the next round, if they have
     * no cards in their hand, they may replenish them with their played cards.
     */
    public boolean canPlayerPlay(Player player) {
        // "If a player runs out of cards in their face-down pile, the cards
        // are replenished with the face-up pile."
        if (player.hand.cards.isEmpty()) {
            if (!player.playedCards.cards.isEmpty()) {
                player.replenishHand();
                return true;
            } else {
                // If they don't have any cards to replenish with, they're
                // need to be removed from the game.
                return false;
            }
        }

        // If they have cards in their hand, they can play
        return true;
    }

    /**
     * The main game loop, terminates when there is only one player remaining
     * (the winner).
     */
    public void playSnap(ArrayList<Player> players) {
        boolean gameInProgress = true;
        int rounds = 0;
        while (gameInProgress) {
            rounds++;

            // Use Iterator so that players can be removed mid-game.
            for (Iterator<Player> i = players.iterator(); i.hasNext();) {
                Player player = i.next();

                // Remove player if they can't play anymore.
                if (!canPlayerPlay(player)) {
                    i.remove();
                    continue;
                }

                // If there are at least two players remaining, play...
                if (players.size() > 1) {
                    player.playCard();
                    raceSnap(players, player);
                    printRoundState(players, rounds);
                } else {
                    // Otherwise, terminal condition and we end the game.
                    System.out.println("\nWinner:");
                    players.get(0).toString();
                    gameInProgress = false;
                }
            }
        }
    }
}
