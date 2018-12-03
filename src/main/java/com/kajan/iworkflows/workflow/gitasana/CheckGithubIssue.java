package com.kajan.iworkflows.workflow.gitasana;

import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.service.IworkflowsGithubIssueService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.TokenProvider.GITHUB;
import static com.kajan.iworkflows.util.WorkflowConstants.IS_NEW_ISSUE_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.NEW_ISSUES_KEY;

@Service("checkGithubIssue")
@Slf4j
public class CheckGithubIssue implements JavaDelegate {

    @Autowired
    OauthTokenService tokenService;

    @Autowired
    IworkflowsGithubIssueService iworkflowsGithubIssueService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("checking for new github issue");

        String accessToken = tokenService.getToken("kajan", GITHUB).getAccessToken().getValue();
        if (accessToken == null) {
            throw new IworkflowsPreConditionRequiredException("No access token for kajan for github");
        }

        // get issues from github
        IssueService issueService = new IssueService();
        issueService.getClient().setOAuth2Token(accessToken);
        Map<String, String> filterData = new HashMap<>();
        filterData.put(IssueService.FILTER_ASSIGNEE, "kajanm");
        List<Issue> issues = issueService.getIssues("kajanm", "iworkflows", filterData);
        log.debug("Found {} issues assigned to user", issues.size(), "kajanm");

        // filter new issues to put in asana
        List<Issue> newIssues = iworkflowsGithubIssueService.filterNewIssues("kajan", issues);
        log.debug("Found {} new issues assigned to user", newIssues.size(), "kajanm");

        // no new issue
        if (newIssues.isEmpty()) {
            execution.setVariable("new_issue", false);
        } else {
            execution.setVariable(IS_NEW_ISSUE_KEY, true);
            execution.setVariable(NEW_ISSUES_KEY, newIssues);
        }

    }
}
