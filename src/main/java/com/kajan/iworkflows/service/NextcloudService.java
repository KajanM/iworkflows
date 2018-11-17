package com.kajan.iworkflows.service;

import com.github.sardine.DavResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NextcloudService {
    String getFile(String principal, String filePath);

    String getFileAsIworkflows(String filepath);

    ResponseEntity<String> uploadFileAsIworkflows(String filePath, MultipartFile fileContent);

    List<DavResource> getDirectoryList(String filePath);

    void createDirectory(String directoryPath);

    boolean notExists(String resourcePath);
}
