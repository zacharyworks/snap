package com.company;

public class Config {
    public int numOfPlayers;
    public boolean checkSuit;
    public boolean checkRank;
    public boolean printState;
    public boolean spoofReactionDelays;

    public Config() {}

    public Config(
            int numOfPlayers,
            boolean checkSuit,
            boolean checkRank,
            boolean printState,
            boolean spoofReactionDelays) {
        this.numOfPlayers = numOfPlayers;
        this.checkSuit = checkSuit;
        this.checkRank = checkRank;
        this.printState = printState;
        this.spoofReactionDelays = spoofReactionDelays;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean dontCheckSuit() {
        return !checkSuit;
    }

    public boolean dontCheckRank() {
        return !checkRank;
    }

    public boolean getPrintState() {
        return printState;
    }

    public boolean spoofReactionDelays() {
        return spoofReactionDelays;
    }
}
