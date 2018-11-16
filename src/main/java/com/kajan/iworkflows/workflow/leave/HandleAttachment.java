package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;

@Service("handleAttachment")
@Slf4j
public class HandleAttachment implements JavaDelegate {

    private NextcloudService nextcloudService;

    @Value("${nextcloud.uri.file}")
    private String uploadUrl;

    @Override
    public void execute(DelegateExecution execution) {
        SubmittedLeaveFormDetails data = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);

        StringJoiner joiner = new StringJoiner("/");
        joiner.add(data.getFaculty());
        joiner.add(data.getDepartment());
        joiner.add(data.getStartDate());
        joiner.add(data.getEmployeeId() + ".pdf");
        // TODO: kajan, instead of uploading to the same location, create directories on the fly
        nextcloudService.uploadFileAsIworkflows("/attachments/leave/leave2.txt", "this is a sample leave file");
        log.debug("Successfully stored attachment to {}", joiner.toString());
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
