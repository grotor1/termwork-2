package com.grotor.javafxcw.controller;

import com.grotor.javafxcw.Main;
import com.grotor.javafxcw.pages.GamePage;
import com.grotor.javafxcw.pages.MainPage;
import com.grotor.javafxcw.pages.RoomPage;
import com.grotor.javafxcw.services.ConnectionManager;
import com.grotor.javafxcw.threads.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class RoomController {
    public Connection connection = ConnectionManager.getInstance().getConnection();


    public Label roomIdLabel;
    public Label playersCountLabel;
    public Button leaveButton;
    public GridPane controlContainer;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        Platform.runLater(() -> {
            try {
                roomIdLabel.setText("Номер комнаты: " + this.code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(String playersCount) {
        this.playersCount = playersCount;
        Platform.runLater(() -> playersCountLabel.setText("Количество участников: " + this.playersCount));
    }

    public void setStartButton(boolean startButton) {
        this.startButton = startButton;

        if (startButton) {
            Button button = new Button("Начать");
            controlContainer.add(button, 0, 1);

            button.setOnMouseClicked(mouseEvent -> {
                connection.send("start", "");
            });
        } else {
            if (controlContainer.getChildren().size() == 2) {
                controlContainer.getChildren().remove(1);
            }
        }
    }

    @FXML
    private void initialize() {
        leaveButton.setOnMouseClicked(mouseEvent -> {
            connection.send("leave", "");
            startButton = false;
            Main.setScene(MainPage.scene);
        });

        connection.addHandler("started", (payload) -> {
            GameController gameController = GamePage.fxmlLoader.getController();
            gameController.init();
            Platform.runLater(() -> Main.setScene(GamePage.scene));
        });
    }

    private String code;
    private String playersCount;
    private boolean startButton;
}
