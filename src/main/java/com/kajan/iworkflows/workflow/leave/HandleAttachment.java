package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.util.WorkflowConstants;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.kajan.iworkflows.util.Constants.PATH_DELIMTER;
import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;

@Service("handleAttachment")
@Slf4j
public class HandleAttachment implements JavaDelegate {

    private NextcloudService nextcloudService;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            SubmittedLeaveFormDetails data = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
            List<String> attachmentNames = data.getDocuments();
            if(attachmentNames == null || attachmentNames.isEmpty()) {
                log.debug("No attachments included, skipping attachment handling process");
                return;
            }

            String principal = (String) execution.getVariable(OWNER_KEY);
            StringJoiner path = new StringJoiner(PATH_DELIMTER);
            List<String> paths = Arrays.asList(
                    WorkflowConstants.LEAVE_ATTACHMENTS_KEY,
                    data.getFaculty(),
                    data.getDepartment(),
                    data.getSubmittedDate(),
                    data.getEmployeeId());

            paths.forEach(pathFragment -> path.add(UriUtils.encodePathSegment(pathFragment, "UTF-8")));

            attachmentNames.forEach(attachment -> {
                String encodedFileName = UriUtils.encodePathSegment(attachment, "UTF-8");

                InputStream file = nextcloudService.getFile(IWORKFLOWS_USERNAME, path.toString() + "/" + encodedFileName);
                nextcloudService.uploadFile(principal, encodedFileName, file);
            });
        } catch (Exception e) {
            log.error("Unable to upload the file to user's NextCloud account", e);
        }
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }
}
