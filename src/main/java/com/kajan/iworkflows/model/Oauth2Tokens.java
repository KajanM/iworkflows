package com.kajan.iworkflows.model;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Oauth2Tokens {
    private AuthorizationCode authorizationCode;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}
