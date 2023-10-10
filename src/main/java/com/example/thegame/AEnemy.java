package com.example.thegame;

import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AEnemy {
    ArrayList<HashMap<VBox, Card>> movesArray;
    private int turnCounter = 0;

    public AEnemy(ArrayList<HashMap<VBox, Card>> moves) {
        movesArray = moves;
    }

    public ArrayList<HashMap<VBox, Card>> getMovesArray() {
        return movesArray;
    }

    public HashMap<VBox, Card> getCurrentMove() {
        return movesArray.get(turnCounter);
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void addTurnCounter() {
        this.turnCounter++;
    }


}
