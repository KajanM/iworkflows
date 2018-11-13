package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.Mapper;
import com.kajan.iworkflows.repository.MapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by User on 11/13/2018.
 */

@Service
public class MapperServiceImpl {

    @Autowired
    private MapperRepository repository;

    public Iterable<Mapper> findByIworkflowsRole(String iworkflowsRole) {
        return repository.findByIworkflowsRole(iworkflowsRole);
    }
}
