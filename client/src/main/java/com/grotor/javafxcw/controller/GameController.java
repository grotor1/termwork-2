package com.grotor.javafxcw.controller;

import com.google.gson.reflect.TypeToken;
import com.grotor.javafxcw.Main;
import com.google.gson.*;
import com.grotor.javafxcw.entity.Card;
import com.grotor.javafxcw.entity.Player;
import com.grotor.javafxcw.helpers.CardHelper;
import com.grotor.javafxcw.pages.GamePage;
import com.grotor.javafxcw.pages.MainPage;
import com.grotor.javafxcw.pages.RoomPage;
import com.grotor.javafxcw.services.ConnectionManager;
import com.grotor.javafxcw.threads.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class GameController {
    public Connection connection = ConnectionManager.getInstance().getConnection();

    public String currentPlayer;
    public String index;
    public String priorityColor;
    public boolean isChoosingColor;

    public List<Player> playerList;

    public GridPane playersContainer;
    public Label playersCurrentCard;
    public HBox cardsContainer;
    public HBox colorContainer;

    @FXML
    private void initialize() {
        Arrays.stream((new String[] {"red", "blue", "green", "yellow"})).forEach((color) -> {
            Button button = new Button(CardHelper.getColorString(color));
            button.setOnMouseClicked(event -> {
                connection.send("color", color);
                colorContainer.setDisable(true);
            });
            colorContainer.getChildren().add(button);
        });
        colorContainer.setDisable(true);
    }

    public void init() {
        connection.addHandler("player", (payload) -> Platform.runLater(() -> playerHandler(payload)));
        connection.addHandler("players", (payload) -> Platform.runLater(() -> playersHandler(payload)));
        connection.addHandler("winner", (payload) -> Platform.runLater(() -> winnerHandler(payload)));
        connection.addHandler("lastCard", (payload) -> Platform.runLater(() -> lastCardHandler(payload)));
        connection.addHandler("color", (payload) -> Platform.runLater(() -> colorHandler(payload)));
        connection.addHandler("index", (payload) -> Platform.runLater(() -> indexHandler(payload)));
        connection.addHandler("card", (payload) -> Platform.runLater(() -> cardHandler(payload)));
        connection.addHandler("delete", (payload) -> Platform.runLater(() -> deleteHandler(payload)));
    }

    private void playerHandler(String payload) {
        currentPlayer = payload;
        cardsContainer.setDisable(!currentPlayer.equals(index));
        for (int i = 0; i < playersContainer.getChildren().size(); i++) {
            Node node = playersContainer.getChildren().get(i);
            if (node instanceof VBox vBox) {
                if (String.valueOf(i).equals(currentPlayer)) {
                    Label isCurrent = new Label();
                    isCurrent.setText("Сейчас ходит");
                    vBox.getChildren().add(isCurrent);
                } else {
                    if (vBox.getChildren().size() == 3) {
                        vBox.getChildren().remove(2);
                    }
                }
            }
        }
    }

    private void playersHandler(String payload) {
        Type listType = new TypeToken<ArrayList<Player>>(){}.getType();
        List<Player> playerList = new Gson().fromJson(payload, listType);
        this.playerList = playerList;
        playersContainer.getChildren().clear();

        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            Label name = new Label();
            name.setText(player.name());
            Label cardCount = new Label();
            cardCount.setText("Карт: " + player.cardCount());
            vBox.getChildren().addAll(name, cardCount);
            playersContainer.add(vBox, i % 5 , i / 5);
        }
    }

    private void winnerHandler(String payload) {
        playersCurrentCard.setText("Победитель: " + playerList.get(Integer.parseInt(payload)).name());
        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            Platform.runLater(() -> {
                Main.setScene(RoomPage.scene);
            });
        });
    }

    private void colorHandler(String payload) {
        priorityColor = payload;
        playersCurrentCard.setText("Цвет: " + CardHelper.getColorString(payload));
    }

    private void lastCardHandler(String payload) {
        Card card = new Gson().fromJson(payload, Card.class);
        playersCurrentCard.setText(CardHelper.getCardString(card));
    }

    private void indexHandler(String payload) {
        index = payload;
    }

    private void cardHandler(String payload) {
        Card card = new Gson().fromJson(payload, Card.class);
        Button btn = new Button(CardHelper.getCardString(card));
        btn.setOnMouseClicked(mouseEvent -> connection.send("card", payload));
        cardsContainer.getChildren().add(btn);
    }

    private void deleteHandler(String payload) {
        Card card = new Gson().fromJson(payload, Card.class);
        cardsContainer.getChildren().removeIf((node) -> {
            if (node instanceof  Button button) {
                return button.getText().equals(CardHelper.getCardString(card));
            }
            return false;
        });
        colorChooseProceed(card);
    }

    private void colorChooseProceed(Card card) {
        if ("+4color".equals(card.type()) || "color".equals(card.type())) {
            cardsContainer.setDisable(true);
            colorContainer.setDisable(false);
        }
    }
}
