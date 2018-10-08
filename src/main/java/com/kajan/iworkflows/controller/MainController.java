package com.kajan.iworkflows.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping({"/", "/index", "/index.html"})
    public String home(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        return "index";
    }

    //@GetMapping("/login")
    //public String login() {
    //    return "login";
    //}

    @GetMapping("user")
    public String getUserPage(Principal principal, Model model) {
        logger.debug("hit /user end-point");
        model.addAttribute("name", principal.getName());
        return "user";
    }

}
