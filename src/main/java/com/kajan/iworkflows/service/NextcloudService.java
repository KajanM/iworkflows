package com.kajan.iworkflows.service;

import org.springframework.http.ResponseEntity;

public interface NextcloudService {
    String getFile(String principal, String filePath);

    String getFileAsIworkflows(String filepath);

    ResponseEntity<String> uploadFileAsIworkflows(String filePath, String fileContent);
}
