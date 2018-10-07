package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.Oauth2TokenStore;
import org.springframework.data.repository.CrudRepository;

public interface Oauth2TokenH2Repository extends CrudRepository<Oauth2TokenStore, Long> {
}
