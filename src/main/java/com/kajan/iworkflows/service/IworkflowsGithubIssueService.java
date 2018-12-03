package com.kajan.iworkflows.service;

import com.kajan.iworkflows.model.GithubIssue;
import com.kajan.iworkflows.repository.GithubRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.egit.github.core.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class IworkflowsGithubIssueService {
    @Autowired
    private GithubRepository githubRepository;

    public List<Issue> filterNewIssues(String principal, List<Issue> allIssues) {
        Iterable<GithubIssue> processedIssues = githubRepository.findByPrincipal(principal);
        if (!processedIssues.iterator().hasNext()) {
            allIssues.forEach(issue -> {
                GithubIssue githubIssue = new GithubIssue();
                githubIssue.setPrincipal(principal);
                githubIssue.setIssueNumber(issue.getNumber());
                log.debug("Filtered issued id: {}", issue.getNumber());
                githubRepository.save(githubIssue);
            });
            return allIssues;
        }

        int lastIssueId = StreamSupport.stream(processedIssues.spliterator(), false)
                .map(GithubIssue::getIssueNumber)
                .mapToInt(value -> value)
                .max().orElseThrow(NoSuchElementException::new);

        List<Issue> filteredIssues = allIssues.stream()
                .filter(issue -> issue.getNumber() > lastIssueId)
                .collect(Collectors.toList());

        filteredIssues.forEach(issue -> {
            GithubIssue githubIssue = new GithubIssue();
            githubIssue.setPrincipal(principal);
            githubIssue.setIssueNumber(issue.getNumber());
            githubRepository.save(githubIssue);
        });

        return filteredIssues;
    }
}
