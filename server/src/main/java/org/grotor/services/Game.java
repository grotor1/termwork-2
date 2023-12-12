package org.grotor.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Game extends Thread {
    private static final int INITIAL_CARDS = 5;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean reversed = false;
    private String priorityColor;
    private Card lastCard;

    public Game(List<Client> clients) {
        this.players = clients.stream().map(Player::new).toList();
        this.start();
    }

    public void init() {
        lastCard = Card.getRandomCardInit();
        currentPlayerIndex = 0;
        for (Player player : players) {
            player.startGame();
        }

        for (Player player : players) {
            for (int j = 0; j < INITIAL_CARDS; j++) {
                player.addCard(Card.getRandomCard());
            }
        }

        for (int i = 0; i < players.size(); i++) {
            players.get(i).sendPlayersState(players);
            players.get(i).updateLastCard(lastCard);
            players.get(i).setIndex(i);
            players.get(i).sendCurrentPlayer(currentPlayerIndex);
        }
    }


    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        int nextPlayerIndex;
        if (reversed) {
            nextPlayerIndex = (currentPlayerIndex == 0 ? players.size() : currentPlayerIndex) - 1;
        } else {
            nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        return players.get(nextPlayerIndex);
    }

    public void makeTurn() {
        currentPlayerIndex = reversed ?
                (currentPlayerIndex == 0 ? players.size() : currentPlayerIndex) - 1 :
                (currentPlayerIndex + 1) % players.size();

        for (Player player : players) {
            player.sendPlayersState(players);
            System.out.println(priorityColor);
            if (priorityColor == null) {
                player.updateLastCard(lastCard);
            }
            player.sendCurrentPlayer(currentPlayerIndex);
        }
    }

    public void proceedCard(Card card) {
        String color;
        switch (card.getType()) {
            case "reverse":
                reversed = !reversed;
            case "+2":
                for (int i = 0; i < 2; i++) {
                    for (Player player : players) {
                        if (!player.equals(getCurrentPlayer())) {
                            player.addCard(Card.getRandomCard());
                        }
                    }
                }
                break;
            case "color":
                color = getCurrentPlayer().getColor();
                for (Player player : players) {
                    player.sendPriorityColor(color);
                }
                priorityColor = color;
                break;
            case "+4color":
                color = getCurrentPlayer().getColor();
                for (Player player : players) {
                    player.sendPriorityColor(color);
                }
                priorityColor = color;

                for (int i = 0; i < 4; i++) {
                    getNextPlayer().addCard(Card.getRandomCard());
                }
                break;
        }
    }

    public boolean isAnyCardAcceptable(Player player) {
        return player.getCards().stream().anyMatch(this::isCardAcceptable);
    }

    public boolean isCardAcceptable(Card card) {
        return priorityColor == null ? lastCard.compatible(card) && getCurrentPlayer().isInHand(card) : priorityColor.equals(card.getColor());
    }

    @Override
    public void run() {
        this.init();
        while (players.stream().noneMatch(Player::isEmptyHand)) {
            if (isAnyCardAcceptable(getCurrentPlayer())) {
                getCard();
            } else {
                getCurrentPlayer().addCard(Card.getRandomCard());
            }
            makeTurn();
        }

        Optional<Player> winner = players.stream().filter(Player::isEmptyHand).findAny();

        winner.ifPresent(w -> {
            int index = players.indexOf(w);
            players.forEach((player) -> player.sendWinner(index));
        });
    }

    private void getCard() {
        Card card = getCurrentPlayer().getCard();
        while (!isCardAcceptable(card)) {
            card = getCurrentPlayer().getCard();
        }
        getCurrentPlayer().deleteCard(card);
        priorityColor = null;
        lastCard = card;
        proceedCard(card);
    }
}
