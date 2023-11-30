package org.grotor.services;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    private Client client;
    private List<Card> cards = new ArrayList<>();

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

    public String getColor() {
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

    public boolean addCard(Card card) {
        client.getConnection().send("card", String.valueOf(card));
        return cards.add(card);
    }

    public boolean deleteCard(Card card) {
        return cards.remove(card);
    }
}
