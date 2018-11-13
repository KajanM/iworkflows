package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.Mapper;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by User on 11/12/2018.
 */
public interface MapperRepository extends CrudRepository<Mapper, Long> {

    Iterable<Mapper> findByIworkflowsRole(String learnorgRole);

//    List<Mapper> findByEmployeeId(String employeeId);
}
