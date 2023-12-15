package com.grotor.javafxcw.helpers;

import com.grotor.javafxcw.entity.Card;

public class CardHelper {
    public static String getColorString(String color) {
        return switch (color) {
            case "red" -> "Красный";
            case "green" -> "Зеленый";
            case "yellow" -> "Желтый";
            case "blue" -> "Синий";
            default -> color;
        };
    }

    public static String getTypeString(String type) {
        return switch (type) {
            case "reverse" -> "смена направления";
            case "+4color" -> "Смена цвета +4";
            case "color" -> "Смена цвета";
            default -> type;
        };
    }

    public static boolean isWithColor(String type) {
        return !"+4color".equals(type) && !"color".equals(type);
    }

    public static String getCardString(Card card) {
        return (isWithColor(card.type()) ? (getColorString(card.color()) + " ") : " ") + getTypeString(card.type());
    }
}
