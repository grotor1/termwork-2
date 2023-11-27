package com.grotor.javafxcw.threads;

import com.grotor.javafxcw.services.History;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListenerThread extends Thread{
    private final BufferedReader reader;
    private TextArea history;
    private Socket socket;

    public ListenerThread(TextArea history, Socket socket) throws IOException {
        this.history = history;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = this.reader.readLine();

                new History(this.history).addHistory(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
