package com.conoftherings.playercards;

import java.util.HashMap;
import java.util.Map;

public enum CardType {
    HERO("Hero"),
    ALLY("Ally"),
    ATTACHMENT("Attachment"),
    EVENT("Event"),
    SIDE_QUEST("Side Quest"),
    CONTRACT("Contract");

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
