package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.RequestStore;
import com.kajan.iworkflows.service.impl.RequestStoreServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_TYPE_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.REJECTED_KEY;

@Service("leavePostRejectService")
@Slf4j
public class LeavePostRejectService implements JavaDelegate {
    private final RequestStoreServiceImpl requestStoreService;

    @Autowired
    public LeavePostRejectService (RequestStoreServiceImpl requestStoreService){
        this.requestStoreService = requestStoreService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        log.debug("Leave request rejected");
        RequestStore requestStore = requestStoreService.findByPrincipalAndLeaveType(
                execution.getVariable(OWNER_KEY).toString(),
                execution.getVariable(LEAVE_TYPE_KEY).toString()).iterator().next();
        requestStore.setStatus(REJECTED_KEY);

    }
}
