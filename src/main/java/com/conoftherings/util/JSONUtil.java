/**
 * JSONUtil
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-05-12
 */
package com.conoftherings.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Component;

import com.conoftherings.draft.Pack;
import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;

@Component
public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    private static final String PACK_ID = "deckId";
    private static final String PACK_NAME = "name";
    private static final String PACK_USER_ID = "userId";
    private static final String CARDS = "cards";
    private static final String CARD = "card";
    private static final String CARD_COST = "cost";
    private static final String CARD_NAME = "name";
    private static final String CARD_SPHERE = "sphere";
    private static final String CARD_TYPE = "type";
    private static final String CARD_QUANTITY = "quantity";
    private static final String CARD_IMAGE = "image";

    public List<Pack> parseDraftPacks(String draftPacks) {
        BasicJsonParser parser = new BasicJsonParser();
        List<Pack> packs = new ArrayList<>();
        List<Map<String, Object>> packsJSON = parser.parseList(draftPacks).stream().map(object -> parser.parseMap((String)object)).collect(Collectors.toList());

        for (Map<String, Object> pack: packsJSON) {
            long packId = (Long) pack.get(PACK_ID);
            String packName = (String) pack.get(PACK_NAME);
            String userId = ((Long) pack.get(PACK_USER_ID)).toString();//TODO: Need to change how we get user information from id's...
            List<Map<String, Object>> cardsJSON = ((List<Object>) pack.get(CARDS)).stream().map(card -> parser.parseMap((String) card)).collect(Collectors.toList());
            List<Card> cards = new ArrayList<>();
            for (Map<String, Object> card: cardsJSON) {
                Map<String, Object> cardDetails = (Map<String, Object>) card.get(CARD);
                CardType cardType = CardType.determineFromDescription((String) cardDetails.get(CARD_TYPE));
                String cardName = (String) cardDetails.get(CARD_NAME);
                Sphere sphere = Sphere.determineFromDescription((String) cardDetails.get(CARD_SPHERE));
                String image = (String) cardDetails.get(CARD_IMAGE);
                Long totalQuantity = (Long) card.get(CARD_QUANTITY);
                int cost = determineCost((String) cardDetails.get(CARD_COST), cardType);
                for (int cardNum = 0; cardNum < totalQuantity.intValue(); cardNum++) {
                    cards.add(new Card(cost, cardName, sphere, cardType, image));
                }
            }
            packs.add(new Pack(packId, userId, packName, cards));
        }
        return packs;
    }

    private int determineCost(String costString, CardType cardType) {
        int cost = 0;
        if (cardType != CardType.HERO) {
            try {
                cost = Integer.parseInt(costString);
            } catch (NumberFormatException nfe) {
                if (costString.contains("X") || costString.contains("x")) {
                    logger.debug("Cost is variable, leaving as 0");
                } else {
                    logger.error("Cannot parse cost into string", nfe);
                }
            }
        }
        return cost;
    }
}
