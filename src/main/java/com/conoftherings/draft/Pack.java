package com.conoftherings.draft;

import java.util.List;

import com.conoftherings.playercards.Card;

public class Pack {

    private final long id;
    private final String userName;
    private final String packName;
    private final List<Card> cards;

    public Pack(long id, String userName, String packName, List<Card> cards) {
        this.id = id;
        this.userName = userName;
        this.packName = packName;
        this.cards = cards;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPackName() {
        return packName;
    }

    public List<Card> getCards() {
        return cards;
    }
}
