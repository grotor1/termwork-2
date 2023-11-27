package com.grotor.javafxcw.services;

import javafx.scene.control.TextArea;

public class History {
    private TextArea history;

    public History(TextArea history) {
        this.history = history;
    }

    public void addHistory(String message) {
        String history = this.history.getText();

        System.out.println(message);

        this.history.setText(history + "\n\n" + message);
    }
}
