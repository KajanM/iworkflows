package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyUserStoreServiceImpl {

    @Autowired
    private DummyUserStoreRepository repository;

    public Iterable<DummyUserStore> findByPrincipal(String principal) {
        return repository.findByPrincipal(principal);
    }
}
