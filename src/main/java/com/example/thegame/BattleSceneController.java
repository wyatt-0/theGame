package com.example.thegame;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BattleSceneController {


    public boolean easy = true;


    ArrayList<Card> cardArray;
    ArrayList<Card> frogArray;
    @FXML
    private HBox boardHBox;
    @FXML
    private HBox handHBox;
    @FXML
    public VBox battleSceneVBox;

    @FXML

    private final BattleManager bm = new BattleManager();


    @FXML
    public Label playerScale;
    @FXML
    public Label enemyScale;

    @FXML
    public Label deckErrorMessage;


    public BattleSceneController(boolean easy) {
        this.easy = easy;
        cardArray = easy ? new ArrayList<>(List.of(
                new Card(Card.cat), new Card(Card.turtle), new Card(Card.snake), new Card(Card.dog), new Card(Card.skunk)
        )) : new ArrayList<>(List.of(
                new Card(Card.dog), new Card(Card.skunk), new Card(Card.octopus), new Card(Card.cat), new Card(Card.bear), new Card(Card.opossum), new Card(Card.porcupine)
        ));
        frogArray = easy ? new ArrayList<>(List.of(
                new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog),
                new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog)
        )) :
                new ArrayList<>(List.of(new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog), new Card(Card.frog)
                ));
    }

    public BattleSceneController() {

    }

    @FXML
    protected void initialize() {


        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/HOFBoard.png")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(125, 230, false, false, true, false));
        battleSceneVBox.setBackground(new Background(backgroundImage));

        AEnemy enemy = easy ? new NormalEnemy() : new HardEnemy();
        bm.setEnemy(enemy);
        HashMap<String, Card> turnMap = enemy.getCurrentTurn();


        for (int i = 0; i < 4; i++) {

            Card card = new Card();
            if (i == 0) {
                card = frogArray.get(i);
                createNewCard(card);
                frogArray.remove(i);
                continue;
            }
            int j = (int) (Math.random() * ((cardArray.size() - 1) + 1));
            card = new Card(cardArray.get(j));
            createNewCard(card);
            cardArray.remove(j);
        }


        for (Node node : boardHBox.getChildren()) {
            if (node instanceof final VBox vBox) {
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof final VBox innerVBox) {
                        if (innerVBox.getId().contains("enemyNextMoveContainer")) {
                            bm.addBoardCardMap(innerVBox, turnMap.get(innerVBox.getId()));

                        } else
                            bm.addBoardCardMap(innerVBox, null);
                    }
                }
            }
        }
        cardDisplay();
    }

    public void createNewCard(Card card) {

        double computedSize = Region.USE_COMPUTED_SIZE;
        Font gameFont = Font.font("Monospaced", FontWeight.BOLD, 17);


        Label nameLabel = new Label();
        nameLabel.setFont(gameFont);
        nameLabel.setText(card.getName());
        nameLabel.setPrefSize(computedSize, computedSize);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setTranslateY(10);


        Label healthLabel = new Label();
        healthLabel.setFont(gameFont);
        healthLabel.setText("" + card.getHealth());
        healthLabel.setPrefSize(computedSize, computedSize);
        healthLabel.setTranslateX(37);
        healthLabel.setTranslateY(40);


        Label damageLabel = new Label();
        damageLabel.setFont(gameFont);
        damageLabel.setText("" + card.getDamage());
        damageLabel.setPrefSize(computedSize, computedSize);
        damageLabel.setTranslateX(-35);
        damageLabel.setTranslateY(20);


        Separator separator = new Separator();
        separator.setOpacity(0);

        ImageView image = new ImageView();
        image.setImage(card.getImage());
        image.setFitWidth(92);
        image.setFitHeight(97);
        image.setTranslateY(23);

        ImageView sigilImage = new ImageView();
        sigilImage.setImage(card.getSigilImage());
        sigilImage.setFitWidth(35);
        sigilImage.setFitHeight(35);
        sigilImage.setTranslateX(0);
        sigilImage.setTranslateY(-10);


        VBox cardVBox = new VBox(nameLabel, separator, image, healthLabel, damageLabel, sigilImage);

        int backgroundNum = card.getSouls();
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/cardBackground" + backgroundNum + ".png")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(125, 230, false, false, true, false));

        cardVBox.setBackground(new Background(backgroundImage));

        cardVBox.setId("handCardContainer");
        cardVBox.setOnMouseClicked(this::onMouseClickHand);
        cardVBox.setPrefSize(125, 230);
        cardVBox.setAlignment(Pos.CENTER);
        handHBox.getChildren().add(cardVBox);
        bm.addHandCard(cardVBox, card);
    }

    @FXML
    protected void onMouseClickDeck(MouseEvent event) throws OutOfCardsException {

        if (bm.getHasDrawn()) {
            return;
        }

        VBox vbox = (VBox) findVbox(event.getPickResult().getIntersectedNode());

        if (vbox == null)
            return;

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), deckErrorMessage);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event1 -> deckErrorMessage.setVisible(false));


        if (vbox.getId().contains("card")) {

            if (cardArray.isEmpty()) {
                deckErrorMessage.setVisible(true);
                fadeOut.play();
                throw new OutOfCardsException("Card");
            }

            int i = (int) (Math.random() * ((cardArray.size() - 1) + 1));
            Card card = new Card(cardArray.get(i));
            cardArray.remove(i);
            createNewCard(card);
        } else {
            if (frogArray.isEmpty()) {
                deckErrorMessage.setVisible(true);
                fadeOut.play();
                throw new OutOfCardsException("Frog");

            }
            createNewCard(frogArray.get(0));
            frogArray.remove(0);
        }
        bm.setHasDrawn(true);

    }


    @FXML
    protected void onMouseClickHand(MouseEvent event) {
        VBox vbox = (VBox) findVbox(event.getPickResult().getIntersectedNode());
        if (vbox == null)
            return;
        bm.setCurrentCard(bm.getHandCard(vbox));
    }

    @FXML
    protected void onMouseClickBoard(MouseEvent event) {

        VBox vBox = (VBox) findVbox(event.getPickResult().getIntersectedNode());

        if (vBox == null)
            return;

        bm.summonCard(vBox, handHBox);
        cardDisplay();
    }

    private void cardDisplay() {
        for (Map.Entry<VBox, Card> mapEntry : bm.getBoardCardMap().entrySet()) {
            Card card = mapEntry.getValue();
            String healthLabel = "";
            String damageLabel = "";
            String soulsLabel = "";
            String nameLabel = "";
            Image image = null;
            Image sigil = null;


            if (card != null) {
                healthLabel = card.getHealth() + "";
                damageLabel = card.getDamage() + "";
                soulsLabel = card.getSouls() + "";
                nameLabel = card.getName();
                image = card.getImage();
                sigil = card.getSigilImage();

                BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/cardBackground" + mapEntry.getValue().getSouls() + ".png")),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(125, 230, false, false, true, false));

                mapEntry.getKey().setBackground(new Background(backgroundImage));
            }
            for (Node node : mapEntry.getKey().getChildren()) {

                if (node instanceof final Label label) {
                    if (label.getId().contains("Health")) {
                        label.setText(healthLabel);
                    } else if (label.getId().contains("Damage")) {
                        label.setText(damageLabel);
                    } else if (label.getId().contains("Souls")) {
                        label.setText(soulsLabel);
                    } else if (label.getId().contains("Name")) {
                        label.setText(nameLabel);
                    }
                } else if (node instanceof final ImageView imageView) {
                    if (imageView.getId().contains("cardImage")) {
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(sigil);
                    }
                }

            }
        }
    }


    @FXML
    protected void onButtonClick() {
        bm.fight(this);
        bm.setHasDrawn(false);
        cardDisplay();
    }

    protected Node findVbox(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof final VBox vBox) {
            return vBox;
        }
        return findVbox(node.getParent());
    }
}