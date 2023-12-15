package com.grotor.javafxcw.controller;

import com.grotor.javafxcw.Main;
import com.grotor.javafxcw.pages.RoomPage;
import com.grotor.javafxcw.services.ConnectionManager;
import com.grotor.javafxcw.threads.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainController {
    public Connection connection = ConnectionManager.getInstance().getConnection();


    public Button createRoomBtn;
    public Button enterRoomBtn;
    public TextField roomId;
    public Button nameBtn;
    public TextField name;

    @FXML
    private void initialize() {
        createRoomBtn.setOnMouseClicked(mouseEvent -> createHandle());

        enterRoomBtn.setOnMouseClicked(mouseEvent -> connectHandler());

        nameBtn.setOnMouseClicked(mouseEvent -> nameHandler());
    }

    public void createHandle() {
        RoomController roomController = RoomPage.fxmlLoader.getController();

        roomController.setStartButton(true);

        connection.addHandler("created", (payload) -> {
            Platform.runLater(() -> roomController.setCode(payload));
        });

        connectedHandler();

        connection.send("create", "");
    }

    public void connectedHandler() {
        RoomController roomController = RoomPage.fxmlLoader.getController();

        connection.addHandler("connected", (payload) -> {
            Platform.runLater(() -> Main.setScene(RoomPage.scene));
            connection.removeHandler("connected");
        });

        connection.addHandler("updateCount", (payload) -> {
            Platform.runLater(() -> roomController.setPlayersCount(payload));
        });
    }

    public void connectHandler() {
        RoomController roomController = RoomPage.fxmlLoader.getController();

        roomController.setStartButton(false);

        roomController.setCode(roomId.getText());

        connectedHandler();

        connection.send("connect", roomId.getText());
    }

    public void nameHandler() {
        connection.send("name", name.getText());
    }
}
