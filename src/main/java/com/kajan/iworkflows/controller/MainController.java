package com.kajan.iworkflows.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping({"/", "/index", "/index.html"})
    @ResponseBody
    public String home() {
        return "Hello from Spring";
    }

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        logger.debug("hit /user endpoint");
        model.addAttribute("name", principal.getName());
        return "user";
    }

}
