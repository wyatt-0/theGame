package com.example.thegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HardLoseSceneController {

    @FXML
    Pane hardLosePane;

    @FXML
    protected void hardMenuButton() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
            Stage startStage = (Stage) hardLosePane.getScene().getWindow();
            startStage.setScene(scene);
            startStage.show();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    protected void hardTryAgainButton() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("battleScene.fxml"));
            BattleSceneController bsc = new BattleSceneController(false);
            fxmlLoader.setController(bsc);
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.windowWidth, MainApplication.windowHeight);
            Stage startStage = (Stage) hardLosePane.getScene().getWindow();
            startStage.setScene(scene);
            startStage.show();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
