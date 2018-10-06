package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.service.NextcloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Controller
public class NextcloudController {

    private final Logger logger = LoggerFactory.getLogger(NextcloudController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NextcloudService nextcloudService;

    @GetMapping("/nextcloud-user")
    @ResponseBody
    public String getNextcloudUserInfo(Principal principal) {
        // TODO: get user id from LDAP
        String userInfoUri = "http://localhost:8090/nextcloud/ocs/v1.php/cloud/users/kajan";

        HttpEntity<String> httpEntity = new HttpEntity<>("headers", nextcloudService.getNextcloudHeaders(principal));
        ResponseEntity<String> responseEntity = restTemplate.exchange(userInfoUri, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.toString();
    }

}
