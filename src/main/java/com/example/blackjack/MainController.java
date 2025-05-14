package com.example.blackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;

public class MainController {
    @FXML private Label ngLabel;
    @FXML private Label lgLabel;
    @FXML private Label helpLabel;

    @FXML public void initialize() {
        System.out.println("Main Scene initialized!");
    }

    @FXML protected void onNGLabelClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        HelpController gameController = fxmlLoader.getController();

        File save = new File("save.txt");
        if(save.delete()) {
            System.out.println("New game! Deleted old save.");
        } else {
            System.out.println("New game! File not found.");
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Game");
        stage.show();
    }

    @FXML protected void onLGLabelClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        HelpController gameController = fxmlLoader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Game");
        stage.show();
    }

    @FXML protected void onHelpLabelClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help-view.fxml"));
        HelpController helpController = fxmlLoader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Help");
        stage.show();
    }
}