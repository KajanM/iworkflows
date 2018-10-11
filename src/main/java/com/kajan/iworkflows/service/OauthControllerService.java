package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

public interface OauthControllerService {
    URI getAuthorizationCodeRequestUri(Constants.TokenProvider tokenProvider);

    void exchangeAuthorizationCodeForAccessToken(Constants.TokenProvider registrationId, HttpServletRequest httpServletRequest, Principal principal);

    TokenDTO getOauth2Tokens(Principal principal, TokenProvider tokenProvider);

    Boolean alreadyAuthorized(Principal principal, Constants.TokenProvider provider);

    Boolean revokeOauth2Token(Principal principal, TokenProvider provider);
}
