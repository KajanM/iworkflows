package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;

@Service("handleAttachment")
@Slf4j
public class HandleAttachment implements JavaDelegate {

    private NextcloudService nextcloudService;

    @Override
    public void execute(DelegateExecution execution) {
        SubmittedLeaveFormDetails data = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
        List<String> attachmentNames = data.getDocuments();
        if(attachmentNames == null || attachmentNames.isEmpty()) {
            log.debug("No attachments included, skipping attachment handling process");
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String principal = (String) execution.getVariable(OWNER_KEY);
        StringJoiner path = new StringJoiner("/");
        List<String> paths = Arrays.asList(
                "leave-attachments",
                data.getFaculty(),
                data.getDepartment(),
                dateFormat.format(new Date()));

        paths.forEach(pathFragment -> {
            path.add(UriUtils.encodePathSegment(pathFragment, "UTF-8"));
        });

        //StringJoiner path = new StringJoiner("/");
        //paths.forEach(pathFragment -> {
        //    path.add(pathFragment);
        //    if(nextcloudService.notExists(path.toString())){
        //        nextcloudService.createDirectoryAsIworkflows(path.toString());
        //    }
        //});

        //todo: kajan, populate nextcloud with actual attachment
        //path.add(data.getEmployeeId() + ".txt");

        //nextcloudService.uploadFileAsIworkflows(path.toString(), "this is a sample leave file");
        //log.debug("Successfully stored attachment to {}", path.toString());
        //nextcloudService.uploadFile(principal, "attachments/leave/" + )
        //attachmentNames.forEach(attachment -> {
        //    InputStreamResource resource = nextcloudService.getFileAsIworkflows(path.toString() + "/" + attachment);
        //    //nextcloudService.uploadFile()
        //});
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
