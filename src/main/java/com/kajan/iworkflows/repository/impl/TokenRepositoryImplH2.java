package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.model.TokenStore;
import com.kajan.iworkflows.repository.TokenH2Repository;
import com.kajan.iworkflows.repository.TokenRepository;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
@Profile("h2")
public class TokenRepositoryImplH2 implements TokenRepository {

    private final Logger logger = LoggerFactory.getLogger(TokenRepositoryImplH2.class);

    private TokenH2Repository tokenH2Repository;

    @Override
    public void setToken(String principal, TokenDTO tokenDTO) {
        TokenStore tokenStore = new TokenStore(principal, tokenDTO.getTokenProvider().getProvider());
        if (tokenDTO.getAuthorizationCode() != null) {
            tokenStore.setAuthorizationCode(tokenDTO.getAuthorizationCode().getValue());
        }
        tokenStore.setAccessToken(tokenDTO.getAccessToken().getValue());
        if (tokenDTO.getRefreshToken() != null) {
            tokenStore.setRefreshToken(tokenDTO.getRefreshToken().getValue());
        }

        logger.debug("storing oauth2 tokens in db " + tokenStore);

        tokenH2Repository.save(tokenStore);
    }

    @Override
    public TokenDTO getToken(String principal, TokenProvider tokenProvider) {
        Iterable<TokenStore> allTokens = tokenH2Repository.findByPrincipalAndTokenProvider(principal, tokenProvider.getProvider());

        for (Iterator<TokenStore> iterator = allTokens.iterator(); iterator.hasNext(); ) {
            TokenStore store = iterator.next();
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setTokenProvider(tokenProvider);
            if (store.getAuthorizationCode() != null) {
                tokenDTO.setAuthorizationCode(new AuthorizationCode(store.getAuthorizationCode()));
            }
            tokenDTO.setAccessToken(new BearerAccessToken(store.getAccessToken()));
            if (store.getRefreshToken() != null) {
                tokenDTO.setRefreshToken(new RefreshToken(store.getRefreshToken()));
            }
            return tokenDTO;
        }
        return null;
    }

    @Override
    public Boolean revokeToken(String principal, Constants.TokenProvider tokenProvider) {
        tokenH2Repository.deleteByPrincipalAndTokenProvider(principal, tokenProvider.getProvider());
        logger.debug("Successfully revoked token for Principal: " + principal + " Provider: " + tokenProvider);
        return true;
    }

    @Override
    public Boolean isAlreadyAuthorized(String principal, Constants.TokenProvider provider) {
        Iterator<TokenStore> iterator = tokenH2Repository.findByPrincipalAndTokenProvider(principal, provider.getProvider()).iterator();

        return iterator.hasNext();
    }

    @Autowired
    public void setTokenH2Repository(TokenH2Repository tokenH2Repository) {
        this.tokenH2Repository = tokenH2Repository;
    }
}
