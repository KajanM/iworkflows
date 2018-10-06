package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.service.OauthTokenService;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class NextcloudServiceImpl implements com.kajan.iworkflows.service.NextcloudService {

    private final Logger logger = LoggerFactory.getLogger(NextcloudServiceImpl.class);

    @Autowired
    private OauthTokenService oauthTokenService;

    @Override
    public HttpHeaders getNextcloudHeaders(Principal principal) {
        AccessToken accessToken = oauthTokenService.getAccessToken(principal);
        logger.debug("AccessToken: " + accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("OCS-APIRequest", "true");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getValue());
        return headers;
    }
}
