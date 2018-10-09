package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.repository.Oauth2TokenRepository;
import com.kajan.iworkflows.util.Constants.OauthProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@Profile("memory")
public class Oauth2TokenRepositoryImplMemory implements Oauth2TokenRepository {

    private Map<Principal, Set<Oauth2TokenDTO>> tokensMap = new HashMap<>();

    public Oauth2TokenRepositoryImplMemory() {
    }

    @Override
    public void setOauth2Token(Principal principal, Oauth2TokenDTO oauth2TokenDTO) {
        Set<Oauth2TokenDTO> oauth2TokenDTOSet = tokensMap.get(principal);
        if (oauth2TokenDTOSet == null) {
            oauth2TokenDTOSet = new HashSet<>();
        }
        oauth2TokenDTOSet.add(oauth2TokenDTO);
        tokensMap.put(principal, oauth2TokenDTOSet);
    }

    @Override
    public Oauth2TokenDTO getOauth2Token(Principal principal, OauthProvider oauthProvider) {
        Set<Oauth2TokenDTO> oauth2TokenDTOS = tokensMap.get(principal);
        for (Oauth2TokenDTO tokens : oauth2TokenDTOS) {
            if (tokens.getOauthProvider().equals(oauthProvider)) {
                return tokens;
            }
        }
        throw new IllegalArgumentException("No Oauth2 token found for principal: " + principal + " and oauth registartion id: " + oauthProvider);
    }

    @Override
    public Boolean revokeOauth2Token(Principal principal, OauthProvider oauthProvider) {
        tokensMap.get(principal).removeIf(oauth2TokenDTO -> oauth2TokenDTO.getOauthProvider().equals(oauthProvider));
        return true;
    }

    @Override
    public Boolean alreadyAuthorized(Principal principal, OauthProvider provider) {
        for (Oauth2TokenDTO dto : tokensMap.get(principal)) {
            if (dto.getOauthProvider().equals(provider)) {
                return true;
            }
        }
        return false;
    }
}
