/**
 * DraftPoolStatistics
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-05-11
 */
package com.conoftherings.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;

import com.conoftherings.draft.Pack;
import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;

import javafx.util.Pair;

public class DraftPoolStatistics {

    private final static String DRAFT_LOCATION = "/static/json/draft-packs.json";

    private static Logger logger = LoggerFactory.getLogger(DraftPoolStatistics.class);

    private final static Function<Sphere, Predicate<Card>> SPHERE_PREDICATE_FUNCTION = sphere -> {
        Predicate<Card> cardPredicate = card -> card.getSphere() == sphere;
        return cardPredicate;
    };

    //We need 70 player cards + 5 heroes per player in the draft -
    //Maximum 4 players = 280 player cards (9*35=315 total) + 20 heroes (9*5=45 total)

    //Hero distribution by sphere
    //<table><th><td>Leadership</td><td>Lore</td><td>

    public static void main(String[] args) {
        //Retrieve the draft-packs.json
        String packs = retrieveDraftPacksJson();
        //Parse the json into Pack/Card objects
        List<Pack> allDraftPacks = JSONUtil.parseDraftPacks(packs);

        //Analysis on all pack cards
        List<Card> totalCards = allDraftPacks.stream().map(Pack::getCards).flatMap(List::stream).collect(Collectors.toList());

        int totalCount = totalCards.size();
        System.out.println("Overall Card Count: " + totalCount + " (includes duplicates)");

        //1. Sphere distribution
        Map<Sphere, Statistic> overallSphereCountStats = determineSphereStatistic(totalCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallSphereCountStats.get(sphere);
            System.out.println("OVERALL - Sphere: " + sphere + "  Cards: " + sphereStat.getCategoryTotal() + " Percentage of total cards: " + sphereStat.getPercentage() + "%");
        }

        //2. Hero / Player card totals
        List<Card> heroCards = totalCards.stream().filter(card -> card.getType() == CardType.HERO).collect(Collectors.toList());
        List<Card> playerCards = totalCards.stream().filter(card -> card.getType() != CardType.HERO).collect(Collectors.toList());

        Map<Sphere, Statistic> overallHeroCountStats = determineSphereStatistic(heroCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallHeroCountStats.get(sphere);
            System.out.println("HEROES - Sphere: " + sphere + "  Heroes: " + sphereStat.getCategoryTotal() + " Percentage of total heroes: " + sphereStat.getPercentage() + "%");
        }
        Map<Sphere, Statistic> overallPlayerCountStats = determineSphereStatistic(playerCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallPlayerCountStats.get(sphere);
            System.out.println("PLAYER - Sphere: " + sphere + "  Non-hero cards: " + sphereStat.getCategoryTotal() + " Percentage of total non-hero cards: " + sphereStat.getPercentage() + "%");
        }

        //Getting more granular
        List<Card> allyCards = totalCards.stream().filter(card -> card.getType() == CardType.ALLY).collect(Collectors.toList());
        List<Card> attachmentCards = totalCards.stream().filter(card -> card.getType() == CardType.ATTACHMENT).collect(Collectors.toList());
        List<Card> eventCards = totalCards.stream().filter(card -> card.getType() == CardType.EVENT).collect(Collectors.toList());
        List<Card> sideQuestCards = totalCards.stream().filter(card -> card.getType() == CardType.SIDE_QUEST).collect(Collectors.toList());

        Map<Sphere, Statistic> overallAllyCountStats = determineSphereStatistic(allyCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallAllyCountStats.get(sphere);
            System.out.println("ALLY - Sphere: " + sphere + "  Allies: " + sphereStat.getCategoryTotal() + " Percentage of total allies: " + sphereStat.getPercentage() + "%");
        }
        Map<Sphere, Statistic> overallAttachmentCountStats = determineSphereStatistic(attachmentCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallAttachmentCountStats.get(sphere);
            System.out.println("ATTACHMENT - Sphere: " + sphere + "  Attachments: " + sphereStat.getCategoryTotal() + " Percentage of total attachments: " + sphereStat.getPercentage() + "%");
        }
        Map<Sphere, Statistic> overallEventCountStats = determineSphereStatistic(eventCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallEventCountStats.get(sphere);
            System.out.println("EVENT - Sphere: " + sphere + "  Events: " + sphereStat.getCategoryTotal() + " Percentage of total events: " + sphereStat.getPercentage() + "%");
        }
        Map<Sphere, Statistic> overallSideQuestCountStats = determineSphereStatistic(sideQuestCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallSideQuestCountStats.get(sphere);
            System.out.println("SIDE QUEST - Sphere: " + sphere + "  Side Quests: " + sphereStat.getCategoryTotal() + " Percentage of total side quests: " + sphereStat.getPercentage() + "%");
        }


        //3. Average cost?
        //TODO: The Statistic class isn't set up for this...
        Map<Sphere, Statistic> overallPlayerSphereCostStats = determineCostStatistic(playerCards, SPHERE_PREDICATE_FUNCTION);
        for (Sphere sphere : Sphere.values()) {
            Statistic sphereStat = overallPlayerSphereCostStats.get(sphere);
            System.out.println("PLAYER - Sphere: " + sphere + "  Average cost: " + sphereStat.getCategoryTotal());
        }


        //4. Uniqueness?

        //5. Most common cards per sphere? per type?


        Long totalHeroes = totalCards.stream().filter(card -> card.getType() == CardType.HERO).count();
        Long distinctHeroes = totalCards.stream().filter(card -> card.getType() == CardType.HERO).distinct().count();

        Long totalPlayer = totalCards.stream().filter(card -> card.getType() != CardType.HERO).count();
        Long distinctPlayer = totalCards.stream().filter(card -> card.getType() != CardType.HERO).distinct().count();

        System.out.println("Total heroes: " + totalHeroes + " Distinct Heroes: " + distinctHeroes);
        System.out.println("Total player: " + totalPlayer + " Distinct player: " + distinctPlayer);

    }

    private static Map<Sphere, Statistic> determineSphereStatistic(List<Card> cards, Function<Sphere, Predicate<Card>> spherePredicateFunction) {
        int totalCount = cards.size();
        Map<Sphere, Statistic> overallSphereCountStats = new HashMap<>();
        for (Sphere sphere : Sphere.values()) {
            Predicate<Card> cardPredicate = spherePredicateFunction.apply(sphere);
            Long count = cards.stream().filter(cardPredicate).count();
            overallSphereCountStats.put(sphere, new Statistic(totalCount, count.intValue(), "Overall " + sphere + " Count"));
        }
        return overallSphereCountStats;
    }

    private static Map<Sphere, Statistic> determineCostStatistic(List<Card> cards, Function<Sphere, Predicate<Card>> spherePredicateFunction) {
        int totalCount = cards.size();
        Map<Sphere, Statistic> overallSphereCountStats = new HashMap<>();
        for (Sphere sphere : Sphere.values()) {
            Predicate<Card> cardPredicate = spherePredicateFunction.apply(sphere);
            Double count = cards.stream().filter(cardPredicate).mapToInt(Card::getCost).average().getAsDouble();
            overallSphereCountStats.put(sphere, new Statistic(totalCount, count.intValue(), "Overall " + sphere + " Cost"));
        }
        return overallSphereCountStats;
    }

    private static String retrieveDraftPacksJson() {
        InputStream is = DraftPoolStatistics.class.getResourceAsStream(DRAFT_LOCATION);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String packs = reader.lines().collect(Collectors.joining("\n"));
        return packs;
    }

    private static class Statistic {
        private int overallTotal;
        private int categoryTotal;
        private String categoryName;

        protected Statistic(int overallTotal, int categoryTotal, String categoryName) {
            this.overallTotal = overallTotal;
            this.categoryTotal = categoryTotal;
            this.categoryName = categoryName;
        }

        public int getOverallTotal() {
            return overallTotal;
        }

        public int getCategoryTotal() {
            return categoryTotal;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public long getPercentage() {
            return Math.round((double) this.categoryTotal / this.overallTotal * 100);
        }
    }
}
