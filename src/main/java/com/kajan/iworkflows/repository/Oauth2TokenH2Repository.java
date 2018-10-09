package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.Oauth2TokenStore;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface Oauth2TokenH2Repository extends CrudRepository<Oauth2TokenStore, Long> {

    @Transactional
    void deleteByPrincipalAndOauthProvider(String principal, String oauthProvider);

    Iterable<Oauth2TokenStore> findByPrincipalAndOauthProvider(String principal, String oauthProvider);
}

