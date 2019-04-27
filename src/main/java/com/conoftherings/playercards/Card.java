package com.conoftherings.playercards;

import java.net.URL;

public class Card {

    private final int cost;
    private final String name;
    private final Sphere sphere;
    private final CardType type;
    private final URL image;

    public Card(int cost, String name, Sphere sphere, CardType type, URL image) {
        this.cost = cost;
        this.name = name;
        this.sphere = sphere;
        this.type = type;
        this.image = image;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public Sphere getSphere() {
        return sphere;
    }

    public CardType getType() {
        return type;
    }

    public URL getImage() {
        return image;
    }
}
