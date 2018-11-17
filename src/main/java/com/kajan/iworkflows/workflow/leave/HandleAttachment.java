package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;

@Service("handleAttachment")
@Slf4j
public class HandleAttachment implements JavaDelegate {

    private NextcloudService nextcloudService;

    @Override
    public void execute(DelegateExecution execution) {
        SubmittedLeaveFormDetails data = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);

        List<String> paths = Arrays.asList(
                "leave-attachments",
                data.getFaculty(),
                data.getDepartment(),
                data.getStartDate().replaceAll("/", "-"));

        StringJoiner path = new StringJoiner("/");
        paths.forEach(pathFragment -> {
            path.add(pathFragment);
            if(nextcloudService.notExists(path.toString())){
                nextcloudService.createDirectory(path.toString());
            }
        });

        //todo: kajan, populate nextcloud with actual attachment
        path.add(data.getEmployeeId() + ".txt");

        //nextcloudService.uploadFileAsIworkflows(path.toString(), "this is a sample leave file");
        log.debug("Successfully stored attachment to {}", path.toString());
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
