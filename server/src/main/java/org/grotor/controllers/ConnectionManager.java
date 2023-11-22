package org.grotor.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    private ServerSocket serverSocket;

    public ConnectionManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Connection(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
