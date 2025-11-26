package com.hackathon.knut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/encode")
    public String encodePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, encoded);
        return String.format("Password: %s, Encoded: %s, Matches: %s", password, encoded, matches);
    }
}