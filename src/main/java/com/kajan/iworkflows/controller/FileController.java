package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.exception.IworkflowsWebDavException;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.LearnOrgService;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import static com.kajan.iworkflows.util.Constants.*;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class FileController {

    private NextcloudService nextcloudService;
    private DummyUserStoreServiceImpl userStoreService;
    private LearnOrgService learnOrgService;

    @Value("${testing}")
    private Boolean testing;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        StringJoiner path = new StringJoiner(PATH_DELIMTER);
        UserStore userStore;
        if (testing) {
            userStore = DummyUserStore.toUserStore(userStoreService.findByPrincipal(principal.getName()).iterator().next());
        } else {
            userStore = learnOrgService.getLearnOrgUserInfo(principal);
        }

        List<String> paths = Arrays.asList(
                LEAVE_ATTACHMENTS_DIR_NAME,
                userStore.getFaculty(),
                userStore.getDepartment(),
                dateFormat.format(new Date()),
                userStore.getEmployeeId());

        paths.forEach(pathFragment -> {
            path.add(UriUtils.encodePathSegment(pathFragment, "UTF-8"));
            if (!nextcloudService.exists(IWORKFLOWS_USERNAME, path.toString())) {
                nextcloudService.createDirectory(IWORKFLOWS_USERNAME, path.toString());
            }
        });

        if (file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().build();
        }

        path.add(UriUtils.encodePathSegment(file.getOriginalFilename(), "UTF-8"));

        try {
            nextcloudService.uploadFile(IWORKFLOWS_USERNAME, path.toString(), file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to upload file to NextCloud", e);
            throw new IworkflowsWebDavException("Unable to upload file to NextCloud");
        }
        log.debug("Successfully stored attachment to {}", path.toString());

        return ResponseEntity.ok().build();
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
}
