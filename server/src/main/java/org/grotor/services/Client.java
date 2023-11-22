package org.grotor.services;

import org.grotor.controllers.Connection;

public class Client {
    private Connection connection;
    private String name;

    public Client(Connection connection) {
        this.connection = connection;

        connection.addHandler("name", (payload) -> this.name = payload);
        connection.addHandler("create", (payload) -> RoomManager.getInstance().createRoom(this));
        connection.addHandler("connect", (payload -> RoomManager.getInstance().getRoom(Integer.parseInt(payload)).addClient(this)));
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }
}
