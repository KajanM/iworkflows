package com.kajan.iworkflows.controller.nextcloud;

import com.kajan.iworkflows.service.NextcloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_USERID;

@Controller
@RequestMapping("/nextcloud")
public class NextcloudController {

    private final Logger logger = LoggerFactory.getLogger(NextcloudController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NextcloudService nextcloudService;

    @Value("${nextcloud.uri.userinfo}")
    private String USER_INFOR_URI;

    @GetMapping("/nextcloud-user")
    @ResponseBody
    public String getNextcloudUserInfo(Principal principal) {

        HttpEntity<String> httpEntity = new HttpEntity<>("", nextcloudService.getNextcloudHeaders(principal));
        ResponseEntity<String> responseEntity = restTemplate.exchange(USER_INFOR_URI.replace(PLACEHOLDER_USERID, principal.getName()), HttpMethod.GET, httpEntity, String.class);
        return responseEntity.toString();
    }

}
