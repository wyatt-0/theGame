package com.example.thegame;

public class OutOfCardsException extends Exception {
    private static final String message = " Deck is out of cards";

    public OutOfCardsException(String type) {
        super(type + message);
    }
}
