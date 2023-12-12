package com.example.thegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreenController {

    @FXML
    private Pane startPane;

    @FXML
    protected void startScreenEasyButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("battleScene.fxml"));
            BattleSceneController bsc = new BattleSceneController(true);
            fxmlLoader.setController(bsc);
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
            Stage startStage = (Stage) startPane.getScene().getWindow();
            startStage.setScene(scene);
            startStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    protected void startScreenHardButton() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("battleScene.fxml"));
            BattleSceneController bsc = new BattleSceneController(false);
            fxmlLoader.setController(bsc);
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
            Stage startStage = (Stage) startPane.getScene().getWindow();
            startStage.setScene(scene);
            startStage.show();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    protected void startScreenRulesButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rules.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            Stage startStage = (Stage) startPane.getScene().getWindow();
            startStage.setScene(scene);
            startStage.show();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
