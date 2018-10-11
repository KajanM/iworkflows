package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.TokenStore;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface Oauth2TokenH2Repository extends CrudRepository<TokenStore, Long> {

    @Transactional
    void deleteByPrincipalAndOauthProvider(String principal, String oauthProvider);

    Iterable<TokenStore> findByPrincipalAndOauthProvider(String principal, String oauthProvider);
}

