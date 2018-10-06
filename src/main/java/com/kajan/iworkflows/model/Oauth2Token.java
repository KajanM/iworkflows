package com.kajan.iworkflows.model;

import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Oauth2Token {
    private OauthRegistrationId clientRegistrationId;
    private AuthorizationCode authorizationCode;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}
