package org.grotor.services;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    private Client client;
    private List<Card> cards;

    public Player(Client client) {
        this.client = client;
    }

    public Card getCard() {
        AtomicReference<Card> card = new AtomicReference<>();
        client.getConnection().addHandler("card", (payload) -> {
            card.set(new Gson().fromJson(payload, Card.class));
            notify();
        });
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.getConnection().removeHandler("card");
        return card.get();
    }


    public boolean isExistInHand(Card card) {
        return cards.contains(card);
    }

    public boolean addCard(Card card) {
        return cards.add(card);
    }

    public boolean deleteCard(Card card) {
        return cards.remove(card);
    }
}
