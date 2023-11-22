package org.grotor.controllers;

import org.grotor.event.EventHandler;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Connection extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Map<String, EventHandler> eventHandlers = new HashMap<>();

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                String event = message.split(":")[0];
                String payload = message.split(":", 2)[1];

                eventHandlers.get(event).handle(payload);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String event, String payload) {
        writer.println(event + ":" + payload);
    }

    public void addHandler(String event, EventHandler eventHandler) {
        eventHandlers.put(event, eventHandler);
    }
}
