package com.kajan.iworkflows.service;

import com.kajan.iworkflows.model.Oauth2Token;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

public interface OauthControllerService {
    URI getAuthorizationCodeRequestUri(OauthRegistrationId oauthRegistrationId);

    void exchangeAuthorizationCodeForAccessToken(OauthRegistrationId registrationId, HttpServletRequest httpServletRequest, Principal principal);

    Oauth2Token getOauth2Tokens(Principal principal, OauthRegistrationId oauthRegistrationId);
}
