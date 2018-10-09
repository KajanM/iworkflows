package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.util.Constants.OauthProvider;

import java.security.Principal;

public interface Oauth2TokenRepository {
    void setOauth2Token(Principal principal, Oauth2TokenDTO oauth2TokenDTO);

    Oauth2TokenDTO getOauth2Token(Principal principal, OauthProvider oauthProvider);

    Boolean revokeOauth2Token(Principal principal, OauthProvider oauthProvider);

    Boolean alreadyAuthorized(Principal principal, OauthProvider provider);
}
