package com.kajan.iworkflows.workflow.leave;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("notifyApproval")
public class NotifyApproval implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //TODO: Karady, send email
    }
}
