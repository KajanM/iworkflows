package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import java.security.Principal;

public interface OauthTokenService {
    void setOauth2Tokens(Principal principal, TokenDTO tokenDTO);

    TokenDTO getOauth2Tokens(Principal principal, TokenProvider tokenProvider);

    Boolean revokeOauth2Token(Principal principal, Constants.TokenProvider tokenProvider);

    Boolean alreadyAuthorized(Principal principal, TokenProvider provider);
}
