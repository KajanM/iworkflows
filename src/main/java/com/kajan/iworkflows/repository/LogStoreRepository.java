package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.LogStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LogStoreRepository extends CrudRepository<LogStore, Long> {
    Iterable<LogStore> findByPrincipal(String principal);
}
