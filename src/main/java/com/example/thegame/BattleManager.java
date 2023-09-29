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
    Comparator<VBox> vBoxComparator = (v1, v2) -> {
        String v1ID = v1.getId();
        String v2ID = v2.getId();
        return v1ID.compareTo(v2ID);
    };
    private boolean playerTurn = true;
    private int scaleValue = 0;
    private Card currentCard;
    private final HashMap<VBox,Card> handCardMap = new HashMap<>();
    private final TreeMap<VBox,Card> playerCards = new TreeMap<>(vBoxComparator);
    private final TreeMap<VBox,Card> enemyCards = new TreeMap<>(vBoxComparator);

    public BattleManager(){

    }

    public void addHandCard(VBox vBox,Card card) {
        this.handCardMap.put(vBox,card);
    }
    public void removeHandCard(VBox vBox) {
        this.handCardMap.remove(vBox);
    }

    public Card getHandCard(VBox vBox) {
        return handCardMap.get(vBox);
    }
    public VBox getCurrentCardVBox() {
        for(Map.Entry<VBox, Card> mapEntry : handCardMap.entrySet()) {
            if (mapEntry.getValue() == currentCard)
                return mapEntry.getKey();
        }
        return null;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }
    public Card getCurrentCard() {
        return this.currentCard;
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



