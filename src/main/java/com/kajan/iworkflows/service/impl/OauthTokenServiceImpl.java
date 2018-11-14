package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.repository.TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    private TokenRepository tokenRepository;

    public OauthTokenServiceImpl() {
    }

    @Override
    public void setToken(String principal, TokenDTO tokenDTO) {
        tokenRepository.setToken(principal, tokenDTO);
    }

    @Override
    public TokenDTO getToken(String principal, Constants.TokenProvider tokenProvider) {
        return tokenRepository.getToken(principal, tokenProvider);
    }

    @Override
    public Boolean revokeToken(String principal, Constants.TokenProvider tokenProvider) {
        return tokenRepository.revokeToken(principal, tokenProvider);
    }

    @Override
    public Boolean isAlreadyAuthorized(String principal, Constants.TokenProvider provider) {
        return tokenRepository.isAlreadyAuthorized(principal, provider);
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
