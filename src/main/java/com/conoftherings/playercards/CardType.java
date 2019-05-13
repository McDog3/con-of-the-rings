package com.conoftherings.playercards;

import java.util.HashMap;
import java.util.Map;

public enum CardType {
    HERO("hero"),
    ALLY("ally"),
    ATTACHMENT("attachment"),
    EVENT("event"),
    SIDE_QUEST("player-side-quest"),
    CONTRACT("contract");

    private final String description;

    CardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private static final Map<String, CardType> cardTypesByDescription = new HashMap<>();
    static {
        for (CardType cardType : CardType.values()) {
            cardTypesByDescription.put(cardType.getDescription(), cardType);
        }
    }

    public static CardType determineFromDescription(String description) {
        return cardTypesByDescription.get(description);
    }
}
