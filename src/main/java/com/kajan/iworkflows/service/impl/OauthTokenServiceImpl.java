package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    @Autowired
    Oauth2TokenRepository oauth2TokenRepository;

    public OauthTokenServiceImpl() {
    }

    @Override
    public void setOauth2Tokens(Principal principal, Oauth2Token oauth2Token) {
        oauth2TokenRepository.setOauth2Token(principal, oauth2Token);
    }

    @Override
    public Oauth2Token getOauth2Tokens(Principal principal, OauthRegistrationId oauthRegistrationId) {
        return oauth2TokenRepository.getOauth2Token(principal, oauthRegistrationId);
    }
}
