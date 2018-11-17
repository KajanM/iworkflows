package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.GroupMapper;
import org.springframework.data.repository.CrudRepository;


public interface GroupMapperRepository extends CrudRepository<GroupMapper, Long> {

    Iterable<GroupMapper> findByIworkflowsRole(String learnorgRole);

}
