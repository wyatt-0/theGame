package com.example.thegame;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;


public class BattleSceneController {
    Card frog = new Card(1,0,1,"Frog",new Image(getClass().getResourceAsStream("/images/frog2.png")));
    Card cat = new Card(3,1,1,"Cat",new Image(getClass().getResourceAsStream("/images/cat.png")));
    Card turtle = new Card(6,1,2,"Turtle",new Image(getClass().getResourceAsStream("/images/turtle.png")));
    Card snake = new Card(3,2,2,"Snake",new Image(getClass().getResourceAsStream("/images/snake.png")));
    Card dog = new Card(3,2,2,"Dog",new Image(getClass().getResourceAsStream("/images/dog.png")));

    @FXML private Pane boardPane;
    @FXML public Pane battleScenePane;
    private BattleManager test = new BattleManager();
    HashMap<VBox,Card> cardMap = new HashMap<>();

    @FXML public Label scale;
    @FXML protected void initialize(){
        for (Node node: boardPane.getChildren()) {
            if (node instanceof final VBox vBox) {
                cardMap.put(vBox,null);
            }
        }
    }

    @FXML protected void onMouseClick(MouseEvent event) {
        VBox vbox = (VBox) findVbox(event.getPickResult().getIntersectedNode());
        if (vbox == null || !vbox.getId().contains("CardContainer"))
            return;
        cardMap.put(vbox, new Card(dog));
        cardDisplay();
    }
     private void cardDisplay() {
        for (Map.Entry<VBox, Card> mapEntry : cardMap.entrySet()) {
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
    @FXML protected void onButtonClick() {
        test.updateCards(cardMap);
        test.fight(this);
        cardDisplay();
    }
    protected Node findVbox(Node node) {
        if (node == null){
            return null;
        }
        if (node instanceof final VBox vBox) {
            return vBox;
        }
        return findVbox(node.getParent());
    }
}