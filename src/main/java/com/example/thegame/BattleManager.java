package com.example.thegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class BattleManager {
    Comparator<VBox> vBoxComparator = (v1, v2) -> {
        if (v1 == null || v2 == null)
            return 0;
        String v1ID = v1.getId();
        String v2ID = v2.getId();
        return v1ID.compareTo(v2ID);
    };
    private boolean playerTurn = true;
    private int scaleValue = 0;
    private Card currentCard;
    private AEnemy enemy;
    private final ArrayList<VBox> sacrificeList = new ArrayList<>();
    private final HashMap<VBox, Card> handCardMap = new HashMap<>();
    private final HashMap<VBox, Card> boardCardMap = new HashMap<>();
    private final TreeMap<VBox, Card> playerCards = new TreeMap<>(vBoxComparator);
    private final TreeMap<VBox, Card> enemyCards = new TreeMap<>(vBoxComparator);

    public BattleManager() {

    }

    public void addHandCard(VBox vBox, Card card) {
        this.handCardMap.put(vBox, card);
    }

    public void removeHandCard(VBox vBox) {
        this.handCardMap.remove(vBox);
    }

    public Card getHandCard(VBox vBox) {
        return handCardMap.get(vBox);
    }

    public VBox getCurrentCardVBox() {
        for (Map.Entry<VBox, Card> mapEntry : handCardMap.entrySet()) {
            if (mapEntry.getValue() == currentCard)
                return mapEntry.getKey();
        }
        return null;
    }

    public void setCurrentCard(Card currentCard) {
        if (!sacrificeList.isEmpty())
            return;

        if (this.currentCard == currentCard)
            return;

        VBox currentVBox = getCurrentCardVBox();
        if (currentVBox != null) {
            currentVBox.setEffect(null);
            currentVBox.setStyle("-fx-border-color: black");
        }

        this.currentCard = currentCard;
        VBox newCurrentVBox = getCurrentCardVBox();

        if (newCurrentVBox == null) {
            return;
        }
        newCurrentVBox.setEffect(new Glow());
        newCurrentVBox.setStyle("-fx-border-color: yellow");
    }

    public Card getCurrentCard() {
        return this.currentCard;
    }

    public void addBoardCardMap(VBox vBox, Card card) {
        this.boardCardMap.put(vBox, card);

        if (vBox.getId().contains("playerCard")) {
            this.playerCards.put(vBox, card);
        } else if (vBox.getId().contains("enemyCard")) {
            this.enemyCards.put(vBox, card);
        }
    }

    public Card getBoardCard(VBox vBox) {
        return boardCardMap.get(vBox);
    }

    public HashMap<VBox, Card> getBoardCardMap() {
        return boardCardMap;
    }


    public void summonCard(VBox vBox, HBox hBox) {

        if (getCurrentCard() == null || vBox == null || hBox == null)
            return;

        if (playerCards.get(vBox) != null && currentCard.getSouls() != 0) {

            if (sacrificeList.contains(vBox)) {
                sacrificeList.remove(vBox);
                vBox.setStyle("-fx-border-color: black");
            } else {
                sacrificeList.add(vBox);
                vBox.setStyle("-fx-border-color: red");
            }
        }

        if (currentCard.getSouls() > sacrificeList.size())
            return;

        if (!sacrificeList.isEmpty() && playerCards.get(sacrificeList.get(0)) != null) {
            for (VBox vBox1 : sacrificeList) {
                vBox1.setStyle("-fx-border-color: black");
                addBoardCardMap(vBox1, null);
            }
            return;
        }

        if (playerCards.get(vBox) != null)
            return;

        VBox currentVBox = getCurrentCardVBox();

        sacrificeList.clear();
        addBoardCardMap(vBox, this.currentCard);
        setCurrentCard(null);
        hBox.getChildren().remove(currentVBox);
        removeHandCard(currentVBox);
    }


    public void fight(BattleSceneController bsc) {

        TreeMap<VBox, Card> newEnemyCards = playerTurn ? enemyCards : playerCards;
        TreeMap<VBox, Card> newPlayerCards = playerTurn ? playerCards : enemyCards;

        for (int i = 0; i < newPlayerCards.size(); i++) {

            Map.Entry<VBox, Card> playerEntry = (Map.Entry<VBox, Card>) newPlayerCards.entrySet().toArray()[i];
            Map.Entry<VBox, Card> enemyEntry = (Map.Entry<VBox, Card>) newEnemyCards.entrySet().toArray()[i];

            if (playerEntry.getValue() == null) {
                continue;
            }

            int playerDamage = playerEntry.getValue().getDamage();

            if (enemyEntry.getValue() == null) {
                scaleValue += playerTurn ? playerDamage : -playerDamage;
                continue;
            }

            int enemyHealth = enemyEntry.getValue().getHealth() - playerDamage;
            enemyEntry.getValue().setHealth(enemyHealth);

            if (enemyHealth <= 0) {
                boardCardMap.put(enemyEntry.getKey(), null);
                newEnemyCards.put(enemyEntry.getKey(), null);
            }
        }
        bsc.scale.setText(String.valueOf(scaleValue));
        playerTurn = !playerTurn;

        if (scaleValue >= 5 || scaleValue <= -5) {
            onBattleEnd(bsc);
            return;
        }

        if (!playerTurn) {
            fight(bsc);
            enemy.addTurnCounter();
            enemy.getCurrentMove();
            // for ()

        }

    }

    public void onBattleEnd(BattleSceneController bsc) {

        String currentScene = "";
        if (scaleValue >= 5) {
            currentScene = "mapScene.fxml";
        }
        if (scaleValue <= -5) {
            currentScene = "gameOverScene.fxml";
        }
        if (currentScene.isEmpty()) {
            return;
        }

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



