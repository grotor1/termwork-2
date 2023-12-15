package com.grotor.javafxcw.threads;

import com.grotor.javafxcw.event.EventHandler;

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
        this.init();
    }

    private void init() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                String[] split = message.split(":", 2);
                String event = split[0];
                String payload = split.length < 2 ? "" : split[1];

                System.out.println(message + " " + eventHandlers.containsKey(event));

                if (eventHandlers.containsKey(event)) {
                    eventHandlers.get(event).handle(payload);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String event, String payload) {
        writer.println(event + (payload.isEmpty() ? "" : ":") + payload);
    }

    public void addHandler(String event, EventHandler eventHandler) {
        eventHandlers.put(event, eventHandler);
    }

    public void removeHandler(String event) {
        eventHandlers.remove(event);
    }
}
