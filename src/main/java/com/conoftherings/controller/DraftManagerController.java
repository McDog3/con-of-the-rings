package com.conoftherings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.conoftherings.draft.Draft;
import com.conoftherings.draft.Settings;
import com.conoftherings.service.DraftPackService;

@Controller
public class DraftManagerController {

    private static final int PLAYER_CARD_MIN = 70;
    private static final int HERO_CARD_MIN = 45;
    private static final float SPHERE_BALANCE_MIN = 0.18f;
    private static final int MAX_PLAYER_CARD_DUPLICATES = 3;

    @Autowired
    DraftPackService draftPackService;

    @GetMapping("/draft/{playerCount}")
    public String draft(@PathVariable Integer playerCount, ModelMap modelMap) {
        Draft draft = draftPackService.createDraft(new Settings(playerCount, PLAYER_CARD_MIN, HERO_CARD_MIN, SPHERE_BALANCE_MIN, MAX_PLAYER_CARD_DUPLICATES));
        //Add draft to model
        //TODO: Make draft be an API call, return the draft object directly using Jackson?
        modelMap.put("draft", draft);
        return "draft";
    }
}
