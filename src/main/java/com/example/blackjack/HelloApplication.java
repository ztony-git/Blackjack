package com.example.blackjack;
/* =============================================================
Author          : Tony Zhang
Class           : CSC 164
Class Section   : 401
Date            : 4/16/2024
Assignment      : Module 11, Lab 1
Notes           : N/A

I wrote all the code that was not a part of the starter code,
and I have provided citations to any of my references below.
 * https://opengameart.org/content/playing-cards-vector-png Card PNGs
 * https://www.pinterest.com/pin/351210470914902487/ Card back PNG
============================================================= */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}