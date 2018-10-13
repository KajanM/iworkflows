package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.repository.TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    @Autowired
    TokenRepository tokenRepository;

    public OauthTokenServiceImpl() {
    }

    @Override
    public void setToken(Principal principal, TokenDTO tokenDTO) {
        tokenRepository.setToken(principal, tokenDTO);
    }

    @Override
    public TokenDTO getToken(Principal principal, Constants.TokenProvider tokenProvider) {
        return tokenRepository.getToken(principal, tokenProvider);
    }

    @Override
    public Boolean revokeToken(Principal principal, Constants.TokenProvider tokenProvider) {
        return tokenRepository.revokeToken(principal, tokenProvider);
    }

    @Override
    public Boolean isAlreadyAuthorized(Principal principal, Constants.TokenProvider provider) {
        return tokenRepository.isAlreadyAuthorized(principal, provider);
    }
}
