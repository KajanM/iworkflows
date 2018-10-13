package com.kajan.iworkflows.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String getTestPage() {
        return "test";
    }
}
