package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.CompletedTaskStore;
import com.kajan.iworkflows.model.RequestStore;
import org.springframework.data.repository.CrudRepository;

public interface CompletedTaskStoreRepository extends CrudRepository<CompletedTaskStore, Long> {
    Iterable<CompletedTaskStore> findByPrincipal(String principal);
}
