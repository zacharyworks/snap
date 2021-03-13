package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static Config generateConfig() {
        Config gameConfig = new Config();
        Scanner input = new Scanner(System.in);

        System.out.println("Answer 'y' to configure, otherwise default config" +
                " of two players, snapping on rank only, with random mechanics:");
        if (!input.nextLine().equals("y")) {
            return new Config(
                    2,
                    false,
                    true,
                    true,
                    true);
        }

        System.out.println("Number of players:");
        gameConfig.numOfPlayers = Integer.parseInt(input.nextLine());

        System.out.println("Answer 'y' to snap on card suit:");
        gameConfig.checkSuit = input.nextLine().equals("y");

        System.out.println("Answer 'y' to snap on card rank:");
        gameConfig.checkRank = input.nextLine().equals("y");

        System.out.println("Answer 'y' to make result random, or otherwise" +
                "\n threads will race to snap:");
        gameConfig.spoofReactionDelays = input.nextLine().equals("y");

        System.out.println("Answer 'y' to print how many cards each player" +
                "\n is holding for each round:");
        gameConfig.printState = input.nextLine().equals("y");

        return gameConfig;
    }

    public static void main(String[] args) {
	    // write your code here
        Config gameConfig = generateConfig();

        // Create players hands
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < gameConfig.getNumOfPlayers(); i++) {
            players.add(new Player(i));
        }

        // Create deck & deal
        Dealer dealer = new Dealer();
        dealer.hand.shuffle();
        dealer.deal(players);

        // Play game until one player holds all the cards
        GameEngine snap = new GameEngine(gameConfig);
        snap.playSnap(players);

    }
}
