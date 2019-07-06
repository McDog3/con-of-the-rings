/**
 * DraftSettings
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-07-01
 */
package com.conoftherings.draft;

public class Settings {

    private final int playerCount;
    private final int minPlayerCardsPerPlayer;//PLAYER_CARD_MIN = 70;
    private final int minHeroCards;//HERO_CARD_MIN = 45;
    private final float minSphereBalanceRatio;//SPHERE_BALANCE_MIN = 0.18f;
    private final int maxPlayerCardDuplication;//MAX_PLAYER_CARD_DUPLICATES = 3;

    public Settings(int playerCount, int minPlayerCardsPerPlayer, int minHeroCards, float minSphereBalanceRatio, int maxPlayerCardDuplication) {
        this.playerCount = playerCount;
        this.minPlayerCardsPerPlayer = minPlayerCardsPerPlayer;
        this.minHeroCards = minHeroCards;
        this.minSphereBalanceRatio = minSphereBalanceRatio;
        this.maxPlayerCardDuplication = maxPlayerCardDuplication;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getMinPlayerCardsPerPlayer() {
        return minPlayerCardsPerPlayer;
    }

    public int getMinHeroCards() {
        return minHeroCards;
    }

    public float getMinSphereBalanceRatio() {
        return minSphereBalanceRatio;
    }

    public int getMaxPlayerCardDuplication() {
        return maxPlayerCardDuplication;
    }
}
