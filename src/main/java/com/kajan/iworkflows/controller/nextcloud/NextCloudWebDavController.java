package com.kajan.iworkflows.controller.nextcloud;

import com.kajan.iworkflows.exception.UnauthorizedException;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_FILE_PATH;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_USERID;

@Controller
@RequestMapping("/nextcloud")
public class NextCloudWebDavController {

    private final Logger logger = LoggerFactory.getLogger(NextCloudWebDavController.class);

    private final String WELCOME_FILE_PATH = "welcome.txt";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NextcloudService nextcloudService;

    @Value("${nextcloud.uri.file}")
    private String FILE_ROOT_URI_TEMPLATE;

    @GetMapping("/files")
    @ResponseBody
    public ResponseEntity<String> getWelcomeTxt(Principal principal) {
        String uri = FILE_ROOT_URI_TEMPLATE.replace(PLACEHOLDER_USERID, principal.getName().toLowerCase())
                .replace(PLACEHOLDER_FILE_PATH, WELCOME_FILE_PATH);
        HttpEntity<String> httpEntity = new HttpEntity<>("", nextcloudService.getNextcloudHeaders(principal));
        logger.info("Making request to {} to download the file with headers {}", uri, httpEntity);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        } catch (RestClientException e) {
            logger.error("Error occured while trying to download a file from NextCloud", e);
            throw new UnauthorizedException();
        }

        return responseEntity;
    }
}
