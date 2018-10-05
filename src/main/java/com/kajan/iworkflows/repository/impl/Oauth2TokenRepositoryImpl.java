package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.model.Oauth2Tokens;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Oauth2TokenRepositoryImpl implements Oauth2TokenRepository {

    private Map<Principal, Oauth2Tokens> tokensMap = new HashMap<>();

    public Oauth2TokenRepositoryImpl() {
    }

    @Override
    public void setAuthorizationCode(Principal principal, AuthorizationCode authorizationCode) {
        Oauth2Tokens oauth2Tokens = tokensMap.get(principal);
        if (oauth2Tokens == null) {
            oauth2Tokens = new Oauth2Tokens();
            oauth2Tokens.setAuthorizationCode(authorizationCode);
            tokensMap.put(principal, oauth2Tokens);
        } else {
            oauth2Tokens.setAuthorizationCode(authorizationCode);
        }
    }

    @Override
    public void setAccessToken(Principal principal, AccessToken accessToken) {
        tokensMap.get(principal).setAccessToken(accessToken);
    }

    @Override
    public void setRefreshToken(Principal principal, RefreshToken refreshToken) {
        tokensMap.get(principal).setRefreshToken(refreshToken);
    }

    @Override
    public AccessToken getAccessToken(Principal principal) {
        return tokensMap.get(principal).getAccessToken();
    }
}
