package com.example.thegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardEnemy extends AEnemy {

    private static List<HashMap<String, Card>> HARD_MOVES = new ArrayList<>() {{
        add(new HashMap<>() {{
            put("enemyNextMoveContainer1", Card.hippo);
            put("enemyNextMoveContainer2", Card.dog);

        }});
        add(new HashMap<>());
        add(new HashMap<>() {{
            put("enemyNextMoveContainer4", Card.fightingBird);
        }});
        add(new HashMap<>());
        add(new HashMap<>() {{
            put("enemyNextMoveContainer3", Card.octopus);
            put("enemyNextMoveContainer2", Card.snake);
        }});
    }};

    public HardEnemy() {
        super();
    }

    @Override
    public void loadMoves() {
        List<HashMap<String, Card>> NEW_MOVES = new ArrayList<>();
        for (HashMap<String, Card> newMove : HARD_MOVES) {
            HashMap<String, Card> map = new HashMap<>();
            for (Map.Entry<String, Card> entry : newMove.entrySet()) {
                map.put(entry.getKey(), new Card(entry.getValue()));

            }
            NEW_MOVES.add(map);
        }
        movesArray = NEW_MOVES;
    }

    @Override
    public void endScene(boolean win, BattleSceneController bsc) {
        String currentScene = win ? "hardWinScene.fxml" : "hardLoseScene.fxml";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(currentScene));
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
            Scene battleScene = bsc.battleSceneVBox.getScene();
            Stage startStage = (Stage) battleScene.getWindow();
            startStage.setScene(scene);
            startStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
