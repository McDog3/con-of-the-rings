/**
 * BuildDraftJSON
 * Copyright (c) 2019, FastBridge Learning LLC
 * Created on 2019-05-04
 */
package com.conoftherings.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.GsonBuilder;

/**
 * This is a utility class to rebuild the src/main/resources/static/json/draft-packs.json
 * <p/>
 * Draft packs are assembled using a deck id given on RingsDB, stored in
 * draft.properties. Those deck id's are used to call out to the RingsDB API
 * to retrieve the information related to each card and deck. This allows flexibility
 * to update draft packs through RingsDB and use this utility to synchronize our
 * draft-packs.json with that information.
 *
 * @see <a href=https://ringsdb.com/api>RingsDB API</a>
 *
 * @author benbickel
 */
public class BuildDraftJSON {

    private final static String RINGS_DB_VIEW_DECK_URL = "https://ringsdb.com/deck/view/";
    private final static String RINGS_DB_VIEW_CARD_URL = "http://ringsdb.com/api/public/card/";
    private final static String DRAFT_PROPERTIES_LOCATION = "/draft.properties";
    private final static String DRAFT_LOCATION = "src/main/resources/static/json/";
    private final static String DECK_VIEW_START = "app.deck.init\\(\\{";
    private final static Pattern DECK_VIEW_PATTERN = Pattern.compile(DECK_VIEW_START);
    private final static int DECK_VIEW_START_OFFSET = 14;
    private final static String DECK_VIEW_END = "});";


    public static void main(String[] args) {
        List<String> allDeckIds = retrieveAllDeckIds();
        JSONArray draftPackArray = new JSONArray();
        Map<String, JSONObject> cardCache = new HashMap<>();//A cache allows us to not call out to RingsDB for duplicate cards

        for (String deckId: allDeckIds) {
            JSONObject deckJSON = retrieveDeckJSON(deckId);
            if (deckJSON == null) {
                continue;
            }
            List<JSONObject> cardSlotsJSON = retrieveCardSlotsJSON(deckJSON, cardCache);
            JSONObject draftPackJson = createDraftPackJson(deckJSON, cardSlotsJSON);
            draftPackArray.add(draftPackJson);
        }

        saveDraftPacksToFile(draftPackArray);
	}

	private static List<String> retrieveAllDeckIds() {
        List<String> deckIds = new ArrayList<>();
        Properties draftProperties = new Properties();
        InputStream inputStream = BuildDraftJSON.class.getResourceAsStream(DRAFT_PROPERTIES_LOCATION);
        try {
            draftProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load draft properties file", e);
        }

        List<String> userIds = parseStringArray(draftProperties.getProperty("user.ids"));
        for (String userId: userIds) {
            List<String> userDeckIds = parseStringArray(draftProperties.getProperty("user." + userId + ".packs"));
            deckIds.addAll(userDeckIds);
        }
        return deckIds;
    }

	private static void saveDraftPacksToFile(JSONArray draftPackArray) {
        String outputJSON = new GsonBuilder().setPrettyPrinting().create().toJson(draftPackArray);

        try (FileWriter file = new FileWriter(DRAFT_LOCATION + "draft-packs.json")) {
            file.write(outputJSON);
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while writing JSON to file", e);
        }
    }

    private static JSONObject retrieveDeckJSON(String deckId) {
        final String uri = RINGS_DB_VIEW_DECK_URL + deckId;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JSONObject jsonObject = null;
        Matcher matcher = DECK_VIEW_PATTERN.matcher(result);

        if (matcher.find()) {
            int startIndex = matcher.start() + DECK_VIEW_START_OFFSET;
            String resultSubstring = result.substring(startIndex);
            int endIndex = resultSubstring.indexOf(DECK_VIEW_END) + 1;
            String deckResult = resultSubstring.substring(0, endIndex);
            jsonObject = parseString(deckResult);
        }
        return jsonObject;
    }

    private static List<JSONObject> retrieveCardSlotsJSON(JSONObject draftPack, Map<String, JSONObject> cardCache) {
        List<JSONObject> cardsJSON = new ArrayList<>();
        JSONObject slots = (JSONObject) draftPack.get(RingsDBCodes.DECK_SLOTS);
        Set<Map.Entry<String, Integer>> cardEntries = slots.entrySet();

        for (Map.Entry<String, Integer> cardEntry: cardEntries) {
            JSONObject cardJSON;
            String cardId = cardEntry.getKey();

            if (cardCache.containsKey(cardId)) {
                cardJSON = cardCache.get(cardId);
            } else {
                String uri = RINGS_DB_VIEW_CARD_URL + cardEntry.getKey();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(uri, String.class);

                cardJSON = parseString(result);
                cardCache.put(cardId, cardJSON);
            }
            cardsJSON.add(cardJSON);
        }
        return cardsJSON;
    }

    private static JSONObject createDraftPackJson(JSONObject deckJSON, List<JSONObject> cardSlotsJSON) {
        JSONObject slots = (JSONObject) deckJSON.get(RingsDBCodes.DECK_SLOTS);
        JSONArray slotsArray = new JSONArray();

        for (JSONObject cardSlot: cardSlotsJSON) {
            //create card object
            JSONObject card = new JSONObject();
            card.put(DraftCodes.PACK_NAME, cardSlot.get(RingsDBCodes.PACK_NAME));
            card.put(DraftCodes.CARD_ID, cardSlot.get(RingsDBCodes.CARD_ID));
            card.put(DraftCodes.CARD_NAME, cardSlot.get(RingsDBCodes.CARD_NAME));
            card.put(DraftCodes.CARD_TYPE, cardSlot.get(RingsDBCodes.CARD_TYPE));
            card.put(DraftCodes.CARD_COST, cardSlot.get(RingsDBCodes.CARD_COST));
            card.put(DraftCodes.CARD_SPHERE, cardSlot.get(RingsDBCodes.CARD_SPHERE));
            card.put(DraftCodes.CARD_LIMIT, cardSlot.get(RingsDBCodes.CARD_LIMIT));
            card.put(DraftCodes.CARD_URL, cardSlot.get(RingsDBCodes.CARD_URL));
            card.put(DraftCodes.CARD_UNIQUENESS, cardSlot.get(RingsDBCodes.CARD_UNIQUENESS));

            //create deck slot object
            JSONObject slot = new JSONObject();
            slot.put(DraftCodes.SLOT_CARD, card);
            slot.put(DraftCodes.SLOT_QUANTITY, slots.get(card.get(DraftCodes.CARD_ID)));

            slotsArray.add(slot);
        }

        JSONObject draftPack = new JSONObject();
        draftPack.put(DraftCodes.DECK_NAME, deckJSON.get(RingsDBCodes.DECK_NAME));
        draftPack.put(DraftCodes.DECK_ID, deckJSON.get(RingsDBCodes.DECK_ID));
        draftPack.put(DraftCodes.DECK_CREATOR, deckJSON.get(RingsDBCodes.DECK_CREATOR));
        draftPack.put(DraftCodes.DECK_CARDS, slotsArray);
        return draftPack;
    }

    private static List<String> parseStringArray(String propertyString) {
        return Arrays.asList(propertyString.split(","));
    }

    private static JSONObject parseString(String result) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(result);
        } catch (ParseException e) {
            throw new RuntimeException("Could not parse JSON from object: " + result, e);
        }
        return jsonObject;
    }

    private class RingsDBCodes {
        public static final String PACK_NAME = "pack_name";
        public static final String CARD_ID = "code";
        public static final String CARD_NAME = "name";
        public static final String CARD_TYPE = "type_code";
        public static final String CARD_COST = "cost";
        public static final String CARD_SPHERE = "sphere_code";
        public static final String CARD_LIMIT = "deck_limit";
        public static final String CARD_URL = "url";
        public static final String CARD_UNIQUENESS = "is_unique";
        public static final String DECK_NAME = "name";
        public static final String DECK_ID = "id";
        public static final String DECK_CREATOR = "user_id";
        public static final String DECK_SLOTS = "slots";
    }

    private class DraftCodes {
        public static final String PACK_NAME = "set";
        public static final String CARD_ID = "id";
        public static final String CARD_NAME = "name";
        public static final String CARD_TYPE = "type";
        public static final String CARD_COST = "cost";
        public static final String CARD_SPHERE = "sphere";
        public static final String CARD_LIMIT = "limit";
        public static final String CARD_URL = "image";
        public static final String CARD_UNIQUENESS = "unique";
        public static final String DECK_NAME = "name";
        public static final String DECK_ID = "deckId";
        public static final String DECK_CREATOR = "userId";
        public static final String DECK_CARDS = "cards";
        public static final String SLOT_CARD = "card";
        public static final String SLOT_QUANTITY = "quantity";
    }
}
