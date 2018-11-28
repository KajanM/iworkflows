package com.kajan.iworkflows.repository;

import com.kajan.iworkflows.model.GithubIssue;
import org.springframework.data.repository.CrudRepository;

public interface GithubRepository extends CrudRepository<GithubIssue, Long> {
    Iterable<GithubIssue> findByPrincipal(String principal);
}
