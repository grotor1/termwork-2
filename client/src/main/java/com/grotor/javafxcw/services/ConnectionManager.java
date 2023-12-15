package com.grotor.javafxcw.services;

import com.grotor.javafxcw.threads.Connection;

import java.io.IOException;
import java.net.Socket;

public class ConnectionManager {
    private Connection connection;
    private static final ConnectionManager connectionManager = new ConnectionManager();

    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    public ConnectionManager() {
        this.init();
    }

    public void init() {
        try {
            connection = new Connection(new Socket("localhost", 2345));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
