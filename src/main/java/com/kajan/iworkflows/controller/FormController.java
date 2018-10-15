package com.kajan.iworkflows.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class FormController {
    @GetMapping({"/forms/leave-form", "/forms/leave-form.html"})
    public String leaveForm(Principal principal, Model model) {
        return "leave-form";
    }
}
