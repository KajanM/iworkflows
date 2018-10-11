package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.TokenStore;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface Oauth2TokenH2Repository extends CrudRepository<TokenStore, Long> {

    @Transactional
    void deleteByPrincipalAndTokenProvider(String principal, String tokenProvider);

    Iterable<TokenStore> findByPrincipalAndTokenProvider(String principal, String tokenProvider);
}

