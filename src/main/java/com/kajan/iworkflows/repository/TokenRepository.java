package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

import java.security.Principal;

public interface TokenRepository {
    void setToken(Principal principal, TokenDTO tokenDTO);

    TokenDTO getToken(Principal principal, TokenProvider tokenProvider);

    Boolean revokeToken(Principal principal, TokenProvider tokenProvider);

    Boolean isAlreadyAuthorized(Principal principal, Constants.TokenProvider provider);
}
