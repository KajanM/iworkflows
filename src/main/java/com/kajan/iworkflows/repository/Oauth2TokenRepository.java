package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;

import java.security.Principal;

public interface Oauth2TokenRepository {
    void setOauth2Token(Principal principal, Oauth2Token oauth2Token);

    Oauth2Token getOauth2Token(Principal principal, OauthRegistrationId oauthRegistrationId);
}
