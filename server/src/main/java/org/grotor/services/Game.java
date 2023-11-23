package org.grotor.services;

import java.util.Collections;
import java.util.List;

public class Game extends Thread {
    private static int INITIAL_CARDS = 5;
    private List<Player> players;
    private Player currentPlayer;
    private Card lastCard;

    public Game(List<Client> clients) {
        this.players = clients.stream().map(Player::new).toList();
        this.start();
    }

    public void init() {
        lastCard = Card.getRandomCard();
        currentPlayer = players.get(0);

        for (int i = 0; i < INITIAL_CARDS; i++) {
            for (Player player: players) {
                player.addCard(Card.getRandomCard());
            }
        }
    }

    @Override
    public void run() {
        this.init();
        while (true) {
            for (Player player: players) {
                Card card = player.getCard();
                while (lastCard.compatible(card) && !player.deleteCard(card)) {
                    card = player.getCard();
                }

                lastCard = card;
            }
        }
    }
}
