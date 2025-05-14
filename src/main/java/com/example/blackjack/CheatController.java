package com.example.blackjack;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.util.ArrayList;

public class CheatController {
    @FXML private TilePane tilepane;

    @FXML public void initialize() {
        System.out.println("Cheat Scene initialized!");
    }

    // Show deck
    public void showDeck(ArrayList<Card> deck) {
        tilepane.getChildren().clear();
        for (Card card : deck) {
            System.out.println(card);
            File file = new File("images/" + card.toString() + ".png");
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(115);
            tilepane.getChildren().add(imageView);
        }
    }
}
