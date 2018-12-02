package com.conoftherings.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conoftherings.dao.UserRepository;
import com.conoftherings.domain.User;

@RestController
public class HelloWorldController {

    private UserRepository userRepository;

    @Autowired
    HelloWorldController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String index() {
        return "Aaaaattention gamers, ";
    }

    @RequestMapping("/users")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/user/{userName}")
    public User getUser(@PathVariable String userName) {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }
}
