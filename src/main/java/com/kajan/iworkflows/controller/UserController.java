package com.kajan.iworkflows.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @CrossOrigin
    @GetMapping("api/v1/user")
    public Principal user(Principal user) {
        return user;
    }
}