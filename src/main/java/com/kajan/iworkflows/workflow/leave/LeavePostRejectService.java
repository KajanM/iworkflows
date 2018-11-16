package com.kajan.iworkflows.workflow.leave;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("leavePostRejectService")
@Slf4j
public class LeavePostRejectService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.debug("Leave request rejected");
    }
}
