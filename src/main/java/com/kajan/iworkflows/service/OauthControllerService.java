package com.kajan.iworkflows.service;

import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

public interface OauthControllerService {
    URI getAuthorizationCodeRequestUri(OauthRegistrationId oauthRegistrationId);

    void exchangeAuthorizationCodeForAccessToken(OauthRegistrationId registrationId, HttpServletRequest httpServletRequest, Principal principal);

    AccessToken getAccessToken(Principal principal);

    RefreshToken getRefreshToken(Principal principal);
}
