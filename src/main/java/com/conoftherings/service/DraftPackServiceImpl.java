package com.conoftherings.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.conoftherings.draft.Pack;
import com.conoftherings.playercards.Card;
import com.conoftherings.playercards.CardType;
import com.conoftherings.playercards.Sphere;
import com.conoftherings.util.JSONUtil;

@Service
public class DraftPackServiceImpl implements DraftPackService {

    @Value("classpath:static/json/draft-packs.json")
    private Resource resourceFile;

    private Logger logger = LoggerFactory.getLogger(DraftPackServiceImpl.class);

    //TODO: To retrieve data directly from RingsDB - use their api:
    //located  - https://ringsdb.com/api/doc
    //examples - https://ringsdb.com/api/public/decklist/11924
    //examples - https://ringsdb.com/api/public/card/05001

    @Override
    public void createDraft(int playerCount) {
        //TODO: DraftPackManager logic

        //Parse JSON/HTML
//        Map<String, Object> deckJSON = getDeckList("11924");
//        Map<String, Object> cardJSON = getCardDetails("05001");

        InputStream resource;
        String packs = "";
        try {
            resource = resourceFile.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

            packs = reader.lines().collect(Collectors.joining("\n"));
            logger.debug(packs);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        //Create Card objects from JSON/HTML
        //Build Pack objects from the Cards and JSON/HTML
        List<Pack> allDraftPacks = JSONUtil.parseDraftPacks(packs);
        Collections.shuffle(allDraftPacks);

        //Validate the full draft has enough cards and various other properties
        List<Pack> draftPacks = determineUsedDraftPacks(allDraftPacks, playerCount);
    }



    //--------------------------------TODO: cut below from service?
//    private Map<String, Object> getDeckList(String deckId) {
//        //Implemented with RingsDB
//        final String uri = "http://ringsdb.com/api/public/decklist/" + deckId;
//
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
//
//        BasicJsonParser parser = new BasicJsonParser();
//        Map<String, Object> deckJSON = parser.parseMap(result);
//
//        return deckJSON;
//    }
//
//    private Map<String, Object> getCardDetails(String cardId) {
//
//        final String uri = "http://ringsdb.com/api/public/card/" + cardId;
//
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
//
//        BasicJsonParser parser = new BasicJsonParser();
//        Map<String, Object> cardJSON = parser.parseMap(result);
//
//        return cardJSON;
//
//    }
    //--------------------------------TODO: cut above from service?

    private List<Pack> determineUsedDraftPacks(List<Pack> allDraftPacks, int playerCount) {
        final int TOTAL_ATTEMPTS = 200;
        List<Pack> usedDraftPacks = new ArrayList<>();
        DraftStatus draftStatus = determineDraftStatus(usedDraftPacks, playerCount);
        int attempts = 0;
        while (draftStatus != DraftStatus.DRAFT_VALID && attempts++ < TOTAL_ATTEMPTS) {
            //remove existing packs from allDraftPacks listing
            allDraftPacks.removeAll(usedDraftPacks);
            //Do things
            switch (draftStatus) {
                case NEEDS_HEROES:
                    usedDraftPacks.addAll(addHeroesToDraftPacks(allDraftPacks));
                    break;
                case NEEDS_PLAYER_CARDS:
                    usedDraftPacks = addPlayerCardsToDraftPacks(allDraftPacks);
                    break;
                case NEEDS_SPHERE_BALANCE:
                    usedDraftPacks = addSphereBalanceToDraftPacks(allDraftPacks, usedDraftPacks);
                    break;
                case DRAFT_VALID:
                default:
                    break;
            }

            //FUTURE: do things to add/remove from draftPacks to continue balancing everything
            draftStatus = determineDraftStatus(usedDraftPacks, playerCount);
        }

        return usedDraftPacks;
    }

    private List<Pack> addHeroesToDraftPacks(List<Pack> allDraftPacks) {
        //Search for a deck with heroes, add it to the draft
        Optional<Pack> additionalDraftPack = allDraftPacks.stream().filter(pack -> pack.getCards().stream().anyMatch(card -> card.getType() == CardType.HERO)).findFirst();
        List<Pack> draftPacks = new ArrayList<>();
        if (additionalDraftPack.isPresent()) {
            draftPacks.add(additionalDraftPack.get());
        }
        return draftPacks;
    }

    private List<Pack> addPlayerCardsToDraftPacks(List<Pack> allDraftPacks) {
        //Search for a deck with player cards, add it to the draft
        Optional<Pack> additionalDraftPack = allDraftPacks.stream().filter(pack -> pack.getCards().stream().anyMatch(card -> card.getType() != CardType.HERO)).findFirst();
        List<Pack> draftPacks = new ArrayList<>();
        if (additionalDraftPack.isPresent()) {
            draftPacks.add(additionalDraftPack.get());
        }
        return draftPacks;
    }

    private List<Pack> addSphereBalanceToDraftPacks(List<Pack> allDraftPacks, List<Pack> usedDraftPacks) {
        //Determine the sphere with the lowest count
        Map<Sphere, Integer> cardCountsBySphere = new HashMap<>();
        for (Sphere sphere: Sphere.values()) {
            Long sphereCount = usedDraftPacks.stream().map(Pack::getCards).flatMap(List::stream).filter(card -> card.getSphere() == sphere).count();
            cardCountsBySphere.put(sphere, sphereCount.intValue());
        }
        //Then search for a deck with cards of that sphere
        Optional<Map.Entry<Sphere, Integer>> sphereCountEntry = cardCountsBySphere.entrySet().stream().sorted(Map.Entry.comparingByValue()).findFirst();
        List<Pack> draftPacks = new ArrayList<>();
        if (sphereCountEntry.isPresent()) {
            Sphere fewestCountSphere = sphereCountEntry.get().getKey();
            Optional<Pack> additionalDraftPack = allDraftPacks.stream().filter(pack -> pack.getCards().stream().anyMatch(card -> card.getSphere() == fewestCountSphere)).findFirst();
            if (additionalDraftPack.isPresent()) {
                draftPacks.add(additionalDraftPack.get());
            }
        }
        return draftPacks;
    }

    //Work out internal structure to determine "validity"
    private DraftStatus determineDraftStatus(List<Pack> draftPacks, int playerCount) {
        List<Card> allDraftCards = draftPacks.stream().map(Pack::getCards).flatMap(List::stream).collect(Collectors.toList());
        long totalCardCount = allDraftCards.size();

        //1 - We need 6[9?] * playerCount heroes
        long heroCards = allDraftCards.stream().filter(card -> card.getType() == CardType.HERO).count();
        if (heroCards < 6 * playerCount) {
            //we have failed at achieving enough heroes to pass the draft
            return DraftStatus.NEEDS_HEROES;
        }

        //2 - We need at least 70 * playerCount player cards
        long playerDeckCards = allDraftCards.stream().filter(card -> card.getType() != CardType.HERO).count();
        if (playerDeckCards < 70 * playerCount) {
            //we have failed at achieving enough player deck cards to pass the draft
            return DraftStatus.NEEDS_PLAYER_CARDS;
        }

        //3 - Each sphere should not have any less than 20% representation
        for (Sphere sphere: Sphere.values()) {
            long sphereCount = draftPacks.stream().map(Pack::getCards).flatMap(List::stream).filter(card -> card.getSphere() == sphere).count();
            if (sphereCount / totalCardCount < 0.2) {
                //we have failed at achieving a good sphere balance ratio!
                //FUTURE: do something about this
                return DraftStatus.NEEDS_SPHERE_BALANCE;
            }
        }

        return DraftStatus.DRAFT_VALID;
    }

    private enum DraftStatus {
        NEEDS_HEROES, NEEDS_PLAYER_CARDS, NEEDS_SPHERE_BALANCE, DRAFT_VALID;
    }

}
