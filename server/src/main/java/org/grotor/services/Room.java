package org.grotor.services;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Integer id;
    private List<Client> clients = new ArrayList<>();

    public Room(Integer id, Client host) {
        this.id = id;
        clients.add(host);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public Integer getId() {
        return id;
    }
}
