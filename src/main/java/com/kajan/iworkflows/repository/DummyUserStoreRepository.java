package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DummyUserStoreRepository extends CrudRepository<DummyUserStore, Long> {

    Iterable<DummyUserStore> findByPrincipal(String principal);

    List<DummyUserStore> findByEmployeeId(String employeeId);
}
