package com.example.thegame;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Map;


public class BattleSceneController {
    Card[] cardArray = new Card[]{
            new Card(1, 0, 0, "Frog", new Image(getClass().getResourceAsStream("/images/frog2.png"))),
            new Card(3, 1, 1, "Cat", new Image(getClass().getResourceAsStream("/images/cat.png"))),
            new Card(6, 1, 2, "Turtle", new Image(getClass().getResourceAsStream("/images/turtle.png"))),
            new Card(3, 2, 2, "Snake", new Image(getClass().getResourceAsStream("/images/snake.png"))),
            new Card(3, 2, 2, "Dog", new Image(getClass().getResourceAsStream("/images/dog.png")))
    };
    @FXML
    private HBox boardHBox;
    @FXML
    private HBox handHBox;
    @FXML
    public VBox battleSceneVBox;
    private final BattleManager bm = new BattleManager();

    @FXML
    public Label scale;

    @FXML
    protected void initialize() {
        for (Node node : boardHBox.getChildren()) {
            if (node instanceof final VBox vBox) {
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof final VBox innerVBox) {
                        bm.addBoardCardMap(innerVBox, null);
                    }
                }
            }
        }
    }

    @FXML
    protected void onMouseClickDeck(MouseEvent event) {
        VBox vbox = (VBox) findVbox(event.getPickResult().getIntersectedNode());

        if (vbox == null)
            return;

        double computedSize = Region.USE_COMPUTED_SIZE;
        int i = vbox.getId().contains("card") ? (int) (Math.random() * ((cardArray.length - 1 - 1) + 1)) + 1 : 0;
        Card card = new Card(cardArray[i]);

        Label nameLabel = new Label();
        nameLabel.setText(card.getName());
        nameLabel.setPrefSize(computedSize, computedSize);

        Label soulsLabel = new Label();
        soulsLabel.setText("S " + card.getSouls());
        soulsLabel.setPrefSize(computedSize, computedSize);
        soulsLabel.setTranslateX(48);
        soulsLabel.setTranslateY(-10);

        Label healthLabel = new Label();
        healthLabel.setText("H " + card.getHealth());
        healthLabel.setPrefSize(computedSize, computedSize);
        healthLabel.setTranslateX(-40);
        healthLabel.setTranslateY(80);

        Label damageLabel = new Label();
        damageLabel.setText("D " + card.getDamage());
        damageLabel.setPrefSize(computedSize, computedSize);
        damageLabel.setTranslateX(40);

        Separator separator = new Separator();
        separator.setOpacity(0);

        ImageView image = new ImageView();
        image.setImage(card.getImage());
        image.setFitWidth(110);
        image.setFitHeight(66);
        image.setTranslateY(-20);

        VBox cardVBox = new VBox(nameLabel, soulsLabel, healthLabel, separator, image, damageLabel);
        cardVBox.setId("handCardContainer");
        cardVBox.setOnMouseClicked(this::onMouseClickHand);
        cardVBox.setPrefSize(computedSize, computedSize);
        cardVBox.setStyle("-fx-border-color: black");
        cardVBox.setAlignment(Pos.CENTER);
        handHBox.getChildren().add(cardVBox);
        bm.addHandCard(cardVBox, card);

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
            if (card != null) {
                healthLabel = card.getHealth() + " HP";
                damageLabel = card.getDamage() + "DMG";
                soulsLabel = card.getSouls() + " S";
                nameLabel = card.getName();
                image = card.getImage();
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
                    if (imageView.getId().contains("Image")) {
                        imageView.setImage(image);
                    }
                }
            }
        }
    }

    @FXML
    protected void onButtonClick() {
        bm.fight(this);
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