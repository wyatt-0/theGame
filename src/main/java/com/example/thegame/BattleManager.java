package com.example.thegame;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
    private int playerScaleValue = 0;
    private int enemyScaleValue = 0;
    private Card currentCard;
    private AEnemy enemy;
    private final ArrayList<VBox> sacrificeList = new ArrayList<>();
    private final HashMap<VBox, Card> handCardMap = new HashMap<>();
    private final HashMap<VBox, Card> boardCardMap = new HashMap<>();
    private final TreeMap<VBox, Card> playerCards = new TreeMap<>(vBoxComparator);
    private final TreeMap<VBox, Card> enemyCards = new TreeMap<>(vBoxComparator);

    private boolean hasDrawn = true;

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
            if (mapEntry.getValue() == currentCard) {
                return mapEntry.getKey();
            }

        }
        return null;
    }

    public VBox findVBox(String name) {

        for (Map.Entry<VBox, Card> mapEntry : boardCardMap.entrySet()) {
            if (mapEntry.getKey().getId().equals(name)) {
                return mapEntry.getKey();
            }
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
        }

        this.currentCard = currentCard;
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

    public void setHasDrawn(boolean hasDrawn) {
        this.hasDrawn = hasDrawn;
    }

    public boolean getHasDrawn() {
        return this.hasDrawn;
    }

    public void setEnemy(AEnemy enemy) {
        this.enemy = enemy;
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

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.RED);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/cardBackground" + currentCard.getSouls() + ".png")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(125, 230, false, false, true, false));

        if (playerCards.get(vBox) != null && currentCard.getSouls() != 0) {
            if (sacrificeList.contains(vBox)) {
                sacrificeList.remove(vBox);
                vBox.setEffect(null);
            } else {
                sacrificeList.add(vBox);
                if (sacrificeList.size() < currentCard.getSouls())
                    vBox.setEffect(borderGlow);

            }
        }

        if (currentCard.getSouls() > sacrificeList.size())
            return;

        if (!sacrificeList.isEmpty() && playerCards.get(sacrificeList.get(0)) != null) {
            for (VBox vBox1 : sacrificeList) {
                vBox1.setBackground(null);
                vBox.setEffect(null);
                if (boardCardMap.get(vBox1).getSigil() == Sigil.SMELLY) {
                    Card card = enemyCards.get(findVBox("enemyCardContainer" + vBox1.getId().charAt(vBox1.getId().length() - 1)));
                    card.setDamage(card.getDamage() + 1);
                }
                addBoardCardMap(vBox1, null);


            }
            return;
        }

        if (playerCards.get(vBox) != null)
            return;

        VBox currentVBox = getCurrentCardVBox();
        vBox.setBackground(new Background(backgroundImage));
        vBox.setEffect(null);
        sacrificeList.clear();
        addBoardCardMap(vBox, this.currentCard);

        Card enemyCard = enemyCards.get(findVBox("enemyCardContainer" + vBox.getId().charAt(vBox.getId().length() - 1)));
        if (enemyCard != null && enemyCard.getSigil() == Sigil.SMELLY && playerCards.get(vBox).getDamage() != 0) {
            playerCards.get(vBox).setDamage(playerCards.get(vBox).getDamage() - 1);
        }
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


            if (playerEntry.getValue() == null || playerEntry.getValue().getDamage() <= 0) {
                continue;
            }


            int playerDamage = playerEntry.getValue().getDamage();
            int originalPlayerDamage = playerEntry.getValue().getDamage();

            if (playerEntry.getValue().getSigil() == Sigil.ARMS) {

                for (Map.Entry<VBox, Card> enemy : newEnemyCards.entrySet()) {

                    if (enemy.getValue() == null) {
                        addToScale(playerDamage);
                        continue;
                    }
                    boolean hasBramble = enemy.getValue().getSigil() == Sigil.BRAMBLE;


                    enemy.getValue().setHealth(enemy.getValue().getHealth() - playerDamage);
                    if (enemy.getValue().getHealth() <= 0) {
                        cardDeath(bsc, enemy, playerEntry);
                    }
                    if (hasBramble) {
                        playerEntry.getValue().setHealth(playerEntry.getValue().getHealth() - 1);

                        if (playerEntry.getValue().getHealth() <= 0) {
                            cardDeath(bsc, playerEntry, enemy);
                            break;
                        }
                    }

                }
                continue;
            }

            if (enemyEntry.getValue() == null) {
                addToScale(playerDamage);
                continue;
            }

            if (playerEntry.getValue().getSigil() == Sigil.FLIGHT) {
                if (playerEntry.getValue().getSigil() == Sigil.FLIGHT && enemyEntry.getValue().getSigil() != Sigil.HUNTER) {
                    addToScale(playerDamage);
                    continue;
                }

            }

            int enemyHealth = enemyEntry.getValue().getHealth() - playerDamage;

            // poison - instantly kills enemy card
            if (playerEntry.getValue().getSigil() == Sigil.POISON) {
                enemyHealth = 0;
            }


            enemyEntry.getValue().setHealth(enemyHealth);

            Map.Entry<VBox, Card> enemyEntryCopy = Map.entry(enemyEntry.getKey(), new Card(enemyEntry.getValue()));
            if (enemyHealth <= 0) {
                cardDeath(bsc, enemyEntry, playerEntry);
            }

            // bramble - player takes 1 damage if they deal damage to card with this sigil
            if (enemyEntryCopy.getValue().getSigil() == Sigil.BRAMBLE) {
                playerEntry.getValue().setHealth(playerEntry.getValue().getHealth() - 1);

                if (playerEntry.getValue().getHealth() <= 0) {
                    cardDeath(bsc, playerEntry, enemyEntryCopy);
                }
            }
        }
        bsc.playerScale.setText(String.valueOf(playerScaleValue));
        bsc.enemyScale.setText(String.valueOf(enemyScaleValue));
        playerTurn = !playerTurn;

        if (playerScaleValue >= enemyScaleValue + 8 || enemyScaleValue >= playerScaleValue + 8) {
            onBattleEnd(bsc);
            return;
        }

        if (!playerTurn) {
            HashMap<String, Card> currentTurnMap = enemy.getCurrentTurn();
            for (Map.Entry<VBox, Card> enemyCardMap : enemyCards.entrySet()) {

                String enemyID = enemyCardMap.getKey().getId();
                char enemyNum = enemyID.charAt(enemyID.length() - 1);
                VBox enemyNextBox = findVBox("enemyNextMoveContainer" + enemyNum);
                Card playerCard = playerCards.get(findVBox("playerCardContainer" + enemyNum));


                if (findVBox("enemyNextMoveContainer" + enemyNum) == null) {
                    continue;
                }

                if (enemyCardMap.getValue() == null) {

                    addBoardCardMap(enemyCardMap.getKey(), boardCardMap.get(enemyNextBox));

                    if (boardCardMap.get(enemyNextBox) != null && playerCard != null) {

                        if (playerCard.getSigil() == Sigil.SMELLY && enemyCardMap.getValue().getDamage() > 0) {
                            enemyCardMap.getValue().setDamage(enemyCardMap.getValue().getDamage() - 1);
                        }
                        if (enemyCardMap.getValue().getSigil() == Sigil.SMELLY && playerCard.getDamage() > 0) {
                            playerCard.setDamage(playerCard.getDamage() - 1);
                        }
                    }
                    addBoardCardMap(findVBox("enemyNextMoveContainer" + enemyNum), null);
                }


                if (boardCardMap.get(enemyNextBox) == null) {
                    addBoardCardMap(findVBox("enemyNextMoveContainer" + enemyNum), currentTurnMap.get("enemyNextMoveContainer" + enemyNum));
                    findVBox("enemyNextMoveContainer" + enemyNum).setBackground(null);
                }

            }

            fight(bsc);
        }
    }

    public void addToScale(int damage) {
        if (playerTurn) {
            playerScaleValue += damage;
        } else {
            enemyScaleValue += damage;
        }
    }

    public void cardDeath(BattleSceneController bsc, Map.Entry<VBox, Card> enemyEntry, Map.Entry<VBox, Card> playerEntry) {
        // bloodthirsty - when a card gets a kill they get +1 damage
        if (playerEntry.getValue().getSigil() == Sigil.BLOODTHIRSTY) {
            playerEntry.getValue().setDamage(playerEntry.getValue().getDamage() + 1);
        }
        if (enemyEntry.getValue().getSigil() == Sigil.SMELLY) {
            String enemyID = enemyEntry.getKey().getId();
            int enemyCollum = Integer.parseInt(String.valueOf(enemyID.charAt(enemyID.length() - 1)));

            Map<VBox, Card> newPlayerCards = enemyEntry.getKey().getId().contains("playerCard") ? enemyCards : playerCards;

            Map.Entry<VBox, Card> newEntry = (Map.Entry<VBox, Card>) newPlayerCards.entrySet().toArray()[enemyCollum - 1];

            if (newEntry.getValue() != null) {
                newEntry.getValue().setDamage(newEntry.getValue().getDamage() + 1);
            }

        }
        if (enemyEntry.getValue().getSigil() == Sigil.UNDEAD) {
            bsc.createNewCard(new Card(Card.opossum));
        }
        addBoardCardMap(enemyEntry.getKey(), null);
        enemyEntry.getKey().setBackground(null);
    }

    public void onBattleEnd(BattleSceneController bsc) {

        boolean win;
        if (playerScaleValue >= enemyScaleValue + 8) {
            win = true;
        } else if (enemyScaleValue >= playerScaleValue + 8) {
            win = false;
        } else {
            return;
        }
        enemy.endScene(win, bsc);

    }
}



