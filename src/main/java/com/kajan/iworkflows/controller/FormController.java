package com.kajan.iworkflows.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/forms")
public class FormController {
    @GetMapping({"leave-form", "leave-form.html"})
    public String leaveForm(Principal principal, Model model) {
        return "leave-form";
    }
}
