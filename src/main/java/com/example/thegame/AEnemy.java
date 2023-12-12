package com.example.thegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AEnemy {
    List<HashMap<String, Card>> movesArray = new ArrayList<>();
    private int turnCounter = -1;

    public AEnemy() {
        loadMoves();
    }

    public abstract void loadMoves();

    public abstract void endScene(boolean win, BattleSceneController bsc);


    public List<HashMap<String, Card>> getMovesArray() {
        return movesArray;
    }

    public HashMap<String, Card> getCurrentTurn() {
        this.turnCounter++;
        if (turnCounter >= movesArray.size()) {
            return new HashMap<>();
        }
        return movesArray.get(turnCounter);
    }

    public int getTurnCounter() {
        return turnCounter;
    }


}
