package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.kajan.iworkflows.util.WorkflowConstants.DUE_DATE_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;

@Service("setDueDate")
@Slf4j
public class SetDueDate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        SubmittedLeaveFormDetails details = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
        Date startDate = details.getStartDate();
        execution.setVariable(DUE_DATE_KEY, startDate);
        log.debug("Due date set to {}", startDate);
    }
}
