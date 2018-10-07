package com.kajan.iworkflows.dto;

import com.kajan.iworkflows.util.Constants.OauthProvider;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Oauth2TokenDTO {
    private OauthProvider oauthProvider;
    private AuthorizationCode authorizationCode;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}
