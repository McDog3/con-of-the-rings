package com.conoftherings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.conoftherings.dto.RegistrationDTO;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/login")
    public String realLogin(RegistrationDTO registrationDTO) {

        return "login";
    }
}
