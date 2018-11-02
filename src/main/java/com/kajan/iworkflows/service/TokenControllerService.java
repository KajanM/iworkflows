package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

public interface TokenControllerService {
    URI getAuthorizationCodeRequestUri(Constants.TokenProvider tokenProvider);

    void exchangeAuthorizationCodeForAccessToken(Constants.TokenProvider registrationId, String queryParams, Principal principal);

    TokenDTO getToken(Principal principal, TokenProvider tokenProvider);

    Boolean isAlreadyAuthorized(Principal principal, Constants.TokenProvider provider);

    Boolean revokeToken(Principal principal, TokenProvider provider);
}
