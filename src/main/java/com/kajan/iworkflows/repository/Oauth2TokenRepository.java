package com.kajan.iworkflows.repository;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import java.security.Principal;

public interface Oauth2TokenRepository {
    void setAuthorizationCode(Principal principal, AuthorizationCode authorizationCode);

    void setAccessToken(Principal principal, AccessToken accessToken);

    void setRefreshToken(Principal principal, RefreshToken refreshToken);

    AccessToken getAccessToken(Principal principal);

    RefreshToken getRefreshToken(Principal principal);
}
