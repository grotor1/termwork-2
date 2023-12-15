package com.grotor.javafxcw.pages;

import com.grotor.javafxcw.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class GamePage {
    public static FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("game-view.fxml"));

    public static Scene scene;

    static {
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
