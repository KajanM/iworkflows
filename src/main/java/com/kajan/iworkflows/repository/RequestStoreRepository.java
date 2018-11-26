package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.RequestStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface RequestStoreRepository extends CrudRepository<RequestStore, Long> {
    Iterable<RequestStore> findByPrincipalAndStatus(String principal, String status);
    Iterable<RequestStore> findByPrincipalAndStatusIn(String principal, List status);

}
