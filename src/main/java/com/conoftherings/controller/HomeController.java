package com.conoftherings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.conoftherings.service.DraftPackService;

@Controller
public class HomeController {

    @Autowired
    DraftPackService draftPackService;

    @GetMapping("/home")
    public String home() {
        //TODO: remove after testing
        draftPackService.createDraft();
        return "home";
    }
}
