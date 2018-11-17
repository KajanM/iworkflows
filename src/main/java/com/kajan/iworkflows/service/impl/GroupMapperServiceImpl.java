package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.GroupMapper;
import com.kajan.iworkflows.repository.GroupMapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMapperServiceImpl {

    @Autowired
    private GroupMapperRepository repository;

    public Iterable<GroupMapper> findByIworkflowsRole(String iworkflowsRole) {
        return repository.findByIworkflowsRole(iworkflowsRole);
    }
}
