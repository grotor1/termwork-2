package com.grotor.javafxcw.controller;

import com.grotor.javafxcw.services.History;
import com.grotor.javafxcw.threads.ListenerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class DialogController {
    @FXML
    private TextArea history;
    @FXML
    private TextField input;

    private Socket socket;
    private PrintWriter writer;

    @FXML
    private void initialize() {
        try {
            this.socket = new Socket("localhost", 2345);
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            new ListenerThread(history, socket).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void sendMessage() {
        String message = this.input.getText();

        writer.println(message);
        new History(this.history).addHistory(message);
    }
}
