package org.grotor.services;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    public Client getClient() {
        return client;
    }

    private Client client;

    public List<Card> getCards() {
        return cards;
    }

    private List<Card> cards = new CopyOnWriteArrayList<>();

    public Player(Client client) {
        this.client = client;
    }

    public synchronized Card getCard() {
        AtomicReference<Card> card = new AtomicReference<>();
        client.getConnection().addHandler("card", (payload) -> {
            synchronized (this) {
                card.set(new Gson().fromJson(payload, Card.class));
                notify();
            }
        });
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.getConnection().removeHandler("card");
        return card.get();
    }

    public synchronized String getColor() {
        AtomicReference<String> color = new AtomicReference<>();
        client.getConnection().addHandler("color", (payload) -> {
            synchronized (this) {
                color.set(payload);
                notify();
            }
        });
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.getConnection().removeHandler("color");
        return color.get();
    }

    public boolean isEmptyHand() {
        return cards.isEmpty();
    }

    public void sendCurrentPlayer(int index) {
        client.getConnection().send("player", String.valueOf(index));
    }

    public void sendPlayersState(List<Player> players) {
        List<Map<String, String>> res = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Map<String, String> p = new HashMap<>();
            p.put("index", String.valueOf(i));
            p.put("name", player.getClient().getName());
            p.put("cardCount", String.valueOf(player.getCards().size()));
            res.add(p);
        }

        client.getConnection().send("players", new Gson().toJson(res));
    }

    public void sendWinner(int index) {
        client.getConnection().send("winner", String.valueOf(index));
    }

    public void sendPriorityColor(String color) {
        client.getConnection().send("color", color);
    }

    public void updateLastCard(Card card) {
        client.getConnection().send("lastCard", String.valueOf(card));
    }

    public void setIndex(int index) {
        client.getConnection().send("index", String.valueOf(index));
    }

    public void startGame() {
        client.getConnection().send("started", "");
    }

    public void addCard(Card card) {
        client.getConnection().send("card", String.valueOf(card));
        cards.add(card);
    }

    public boolean isInHand(Card card) {
        return cards.contains(card);
    }

    public void deleteCard(Card card) {
        client.getConnection().send("delete", String.valueOf(card));
        cards.remove(card);
    }
}
