package com.kajan.iworkflows.workflow.gitasana;

import com.asana.Client;
import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.eclipse.egit.github.core.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.TokenProvider.ASANA;
import static com.kajan.iworkflows.util.WorkflowConstants.NEW_ISSUES_KEY;

@Service("createNewTaskInAsana")
@Slf4j
public class CreateNewTaskInAsana implements JavaDelegate {

    @Autowired
    OauthTokenService tokenService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("Attempting to create new task is Asana for user {}", "kajan");
        String accessToken = tokenService.getToken("kajan", ASANA).getAccessToken().getValue();
        if (accessToken == null) {
            throw new IworkflowsPreConditionRequiredException("No access token for kajan for asana");
        }
        List<Issue> newIssues = (List<Issue>) execution.getVariable(NEW_ISSUES_KEY);
        log.debug("new issues: {}", newIssues);

        Client asanaClient = Client.accessToken(accessToken);

        asanaClient.workspaces.findAll().forEach(workspace -> {
            log.debug(workspace.id);
        });

        Map<String, String> params = new HashMap<>();
        //params.put("workspace", asanaClient.workspaces.findAll())

        newIssues.forEach(issue -> {
            try {
                asanaClient.tasks.createInWorkspace("30877396477222").data("name", issue.getTitle()).execute();
            } catch (IOException e) {
                log.error("unable to create task in asana");
            }
        });
    }
}
