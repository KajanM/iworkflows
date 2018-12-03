package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogStoreServiceImpl {
    @Autowired
    private LogStoreRepository repository;

    public Iterable<LogStore> findByPrincipal(String principal) {
        return repository.findByPrincipal(principal);
    }
}
