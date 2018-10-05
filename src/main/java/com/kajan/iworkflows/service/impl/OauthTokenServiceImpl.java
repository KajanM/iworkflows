package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
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
    public void setAuthorizationCode(Principal principal, AuthorizationCode authorizationCode) {
        oauth2TokenRepository.setAuthorizationCode(principal, authorizationCode);
    }

    @Override
    public void setAccessToken(Principal principal, AccessToken accessToken) {
        oauth2TokenRepository.setAccessToken(principal, accessToken);
    }

    @Override
    public void setRefreshToken(Principal principal, RefreshToken refreshToken) {
        oauth2TokenRepository.setRefreshToken(principal, refreshToken);
    }

    @Override
    public AccessToken getAccessToken(Principal principal) {
        return oauth2TokenRepository.getAccessToken(principal);
    }
}
