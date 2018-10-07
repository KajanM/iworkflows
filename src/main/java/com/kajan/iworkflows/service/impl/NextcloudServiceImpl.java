package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants.OauthProvider;
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
    public HttpHeaders getNextcloudHeaders(Principal principal, OauthProvider oauthProvider) {
        Oauth2TokenDTO oauth2TokenDTO = oauthTokenService.getOauth2Tokens(principal, oauthProvider);
        logger.debug("Oauth2TokenDTO: " + oauth2TokenDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("OCS-APIRequest", "true");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + oauth2TokenDTO.getAccessToken().getValue());
        return headers;
    }
}
