package org.grotor.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RoomManager {
    private static RoomManager instance;

    private Map<Integer, Room> roomList = new HashMap<>();

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public void createRoom(Client host) {
        Random rm = new Random();
        int id;
        do {
            id = rm.nextInt(100000, 1000000);
        } while (!roomList.containsKey(id));
        roomList.put(id, new Room(id, host));
    }

    public Room getRoom(Integer id) {
        return roomList.get(id);
    }
}
