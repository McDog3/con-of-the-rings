package com.conoftherings.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conoftherings.dao.UserRepository;
import com.conoftherings.domain.User;

@RestController
@RequestMapping("/login")
public class LoginRestController {

    private UserRepository userRepository;

    @Autowired
    LoginRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String register(@RequestParam String userName, @RequestParam String email, @RequestParam String password) {
        //FIXME: password will not be saved directly to DB
        User newUser = new User();
        newUser.setUsername(userName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return "success";
    }

    @GetMapping("/in")
    public String login(@RequestParam String userName, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(userName, password);
        if (optionalUser.isPresent()) {
            //TODO: Add userInfo to the session
        } else {
            return "failure";
        }
        return "success";
    }

    @GetMapping("/out")
    public String logout() {
        //TODO: Remove userInfo from context - send to home page
        return "success";
    }
}
