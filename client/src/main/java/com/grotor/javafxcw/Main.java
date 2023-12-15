package com.grotor.javafxcw;

import com.grotor.javafxcw.pages.MainPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;


        primaryStage.setTitle("Uno");
        primaryStage.setScene(MainPage.scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}