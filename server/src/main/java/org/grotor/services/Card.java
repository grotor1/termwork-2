package org.grotor.services;

import java.util.Objects;
import java.util.Random;

public class Card {
    private static final String[] COLORS = new String[] {"red", "green", "yellow", "blue"};
    private static final String[] TYPES = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "reverse", "+2", "+4color", "color"};
    private String color;
    private String type;

    public Card(String color, String type) {
        this.color = color;
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean compatible(Card card) {
        return card.type.equals(this.type) || card.color.equals(this.color);
    }

    public static Card getRandomCard() {
        String color = COLORS[new Random().nextInt(COLORS.length)];
        String type = TYPES[new Random().nextInt(TYPES.length)];
        return new Card(color, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(color, card.color) && Objects.equals(type, card.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
