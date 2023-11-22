package org.grotor.controllers;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2345)) {
            ConnectionManager connectionManager = new ConnectionManager(serverSocket);
            connectionManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
