package com.conoftherings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conoftherings.draft.Draft;
import com.conoftherings.draft.Settings;
import com.conoftherings.service.DraftPackService;

@RestController
public class DraftRestController {

    private static final int PLAYER_CARD_MIN = 70;
    private static final int HERO_CARD_MIN = 45;
    private static final float SPHERE_BALANCE_MIN = 0.18f;
    private static final int MAX_PLAYER_CARD_DUPLICATES = 3;

    @Autowired
    DraftPackService draftPackService;

    @RequestMapping(value = "/draft/create", method = RequestMethod.GET)
    public Draft draft(@RequestParam("playerCount") Integer playerCount) {
        Draft draft = draftPackService.createDraft(new Settings(playerCount, PLAYER_CARD_MIN, HERO_CARD_MIN, SPHERE_BALANCE_MIN, MAX_PLAYER_CARD_DUPLICATES));
        return draft;
    }
}
