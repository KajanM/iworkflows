package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.GroupMapper;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by User on 11/12/2018.
 */
public interface GroupMapperRepository extends CrudRepository<GroupMapper, Long> {

    Iterable<GroupMapper> findByIworkflowsRole(String learnorgRole);

//    List<GroupMapper> findByEmployeeId(String employeeId);
}
