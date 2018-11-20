package com.kajan.iworkflows.service;

import com.github.sardine.DavResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface NextcloudService {
    InputStream getFile(String principal, String filePath);

    InputStreamResource getFileAsIworkflows(String filepath);

    ResponseEntity<String> uploadFile(String principal, String filePath, MultipartFile fileContent);

    ResponseEntity<String> uploadFileAsIworkflows(String filePath, MultipartFile fileContent);

    List<DavResource> getDirectoryListForIworkflows(String filePath);

    void createDirectoryAsIworkflows(String directoryPath);

    boolean notExists(String resourcePath);
}
