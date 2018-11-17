package com.kajan.iworkflows.controller.nextcloud;

import com.github.sardine.DavResource;
import com.kajan.iworkflows.service.NextcloudService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nextcloud")
public class NextCloudWebDavController {

    private final String WELCOME_FILE_PATH = "welcome.txt";

    private final NextcloudService nextcloudService;

    @Value("${nextcloud.uri.file}")
    private String FILE_ROOT_URI_TEMPLATE;

    public NextCloudWebDavController(NextcloudService nextcloudService) {
        this.nextcloudService = nextcloudService;
    }

    @GetMapping("/files")
    public ResponseEntity<String> getWelcomeTxt(Principal principal) {
        String file = nextcloudService.getFile(principal.getName(), WELCOME_FILE_PATH);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @GetMapping("/files/iworkflows")
    public ResponseEntity<String> getWelcomeTxtAsIworkflows(Principal principal) {
        String file = nextcloudService.getFileAsIworkflows(WELCOME_FILE_PATH);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @GetMapping("/files/dir")
    public List<DavResource> getDirList() {
        return nextcloudService.getDirectoryList("");
    }

}
