package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.model.Oauth2TokenStore;
import com.kajan.iworkflows.repository.Oauth2TokenH2Repository;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.util.Constants.OauthProvider;
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
    public void setOauth2Token(Principal principal, Oauth2TokenDTO oauth2TokenDTO) {
        Oauth2TokenStore oauth2TokenStore = new Oauth2TokenStore(principal.getName(), oauth2TokenDTO.getOauthProvider().getProvider());
        oauth2TokenStore.setAuthorizationCode(oauth2TokenDTO.getAuthorizationCode().getValue());
        oauth2TokenStore.setAccessToken(oauth2TokenDTO.getAccessToken().getValue());
        if (oauth2TokenDTO.getRefreshToken() != null) {
            oauth2TokenStore.setRefreshToken(oauth2TokenDTO.getRefreshToken().getValue());
        }

        logger.debug("storing oauth2 tokens in db " + oauth2TokenStore);

        oauth2TokenH2Repository.save(oauth2TokenStore);
    }

    @Override
    public Oauth2TokenDTO getOauth2Token(Principal principal, OauthProvider oauthProvider) {
        Iterable<Oauth2TokenStore> allTokens = oauth2TokenH2Repository.findByPrincipalAndOauthProvider(principal.getName(), oauthProvider.getProvider());

        for (Oauth2TokenStore store : allTokens) {
            Oauth2TokenDTO oauth2TokenDTO = new Oauth2TokenDTO();
            oauth2TokenDTO.setOauthProvider(oauthProvider);
            oauth2TokenDTO.setAuthorizationCode(new AuthorizationCode(store.getAuthorizationCode()));
            oauth2TokenDTO.setAccessToken(new BearerAccessToken(store.getAccessToken()));
            if (store.getRefreshToken() != null) {
                oauth2TokenDTO.setRefreshToken(new RefreshToken(store.getRefreshToken()));
            }
            return oauth2TokenDTO;
        }
        return null;
    }

    @Override
    public Boolean revokeOauth2Token(Principal principal, OauthProvider oauthProvider) {
        oauth2TokenH2Repository.deleteByPrincipalAndOauthProvider(principal.getName(), oauthProvider.getProvider());
        return true;
    }

    @Override
    public Boolean alreadyAuthorized(Principal principal, OauthProvider provider) {
        Iterator<Oauth2TokenStore> iterator = oauth2TokenH2Repository.findByPrincipalAndOauthProvider(principal.getName(), provider.getProvider()).iterator();

        if (iterator.hasNext()) {
            return true;
        }
        return false;
    }
}
