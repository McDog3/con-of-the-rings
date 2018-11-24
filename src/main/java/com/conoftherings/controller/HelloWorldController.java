/**
 * HelloWorldController
 * Copyright (c) 2018, FastBridge Learning LLC
 * Created on 11/24/18
 */
package com.conoftherings.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "Aaaaattention gamers, ";
    }
}
