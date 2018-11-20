package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
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

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class FileController {

    private NextcloudService nextcloudService;
    private DummyUserStoreServiceImpl userStoreService;
    private LearnOrgServiceImpl learnOrgService;

    @Value("${testing}")
    private Boolean testing;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        // TODO: replace logic of getting user data from dummy user store
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringJoiner path = new StringJoiner("/");
        if (testing) {
            DummyUserStore userStore = userStoreService.findByPrincipal(principal.getName()).iterator().next();
            List<String> paths = Arrays.asList(
                    "leave-attachments",
                    userStore.getFaculty(),
                    userStore.getDepartment(),
                    dateFormat.format(new Date()),
                    userStore.getEmployeeId());

            paths.forEach(pathFragment -> {
                path.add(UriUtils.encodePathSegment(pathFragment, "UTF-8"));
                if (nextcloudService.notExists(path.toString())) {
                    nextcloudService.createDirectoryAsIworkflows(path.toString());
                }
            });
        } else {
            UserStore userStore = learnOrgService.getLearnOrgUserInfo(principal);
            List<String> paths = Arrays.asList(
                    "leave-attachments",
                    userStore.getFaculty(),
                    userStore.getDepartment(),
                    dateFormat.format(new Date()),
                    userStore.getEmployeeId());

            paths.forEach(pathFragment -> {
                path.add(UriUtils.encodePathSegment(pathFragment, "UTF-8"));
                if (nextcloudService.notExists(path.toString())) {
                    nextcloudService.createDirectoryAsIworkflows(path.toString());
                }
            });
        }


        if (file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().build();
        }

        path.add(UriUtils.encodePathSegment(file.getOriginalFilename(), "UTF-8"));

        nextcloudService.uploadFileAsIworkflows(path.toString(), file);
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
