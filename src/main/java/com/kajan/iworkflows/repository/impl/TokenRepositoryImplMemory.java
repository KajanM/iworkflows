package com.kajan.iworkflows.repository.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.repository.TokenRepository;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.*;
import java.util.function.Predicate;

@Repository
@Profile("memory")
public class TokenRepositoryImplMemory implements TokenRepository {

    private Map<Principal, Set<TokenDTO>> tokensMap = new HashMap<>();

    public TokenRepositoryImplMemory() {
    }

    @Override
    public void setToken(Principal principal, TokenDTO tokenDTO) {
        Set<TokenDTO> tokenDTOSet = tokensMap.get(principal);
        if (tokenDTOSet == null) {
            tokenDTOSet = new HashSet<>();
        }
        tokenDTOSet.add(tokenDTO);
        tokensMap.replace(principal, tokenDTOSet);
    }

    @Override
    public TokenDTO getToken(Principal principal, TokenProvider tokenProvider) {
        return tokensMap.get(principal)
                .stream()
                .filter(Predicate.isEqual(tokenProvider))
                .findFirst()
                .get();
    }

    @Override
    public Boolean revokeToken(Principal principal, Constants.TokenProvider tokenProvider) {
        tokensMap.get(principal).removeIf(oauth2TokenDTO -> oauth2TokenDTO.getTokenProvider().equals(tokenProvider));
        return true;
    }

    @Override
    public Boolean isAlreadyAuthorized(Principal principal, Constants.TokenProvider provider) {
        Optional<TokenDTO> token = tokensMap.get(principal).stream()
                .filter(tokenDTO -> tokenDTO.getTokenProvider().equals(provider))
                .findFirst();
        if (token.isPresent()) {
            return true;
        }
        return false;
    }
}
