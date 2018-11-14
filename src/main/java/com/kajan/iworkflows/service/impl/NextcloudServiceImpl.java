package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.TokenProvider.NEXTCLOUD;

@Service
public class NextcloudServiceImpl implements NextcloudService {

    private final Logger logger = LoggerFactory.getLogger(NextcloudServiceImpl.class);

    private final String HEADER_OCS_API_REQUEST = "OCS-APIRequest";
    private final String HEADER_VALUE_BEARER = "Bearer ";
    private final String HEADER_VALUE_TRUE = "true";

    private OauthTokenService oauthTokenService;

    @Override
    public HttpHeaders getNextcloudHeaders(Principal principal) {
        TokenDTO tokenDTO = oauthTokenService.getToken(principal.getName(), NEXTCLOUD);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_OCS_API_REQUEST, HEADER_VALUE_TRUE);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BEARER + tokenDTO.getAccessToken().getValue());
        return headers;
    }

    @Autowired
    public void setOauthTokenService(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }
}
