package com.kajan.iworkflows.service;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import java.security.Principal;

public interface OauthTokenService {
    void setAuthorizationCode(Principal principal, AuthorizationCode authorizationCode);

    void setAccessToken(Principal principal, AccessToken accessToken);

    void setRefreshToken(Principal principal, RefreshToken refreshToken);
}
