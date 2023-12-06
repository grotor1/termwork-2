package org.grotor.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private Integer id;
    private List<Client> clients = new CopyOnWriteArrayList<>();

    public Room(Integer id, Client host) {
        this.id = id;
        this.addClient(host);
        host.getConnection().addHandler("start", (payload) -> new Game(clients));
    }

    public void addClient(Client client) {
        clients.add(client);
        client.getConnection().send("connected", "");
        client.getConnection().addHandler("leave", (payload) -> clients.remove(client));
        updateCount();
    }

    public void updateCount() {
        clients.forEach((client) -> {
            client.getConnection().send("updateCount", String.valueOf(clients.size()));
        });
    }

    public Integer getId() {
        return id;
    }
}
