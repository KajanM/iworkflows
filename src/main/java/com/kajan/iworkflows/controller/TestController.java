package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.exception.IworkflowsWebDavException;
import com.kajan.iworkflows.service.NextcloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    private NextcloudService nextcloudService;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @GetMapping("/500")
    public String simulate500() {
        String a = "panda";
        //noinspection ResultOfMethodCallIgnored
        a.charAt(50);
        return null;
    }

    @GetMapping("/401")
    public String simulate401() {
        throw new IworkflowsPreConditionRequiredException();
    }

    @GetMapping("/get-file")
    public InputStream getFile(@RequestParam("path") String resourcePath, Principal principal) {
        return nextcloudService.getFile(principal.getName(), resourcePath);
    }

    @GetMapping("/get-file-iworkflows")
    public ResponseEntity<?> getFileAsIworkflows() {
        return ResponseEntity.ok(nextcloudService.getFile(IWORKFLOWS_USERNAME, "welcome.txt"));
    }

    @PostMapping("/upload-as-iworkflows")
    public ResponseEntity<?> uploadFileAsIworkflows(@RequestParam("file") MultipartFile file) {
        try {
            nextcloudService.uploadFile(IWORKFLOWS_USERNAME, "", file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to upload file to NextCloud", e);
            throw new IworkflowsWebDavException("Unable to upload file to NextCloud");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload-as-user")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            nextcloudService.uploadFile(principal.getName(), UriUtils.encodePathSegment(file.getOriginalFilename(), "UTF-8"), file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to upload file to NextCloud", e);
            throw new IworkflowsWebDavException("Unable to upload file to NextCloud");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/copy-to-user")
    public ResponseEntity<?> copyToUser(Principal principal) {
        InputStream inputStream = nextcloudService.getFile(IWORKFLOWS_USERNAME, "welcome.txt");
        nextcloudService.uploadFile(principal.getName(), "welcome.txt", inputStream);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/share")
    public ResponseEntity<?> share(@RequestParam("path") String resourcePath, Principal principal) {
        nextcloudService.share(principal.getName(), resourcePath);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
