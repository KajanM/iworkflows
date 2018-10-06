package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class NextcloudServiceImpl implements NextcloudService {

    private final Logger logger = LoggerFactory.getLogger(NextcloudServiceImpl.class);

    @Autowired
    private OauthTokenService oauthTokenService;

    @Override
    public HttpHeaders getNextcloudHeaders(Principal principal, OauthRegistrationId oauthRegistrationId) {
        Oauth2Token oauth2Token = oauthTokenService.getOauth2Tokens(principal, oauthRegistrationId);
        logger.debug("Oauth2Token: " + oauth2Token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("OCS-APIRequest", "true");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + oauth2Token.getAccessToken().getValue());
        return headers;
    }
}
