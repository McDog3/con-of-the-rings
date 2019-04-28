package com.conoftherings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.conoftherings.service.DraftPackService;

@Controller
public class DraftManagerController {

    @Autowired
    DraftPackService draftPackService;

    @GetMapping("/draft/{playerCount}")
    public String draft(@PathVariable Integer playerCount) {
        draftPackService.createDraft(playerCount);
        //Add draft to model
        return "draft";
    }
}
