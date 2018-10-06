package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class Oauth2TokenRepositoryImpl implements Oauth2TokenRepository {

    private Map<Principal, Set<Oauth2Token>> tokensMap = new HashMap<>();

    public Oauth2TokenRepositoryImpl() {
    }

    @Override
    public void setOauth2Token(Principal principal, Oauth2Token oauth2Token) {
        Set<Oauth2Token> oauth2TokenSet = tokensMap.get(principal);
        if (oauth2TokenSet == null) {
            oauth2TokenSet = new HashSet<>();
        }
        oauth2TokenSet.add(oauth2Token);
        tokensMap.put(principal, oauth2TokenSet);
    }

    @Override
    public Oauth2Token getOauth2Token(Principal principal, OauthRegistrationId oauthRegistrationId) {
        Set<Oauth2Token> oauth2Tokens = tokensMap.get(principal);
        for (Oauth2Token tokens : oauth2Tokens) {
            if (tokens.getClientRegistrationId().equals(oauthRegistrationId)) {
                return tokens;
            }
        }
        throw new IllegalArgumentException("No Oauth2 token found for principal: " + principal + " and oauth registartion id: " + oauthRegistrationId);
    }

}
