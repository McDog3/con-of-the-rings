package com.conoftherings.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.conoftherings.draft.Pack;
import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;

@Service
public class DraftPackServiceImpl implements DraftPackService {

    private final static String PACK_ID = "deckId";
    private final static String PACK_NAME = "name";
    private final static String PACK_USER_ID = "userId";
    private final static String CARDS = "cards";
    private final static String CARD = "card";
    private final static String CARD_COST = "cost";
    private final static String CARD_NAME = "name";
    private final static String CARD_SPHERE = "sphere";
    private final static String CARD_TYPE = "type";
    private final static String CARD_QUANTITY = "quantity";

    @Value("classpath:static/json/draft-packs.json")
    Resource resourceFile;

    @Override
    public void createDraft() {
        //TODO: DraftPackManager logic

        //Parse JSON/HTML

        InputStream resource = null;
        try {
            resource = resourceFile.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

            String packs = reader.lines().collect(Collectors.joining("\n"));
            System.out.println(packs);
            parseDraftPacks(packs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create Card objects from JSON/HTML

        //Build Pack objects from the Cards and JSON/HTML

        //Validate the full draft has enough cards and various other properties
    }

    private List<Pack> parseDraftPacks(String draftPacks) {
        BasicJsonParser parser = new BasicJsonParser();
        List<Pack> packs = new ArrayList<>();
        List<Map<String, Object>> packsJSON = parser.parseList(draftPacks).stream().map(object -> parser.parseMap((String)object)).collect(Collectors.toList());

        for (Map<String, Object> pack: packsJSON) {
            int packId = Integer.parseInt((String) pack.get(PACK_ID));
            String packName = (String) pack.get(PACK_NAME);
            String userId = (String) pack.get(PACK_USER_ID);
            List<Map<String, Object>> cardsJSON = ((List<Object>) pack.get(CARDS)).stream().map(card -> parser.parseMap((String) card)).collect(Collectors.toList());
            List<Card> cards = new ArrayList<>();

            for (Map<String, Object> card: cardsJSON) {
                Map<String, Object> cardDetails = (Map<String, Object>) card.get(CARD);
                int cost = Integer.parseInt((String) cardDetails.getOrDefault(CARD_COST, "0"));
                String cardName = (String) cardDetails.get(CARD_NAME);
                Sphere sphere = Sphere.valueOf((String) cardDetails.getOrDefault(CARD_SPHERE, "LEADERSHIP"));
                CardType cardType = CardType.determineFromDescription((String) cardDetails.getOrDefault(CARD_TYPE, "ALLY"));
                String image = "";
                Long totalQuantity = (Long) card.get(CARD_QUANTITY);

                for (int cardNum = 0; cardNum < totalQuantity.intValue(); cardNum++) {
                    cards.add(new Card(cost, cardName, sphere, cardType, image));
                }
            }
            packs.add(new Pack(packId, packName, userId, cards));
        }
        return packs;
    }
}
