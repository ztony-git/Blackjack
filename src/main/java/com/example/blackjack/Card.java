package com.example.blackjack;

public class Card {
    private int value;
    private String suit;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public String toString() {
        return value + "_of_" + suit;
    }
    // Return value of card
    public int getValue() {
        if(value == 1) {
            return 11;
        }
        if(value > 10) {
            return 10;
        }
        return value;
    }

    // Return the true number
    public int getTrueValue() {
        return value;
    }
    public String getSuit() {
        return suit;
    }

    public boolean isAce() {
        return (value == 1);
    }
}
