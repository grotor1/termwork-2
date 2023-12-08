package com.grotor.javafxcw.services;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class History {
    private TextArea history;

    public History(TextArea history) {
        this.history = history;
    }

    public void addHistory(String message) {
        Platform.runLater(() -> {
            String history = this.history.getText();

            this.history.setText(history + "\n\n" + message);
        });
    }
}
