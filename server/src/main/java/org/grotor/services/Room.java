package org.grotor.services;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Integer id;
    private List<Client> clients = new ArrayList<>();

    public Room(Integer id, Client host) {
        this.id = id;
        this.addClient(host);
        host.getConnection().addHandler("start", (payload) -> new Game(clients));
    }

    public void addClient(Client client) {
        clients.add(client);
        client.getConnection().addHandler("leave", (payload) -> clients.remove(client));
    }

    public Integer getId() {
        return id;
    }
}
