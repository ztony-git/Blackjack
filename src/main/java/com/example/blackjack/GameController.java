package com.example.blackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class GameController {
    @FXML private Label hitLabel;
    @FXML private Label holdLabel;
    @FXML private Label scoreLabel;
    @FXML private Label betLabel;
    @FXML private TextField betTextField;
    @FXML private Label userValueLabel;
    @FXML private Label dealerValueLabel;
    @FXML private HBox dealerHand;
    @FXML private HBox userHand;
    @FXML private Button saveButton;
    @FXML private Label gameTextLabel;

    // Dealer
    private int dealerValue;
    private ArrayList<Card> dealerHandArr;
    private Card hiddenCard;
    private int dealerAceAmt;

    // User
    private int userValue;
    private ArrayList<Card> userHandArr;
    private int userAceAmt;
    private boolean bust;
    private boolean reveal;

    private int score;
    private int scoreBet;

    ArrayList<Card> deck = new ArrayList<Card>();

    // Initialize game
    @FXML public void initialize() throws FileNotFoundException {
        hitLabel.setDisable(true);
        holdLabel.setDisable(true);

        try { // Check if theres a save file
            System.out.println("Loaded save!");
            File file = new File("save.txt");
            if(file.exists()) { // load game
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                score = Integer.parseInt(reader.readLine());
                scoreLabel.setText("$" + String.valueOf(score));

                String line;
                while((line = reader.readLine()) != null) {
                    int value = Integer.parseInt(line);
                    String suit = reader.readLine();
                    deck.add(new Card(value, suit));
                }
                reader.close();
            } else { // No save file
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) { // New game
            System.out.println("New game!");
            score = 100;
            scoreLabel.setText("$" + score);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame() {
        System.out.println("Game initialized!");

        // Enable buttons
        betLabel.setDisable(true);
        bust = false;

        // Dealer
        dealerValue = 0;
        dealerHandArr = new ArrayList<Card>();
        dealerAceAmt = 0;

        //Player
        userValue = 0;
        userHandArr = new ArrayList<Card>();
        userAceAmt = 0;

        // Draw dealer cards
        drawDealerCard(false);
        drawDealerCard(true);

        // Draw user cards
        for(int i = 0; i < 2; i++)
            drawUserCard();


        // ADD CONDITION WHERE IF USER VALUE == 21, GAME AUTO ENDS
        if(userValue == 21) {
            endGame();
        }

        printDecks();
        cardImage();

    }

    public void buildDeck() {
        /*
         * 1 = Ace, value of 1 or 11
         * 11 = Jack, value of 10
         * 12 = Queen, value of 10
         * 13 = King, value of 10
         */
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        //int[] values = {1, 13};
        String[] suits = {"clubs", "hearts", "spades", "diamonds"};
        for (int value : values) {
            for (String suit : suits) {
                deck.add(new Card(value, suit));
            }
        }
    }

    // Draw cards if deck is empty
    public Card drawCard() {
        if(deck.isEmpty()) {
            buildDeck();
        }

        int index = (int) Math.floor(Math.random() * deck.size());
        Card pickedCard = deck.get(index);
        deck.remove(index);

        return pickedCard;
    }

    // Draw cards for the dealer
    public void drawDealerCard(boolean isHidden) {
        if(!isHidden) {
            Card drawnCard = drawCard();
            if (drawnCard.isAce())
                dealerAceAmt++;
            dealerValue += drawnCard.getValue();
            dealerHandArr.add(drawnCard);
        } else {
            hiddenCard = drawCard();
            if (hiddenCard.isAce())
                dealerAceAmt++;
            dealerValue += hiddenCard.getValue();
            dealerHandArr.add(hiddenCard);
        }

        dealerValueLabel.setText(String.valueOf(dealerValue));
        cardImage();
    }

    // Draw cards for the user
    public void drawUserCard() {
        Card drawnCard = drawCard();
        if(drawnCard.isAce())
            userAceAmt++;
        userValue+=drawnCard.getValue();
        userHandArr.add(drawnCard);

        userValueLabel.setText(String.valueOf(userValue));
        cardImage();
    }

    // Debugging
    public void printDecks() {
        System.out.println("USER DECK: " + userHandArr + " | Value: " + userValue);
        System.out.println("DEALER DECK: " + dealerHandArr + " | Value: " + dealerValue);
        System.out.println(deck);
    }

    // Return value if there is an ace and it needs to be applied
    public int checkUserAce() {
        if(userValue > 21 && userAceAmt >= 1) {
            userValue-=10;
            userAceAmt--;
            userValueLabel.setText(String.valueOf(userValue));
        }
        return userValue;
    }

    public int checkDealerAce() {
        if(dealerValue > 21 && dealerAceAmt >= 1) {
            dealerValue-=10;
            dealerAceAmt--;
            dealerValueLabel.setText(String.valueOf(dealerValue));
        }
        return dealerValue;
    }

    // Hit label click
    @FXML protected void onHitLabelClicked() {
        if(betTextField.isEditable()) {
            return;
        }

        drawUserCard();
        if(userValue==21) {
            // Win game
            System.out.println("Hit 21!");
            onHoldLabelClicked();
        } else if(checkUserAce() > 21) {
            // End game
            System.out.println("Bust!");
            bust = true;
            endGame();
            //onHoldLabelClicked();
        }
        printDecks();
    }
    // Hold label click
    @FXML protected void onHoldLabelClicked() {
        if(betTextField.isEditable()) {
            return;
        }

        reveal = true;

        while(checkDealerAce() < 17 || dealerValue < userValue) {
            drawDealerCard(false);
        }
        printDecks();
        endGame();
    }
    // Bet label click
    @FXML protected void onBetLabelClicked() {
        if(!betTextField.isEditable()) {
            return;
        }

        try {
            if(betTextField.getText().contains("-")) {
                System.out.println("Not a positive integer!");
                gameTextLabel.setText("Not a positive integer!");
                return;
            }
            scoreBet = Integer.parseInt(betTextField.getText());
            if(scoreBet == 0) {
                gameTextLabel.setText("Cannot bet 0!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Not an integer!");
            gameTextLabel.setText("Not an integer!");
            return;
        }
        if(scoreBet > score) {
            System.out.println("Bet is greater than what you have");
            gameTextLabel.setText("Bet is greater than what you have");
            return;
        }

        hitLabel.setDisable(false);
        holdLabel.setDisable(false);

        betTextField.setEditable(false);
        betLabel.setDisable(true);

        gameTextLabel.setText("");
        startGame();
    }

    // End game
    public void endGame() {
        System.out.println("END GAME!");
        hitLabel.setDisable(true);
        holdLabel.setDisable(true);
        if(bust) {
            System.out.println("Bust!");
            gameTextLabel.setText("Bust!");
            score-=scoreBet;
        } else {
            if (userValue > dealerValue) {
                if(userValue == 21 && userHandArr.size() == 2) {
                    System.out.println("Blackjack!");
                    gameTextLabel.setText("Blackjack!");
                    score+= (int) (scoreBet*1.5);
                } else {
                    System.out.println("User wins!");
                    gameTextLabel.setText("Win!");
                    score += scoreBet;
                }
            } else if (userValue < dealerValue && dealerValue < 21) {
                System.out.println("Dealer wins!");
                gameTextLabel.setText("Loss!");
                score-=scoreBet;
            } else if(dealerValue > 21) {
                System.out.println("Dealer bust!");
                gameTextLabel.setText("Win!");
                score += scoreBet;
            } else {
                System.out.println("Tie!");
                gameTextLabel.setText("Tie!");
            }
        }

        scoreLabel.setText("$" + score);

        if(score <= 0) {
            gameTextLabel.setText("Game over! Start a new game.");
        }

        reveal = false;
        scoreBet = 0;
        betTextField.setEditable(true);
        betLabel.setDisable(false);
    }

    // Showcase card image
    public void cardImage() {
        // Clean both hands
        dealerHand.getChildren().clear();
        userHand.getChildren().clear();

        for (int i = 0; i < dealerHandArr.size(); i++) {
            if(dealerHandArr.size() == 2 && !reveal) {
                if(i == 1) {
                    File file = new File("images/cardback.png");
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(115);
                    dealerHand.getChildren().add(imageView);
                    continue;
                }
            }
            File file = new File("images/" + dealerHandArr.get(i).toString() + ".png");
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(115);
            dealerHand.getChildren().add(imageView);
        }
        for (Card card : userHandArr) {
            File file = new File("images/" + card.toString() + ".png");
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(115);
            userHand.getChildren().add(imageView);
        }
    }

    // Open cheat label
    @FXML protected void onCheatLabelClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cheat-view.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Cheat");
        stage.show();

        CheatController cheatController = fxmlLoader.getController();
        cheatController.showDeck(deck);
    }

    // Save file
    @FXML protected void onSaveButtonClicked() {
        try {
            // Save score
            System.out.println("Saved!");
            System.out.println("Score: " + score);
            BufferedWriter writer = new BufferedWriter(new FileWriter("save.txt"));
            writer.write(String.valueOf(score));
            writer.newLine();

            // Save deck
            for(Card card : deck) {
                writer.write(String.valueOf(card.getTrueValue()));
                writer.newLine();
                writer.write(card.getSuit());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
