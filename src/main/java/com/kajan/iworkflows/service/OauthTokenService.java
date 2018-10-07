package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.util.Constants.OauthProvider;

import java.security.Principal;

public interface OauthTokenService {
    void setOauth2Tokens(Principal principal, Oauth2TokenDTO oauth2TokenDTO);

    Oauth2TokenDTO getOauth2Tokens(Principal principal, OauthProvider oauthProvider);
}
