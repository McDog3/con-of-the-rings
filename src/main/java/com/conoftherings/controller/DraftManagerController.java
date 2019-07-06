package com.conoftherings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DraftManagerController {

    @GetMapping("/draft")
    public String draft() {
        return "draft";
    }
}
