package com.kajan.iworkflows.service;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

public interface OauthTokenService {
    void setToken(String principal, TokenDTO tokenDTO);

    TokenDTO getToken(String principal, TokenProvider tokenProvider);

    Boolean revokeToken(String principal, Constants.TokenProvider tokenProvider);

    Boolean isAlreadyAuthorized(String principal, TokenProvider provider);
}
