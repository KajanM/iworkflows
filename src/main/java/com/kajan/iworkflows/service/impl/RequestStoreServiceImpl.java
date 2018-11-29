package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.RequestStore;
import com.kajan.iworkflows.repository.RequestStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestStoreServiceImpl {
    @Autowired
    private RequestStoreRepository repository;

    public Iterable<RequestStore> findByPrincipalAndStatus(String principal, String status) {
        return repository.findByPrincipalAndStatus(principal, status);
    }
    public Iterable<RequestStore> findByPrincipalAndStatusIn(String principal, List status) {
        return repository.findByPrincipalAndStatusIn(principal, status);
    }
    public Iterable<RequestStore> findByPrincipalAndLeaveType(String principal, String leaveType) {
        return repository.findByPrincipalAndLeaveType(principal, leaveType);
    }

}
