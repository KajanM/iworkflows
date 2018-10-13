package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.model.TokenStore;
import com.kajan.iworkflows.repository.Oauth2TokenH2Repository;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
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

import java.security.Principal;
import java.util.Iterator;

@Repository
@Profile("h2")
public class Oauth2TokenRepositoryImplH2 implements Oauth2TokenRepository {

    private final Logger logger = LoggerFactory.getLogger(Oauth2TokenRepositoryImplH2.class);

    @Autowired
    private Oauth2TokenH2Repository oauth2TokenH2Repository;

    @Override
    public void setOauth2Token(Principal principal, TokenDTO tokenDTO) {
        TokenStore tokenStore = new TokenStore(principal.getName(), tokenDTO.getTokenProvider().getProvider());
        if (tokenDTO.getAuthorizationCode() != null) {
            tokenStore.setAuthorizationCode(tokenDTO.getAuthorizationCode().getValue());
        }
        tokenStore.setAccessToken(tokenDTO.getAccessToken().getValue());
        if (tokenDTO.getRefreshToken() != null) {
            tokenStore.setRefreshToken(tokenDTO.getRefreshToken().getValue());
        }

        logger.debug("storing oauth2 tokens in db " + tokenStore);

        oauth2TokenH2Repository.save(tokenStore);
    }

    @Override
    public TokenDTO getOauth2Token(Principal principal, TokenProvider tokenProvider) {
        Iterable<TokenStore> allTokens = oauth2TokenH2Repository.findByPrincipalAndTokenProvider(principal.getName(), tokenProvider.getProvider());

        for (TokenStore store : allTokens) {
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
    public Boolean revokeOauth2Token(Principal principal, Constants.TokenProvider tokenProvider) {
        oauth2TokenH2Repository.deleteByPrincipalAndTokenProvider(principal.getName(), tokenProvider.getProvider());
        return true;
    }

    @Override
    public Boolean alreadyAuthorized(Principal principal, Constants.TokenProvider provider) {
        Iterator<TokenStore> iterator = oauth2TokenH2Repository.findByPrincipalAndTokenProvider(principal.getName(), provider.getProvider()).iterator();

        if (iterator.hasNext()) {
            return true;
        }
        return false;
    }
}
