package com.kajan.iworkflows.controller;

import com.google.common.io.ByteStreams;
import com.kajan.iworkflows.exception.IworkflowsWebDavException;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.LearnOrgService;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.kajan.iworkflows.util.Constants.DATE_PATTERN;
import static com.kajan.iworkflows.util.Constants.LEAVE_ATTACHMENTS_DIR_NAME;
import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class FileController {

    private NextcloudService nextcloudService;
    private DummyUserStoreServiceImpl userStoreService;
    private LearnOrgService learnOrgService;

    private RuntimeService runtimeService;

    @Value("${testing}")
    private Boolean testing;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        UserStore userStore;
        if (testing) {
            userStore = DummyUserStore.toUserStore(userStoreService.findByPrincipal(principal.getName()).iterator().next());
        } else {
            userStore = learnOrgService.getLearnOrgUserInfo(principal);
        }

        List<String> paths = Arrays.asList(
                LEAVE_ATTACHMENTS_DIR_NAME,
                userStore.getFaculty().toLowerCase().replace(" ", "-"),
                userStore.getDepartment().toLowerCase(),
                dateFormat.format(new Date()),
                userStore.getEmployeeId());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        paths.forEach(pathFragment -> {
            uriBuilder.pathSegment(pathFragment);
            if (!nextcloudService.exists(IWORKFLOWS_USERNAME, uriBuilder.toUriString())) {
                nextcloudService.createDirectory(IWORKFLOWS_USERNAME, uriBuilder.toUriString());
            }
        });


        if (file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().build();
        }

        uriBuilder.pathSegment(file.getOriginalFilename());

        try {
            nextcloudService.uploadFile(IWORKFLOWS_USERNAME, uriBuilder.toUriString(), file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to upload file to NextCloud", e);
            throw new IworkflowsWebDavException("Unable to upload file to NextCloud");
        }
        log.debug("Successfully stored attachment to {}", uriBuilder.toUriString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{processInstanceId}/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("fileName") String fileName) {
        SubmittedLeaveFormDetails subittedLeaveFormDetails = (SubmittedLeaveFormDetails) runtimeService.getVariable(processInstanceId, LEAVE_DETAILS_KEY);
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.pathSegment(
                LEAVE_ATTACHMENTS_DIR_NAME,
                subittedLeaveFormDetails.getFaculty(),
                subittedLeaveFormDetails.getDepartment(),
                dateFormat.format(new Date()),
                subittedLeaveFormDetails.getEmployeeId(),
                fileName
        );

        InputStream fileStream = nextcloudService.getFile(IWORKFLOWS_USERNAME, uriBuilder.toUriString());
        try {
            return ResponseEntity.ok(ByteStreams.toByteArray(fileStream));
        } catch (IOException e) {
            log.error("Unable to convert to byte array");
            return ResponseEntity.badRequest().build();
        }
    }

    @Autowired
    public void setNextcloudService(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }

    @Autowired
    public void setUserStoreService(DummyUserStoreServiceImpl userStoreService) {
        this.userStoreService = userStoreService;
    }

    @Autowired
    public void setLearnOrgService(LearnOrgServiceImpl learnOrgService) {
        this.learnOrgService = learnOrgService;
    }

    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
