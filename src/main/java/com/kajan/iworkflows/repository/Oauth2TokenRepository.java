package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import java.security.Principal;

public interface Oauth2TokenRepository {
    void setOauth2Token(Principal principal, TokenDTO tokenDTO);

    TokenDTO getOauth2Token(Principal principal, TokenProvider tokenProvider);

    Boolean revokeOauth2Token(Principal principal, TokenProvider tokenProvider);

    Boolean alreadyAuthorized(Principal principal, Constants.TokenProvider provider);
}
