package com.kajan.iworkflows.controller.nextcloud;

import com.kajan.iworkflows.service.NextcloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.TokenProvider.NEXTCLOUD;

@Controller
@RequestMapping("/nextcloud")
public class NextCloudWebDavController {

    private final Logger logger = LoggerFactory.getLogger(NextCloudWebDavController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NextcloudService nextcloudService;

    @GetMapping("/files")
    @ResponseBody
    public String listFiles(Principal principal) {
        logger.debug("Hit /files");
        String userInfoUri = "http://localhost:8090/nextcloud/remote.php/dav/files/kajan/welcome.txt";

        HttpEntity<String> httpEntity = new HttpEntity<>("headers", nextcloudService.getNextcloudHeaders(principal, NEXTCLOUD));
        ResponseEntity<String> responseEntity = restTemplate.exchange(userInfoUri, HttpMethod.GET, httpEntity, String.class);
        return responseEntity.toString();
    }
}
