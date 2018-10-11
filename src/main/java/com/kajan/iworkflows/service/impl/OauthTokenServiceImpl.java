package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    @Autowired
    Oauth2TokenRepository oauth2TokenRepository;

    public OauthTokenServiceImpl() {
    }

    @Override
    public void setOauth2Tokens(Principal principal, TokenDTO tokenDTO) {
        oauth2TokenRepository.setOauth2Token(principal, tokenDTO);
    }

    @Override
    public TokenDTO getOauth2Tokens(Principal principal, Constants.TokenProvider tokenProvider) {
        return oauth2TokenRepository.getOauth2Token(principal, tokenProvider);
    }

    @Override
    public Boolean revokeOauth2Token(Principal principal, Constants.TokenProvider tokenProvider) {
        return oauth2TokenRepository.revokeOauth2Token(principal, tokenProvider);
    }

    @Override
    public Boolean alreadyAuthorized(Principal principal, Constants.TokenProvider provider) {
        return oauth2TokenRepository.alreadyAuthorized(principal, provider);
    }
}
