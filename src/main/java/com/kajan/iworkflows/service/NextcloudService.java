package com.kajan.iworkflows.service;

import com.github.sardine.DavResource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.List;

public interface NextcloudService {

    InputStream getFile(String principal, String filePath);

    ResponseEntity<String> uploadFile(String principal, String filePath, InputStream fileContent);

    List<DavResource> getDirectoryList(String principal, String filePath);

    void createDirectory(String principal, String directoryPath);

    boolean exists(String principal, String resourcePath);

    ResponseEntity<String> share(String principal, String resourcePath);
}
