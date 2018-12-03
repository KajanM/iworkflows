package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.model.RequestStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.impl.RequestStoreServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.kajan.iworkflows.util.WorkflowConstants.*;

@Service("leavePostRejectService")
@Slf4j
public class LeavePostRejectService implements JavaDelegate {
    private final RequestStoreServiceImpl requestStoreService;
    private final LogStoreRepository logStoreRepository;

    @Autowired
    public LeavePostRejectService(RequestStoreServiceImpl requestStoreService, LogStoreRepository logStoreRepository) {
        this.requestStoreService = requestStoreService;
        this.logStoreRepository = logStoreRepository;
    }

    @Override
    public void execute(DelegateExecution execution) {
        log.debug("Leave request rejected");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(execution.getVariable(OWNER_KEY).toString(), timestamp, "Leave request rejected"));
        RequestStore requestStore = requestStoreService.findByPrincipalAndLeaveType(
                execution.getVariable(OWNER_KEY).toString(),
                execution.getVariable(LEAVE_TYPE_KEY).toString()).iterator().next();
        requestStore.setStatus(REJECTED_KEY);
        requestStore.setRejectedComment(execution.getVariable(REJECTED_COMMENT_KEY).toString());

    }
}
