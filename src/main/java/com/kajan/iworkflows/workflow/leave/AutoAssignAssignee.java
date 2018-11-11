package com.kajan.iworkflows.workflow.leave;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate  {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // owner is the one who submitted the leave request
        String owner = (String) execution.getVariable("owner");
        log.debug("Owner of the tasks is {}", owner);

        // TODO: Ramiya, logic to find out the approver goes here
        String approver = "kajan";
        log.debug("Auto assigning the task to {}", approver);

        execution.setVariable("approver", approver);
    }
}
