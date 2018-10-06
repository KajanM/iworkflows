package com.kajan.iworkflows.service;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;

import java.security.Principal;

public interface OauthTokenService {
    void setOauth2Tokens(Principal principal, Oauth2Token oauth2Token);

    Oauth2Token getOauth2Tokens(Principal principal, OauthRegistrationId oauthRegistrationId);
}
