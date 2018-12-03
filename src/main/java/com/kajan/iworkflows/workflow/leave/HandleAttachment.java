package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.kajan.iworkflows.util.Constants.URI_PATH_DELIMITER;
import static com.kajan.iworkflows.util.WorkflowConstants.*;

@Service("handleAttachment")
@Slf4j
public class HandleAttachment implements JavaDelegate {

    private NextcloudService nextcloudService;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;
    private Timestamp timestamp;
    private LogStoreRepository logStoreRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String principal = (String) execution.getVariable(OWNER_KEY);
        try {
            SubmittedLeaveFormDetails data = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
            List<String> attachmentNames = data.getDocuments();
            if (attachmentNames == null || attachmentNames.isEmpty()) {
                log.debug("No attachments included, skipping attachment handling process");
                timestamp = new Timestamp(System.currentTimeMillis());
                logStoreRepository.save(new LogStore(principal, timestamp, "No attachments included, skipping attachment handling process "));
                return;
            }

            StringJoiner path = new StringJoiner(URI_PATH_DELIMITER);
            List<String> paths = Arrays.asList(
                    NEXTCLOUD_LEAVE_ATTACHMENTS_DIR_NAME,
                    data.getFaculty().toLowerCase().replace(" ", "-"),
                    data.getDepartment().toLowerCase(),
                    data.getSubmittedDate(),
                    data.getEmployeeId());

            paths.forEach(pathFragment -> path.add(pathFragment));
            nextcloudService.share(principal, path.toString());
        } catch (Exception e) {
            log.error("Unable to share the file with the user", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to share the file with the user " + e));
        }
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }

    @Autowired
    public void setLogStoreRepository(LogStoreRepository logStoreRepository) {
        this.logStoreRepository = logStoreRepository;
    }
}
