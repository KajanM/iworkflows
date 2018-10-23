package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import org.springframework.data.repository.CrudRepository;

public interface DummyUserStoreRepository extends CrudRepository<DummyUserStore, Long> {

    Iterable<DummyUserStore> findByPrincipal(String principal);
}
