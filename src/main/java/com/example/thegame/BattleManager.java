package com.example.thegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BattleManager {
    Comparator<VBox> vBoxComparator = new Comparator<VBox>() {
        public int compare(VBox v1, VBox v2) {
            String v1ID = v1.getId();
            String v2ID = v2.getId();
            return v1ID.compareTo(v2ID);
        }
    };
    boolean playerTurn = true;
    int scaleValue = 0;
    TreeMap<VBox,Card> playerCards = new TreeMap<>(vBoxComparator);
    TreeMap<VBox,Card> enemyCards = new TreeMap<>(vBoxComparator);



    public BattleManager(){

    }
    public void updateCards(HashMap<VBox,Card> newCards){

        for (Map.Entry<VBox, Card> mapEntry : newCards.entrySet()) {
            if (mapEntry.getKey().getId().contains("playerCard")){
                this.playerCards.put(mapEntry.getKey(),mapEntry.getValue());
            }
            else if (mapEntry.getKey().getId().contains("enemyCard")){
                this.enemyCards.put(mapEntry.getKey(),mapEntry.getValue());
            }
        }
    }
    public void fight(BattleSceneController bsc) {

        TreeMap<VBox,Card> newEnemyCards = playerTurn ? enemyCards : playerCards;
        TreeMap<VBox,Card> newPlayerCards = playerTurn ? playerCards : enemyCards;

        for (int i = 0; i < newPlayerCards.size(); i++) {

            Map.Entry<VBox, Card> playerEntry = (Map.Entry<VBox, Card>) newPlayerCards.entrySet().toArray()[i];
            Map.Entry<VBox, Card> enemyEntry = (Map.Entry<VBox, Card>) newEnemyCards.entrySet().toArray()[i];

            if (playerEntry.getValue() == null) {
                continue;
            }

            int playerDamage = playerEntry.getValue().getDamage();

            if (enemyEntry.getValue() == null){
                scaleValue += playerTurn ? playerDamage: -playerDamage;
                continue;
            }

            int enemyHealth = enemyEntry.getValue().getHealth() - playerDamage;
            enemyEntry.getValue().setHealth(enemyHealth);

            if (enemyHealth <= 0) {
                bsc.cardMap.put(enemyEntry.getKey(), null);
                newEnemyCards.put(enemyEntry.getKey(), null);
            }
        }
        bsc.scale.setText(String.valueOf(scaleValue));
        playerTurn = !playerTurn;

        if (scaleValue >= 5 || scaleValue <= -5){
            onBattleEnd(bsc);
            return;
        }

        if(!playerTurn){
            fight(bsc);
            }
        }

        public void onBattleEnd(BattleSceneController bsc){

            String currentScene = "";
            if (scaleValue >= 5){
                currentScene = "mapScene.fxml";
            }
            if (scaleValue <= -5){
                currentScene = "gameOverScene.fxml";
            }
            if(currentScene.isEmpty()){
                return;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(currentScene));
                Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
                Scene test = bsc.battleScenePane.getScene();
                Stage startStage = (Stage) test.getWindow();
                startStage.setScene(scene);
                startStage.show();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }

        }
}


