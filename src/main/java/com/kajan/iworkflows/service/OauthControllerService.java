package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.OauthProvider;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

public interface OauthControllerService {
    URI getAuthorizationCodeRequestUri(OauthProvider oauthProvider);

    void exchangeAuthorizationCodeForAccessToken(OauthProvider registrationId, HttpServletRequest httpServletRequest, Principal principal);

    Oauth2TokenDTO getOauth2Tokens(Principal principal, Constants.OauthProvider oauthProvider);
}
