package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.exception.UnauthorizedException;
import com.kajan.iworkflows.service.NextcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.security.Principal;

@RestController
@RequestMapping("test")
public class TestController {

    private NextcloudService nextcloudService;

    @GetMapping("/500")
    public String simulate500() {
        String a = "panda";
        //noinspection ResultOfMethodCallIgnored
        a.charAt(50);
        return null;
    }

    @GetMapping("/401")
    public String simulate401() {
        throw new UnauthorizedException();
    }

    @GetMapping("/get-file")
    public InputStream getFile(Principal principal) {
        return nextcloudService.getFile(principal.getName(), "welcome.txt");
    }

    @GetMapping("/get-file-iworkflows")
    public ResponseEntity<Resource> getFileAsIworkflows() {
       return ResponseEntity.ok(nextcloudService.getFileAsIworkflows("welcome.txt"));
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
