package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;

public interface TokenRepository {
    void setToken(String principal, TokenDTO tokenDTO);

    TokenDTO getToken(String principal, TokenProvider tokenProvider);

    Boolean revokeToken(String principal, TokenProvider tokenProvider);

    Boolean isAlreadyAuthorized(String principal, Constants.TokenProvider provider);
}
