package com.conoftherings.playercards;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Sphere {
    LEADERSHIP("leadership"), LORE("lore"), SPIRIT("spirit"), TACTICS("tactics"), NEUTRAL("neutral");

    private String description;

    Sphere(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private static final Map<String, Sphere> spheresByDescription;
    static {
        spheresByDescription = Arrays.stream(Sphere.values()).collect(Collectors.toMap(Sphere::getDescription, Function.identity()));
    }

    public static Sphere determineFromDescription(String description) {
        return spheresByDescription.get(description);
    }

}
