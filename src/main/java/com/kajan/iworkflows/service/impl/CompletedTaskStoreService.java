package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.CompletedTaskStore;
import com.kajan.iworkflows.repository.CompletedTaskStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompletedTaskStoreService {
    @Autowired
    private CompletedTaskStoreRepository repository;

    public Iterable<CompletedTaskStore> findByPrincipal(String principal) {
        return repository.findByPrincipal(principal);
    }

}
