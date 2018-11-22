package com.kajan.iworkflows.service.impl;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.exception.IworkflowsWebDavException;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_FILE_PATH;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_USERID;
import static com.kajan.iworkflows.util.Constants.TokenProvider.NEXTCLOUD;

@Service
@Slf4j
public class NextcloudServiceImpl implements NextcloudService {

    private final String HEADER_VALUE_BEARER = "Bearer ";
    private final String HEADER_VALUE_BASIC = "Basic ";

    @Value("${nextcloud.uri.file}")
    private String FILE_ROOT_URI_TEMPLATE;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @Value("${iworkflows.credentials.nextcloud.password}")
    private String IWORKFLOWS_PASSWORD;

    private OauthTokenService oauthTokenService;
    private Sardine webDavClient;

    @Override
    public InputStream getFile(String principal, String filePath) {
        String uri = getUri(principal, filePath);

        log.info("Making request to {} to download the file", uri);
        try {
            InputStream downloadedContent = webDavClient.get(uri, getAuthorizationHeader(principal));
            log.info("Successfully downloaded content from {}", uri);
            return downloadedContent;
        } catch (IOException e) {
            log.error("Unable to get file from NextCloud", e);
            throw new IworkflowsWebDavException("Unable to get file from NextCloud");
        }
    }

    @Override
    public ResponseEntity<String> uploadFile(String principal, String filePath, InputStream fileContent) {

        String uri = getUri(principal, filePath);
        log.info("Attempting to upload file to {}", uri);

        try {
            webDavClient.put(uri, fileContent, getAuthorizationHeader(principal));
            log.info("Succesfully uploaded file to {}", uri);
        } catch (IOException e) {
            log.error("Unable to upload the file to NextCloud", e);
            throw new IworkflowsWebDavException("Unable to upload the file to NextCloud");
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public List<DavResource> getDirectoryList(String principal, String filePath) {
        if (!principal.equals(IWORKFLOWS_USERNAME)) {
            throw new NotImplementedException();
        }
        Sardine sardine = SardineFactory.begin(IWORKFLOWS_USERNAME, IWORKFLOWS_PASSWORD);

        List<DavResource> resources;
        try {
            resources = sardine.list(getUri(principal, filePath));
        } catch (IOException e) {
            log.error("Unable to retrieve directory list from NextCloud", e);
            throw new IworkflowsWebDavException("Unable to retrieve directory list from NextCloud");
        }
        if (resources == null) return null;

        for (DavResource res : resources) {
            log.debug("Found file from server: {}", res);
        }
        return resources;

    }

    @Override
    public void createDirectory(String principal, String directoryPath) {
        if (!principal.equals(IWORKFLOWS_USERNAME)) {
            throw new NotImplementedException();
        }
        Sardine sardine = SardineFactory.begin(IWORKFLOWS_USERNAME, IWORKFLOWS_PASSWORD);
        String uri = getUri(principal, directoryPath);
        try {
            log.info("Attempting to create directory in {}", uri);
            sardine.createDirectory(uri);
            log.info("Succesfully created directory at {}", uri);
        } catch (IOException e) {
            log.error("Unable to create specified directory in NextCloud", e);
            throw new IworkflowsWebDavException("Unable to create specified directory in NextCloud");
        }
    }

    @Override
    public boolean exists(String principal, String resourcePath) {
        if (!principal.equals(IWORKFLOWS_USERNAME)) {
            throw new NotImplementedException();
        }
        Sardine sardine = SardineFactory.begin(IWORKFLOWS_USERNAME, IWORKFLOWS_PASSWORD);
        try {
            return sardine.exists(getUri(principal, resourcePath));
        } catch (IOException e) {
            log.error("Unable to check if the resource exist in NextCloud or not", e);
            throw new IworkflowsWebDavException("Unable to check if the resource exist in NextCloud or not");
        }
    }

    private String getUri(String principal, String filePath) {
        return FILE_ROOT_URI_TEMPLATE.replace(PLACEHOLDER_USERID, UriUtils.encodePathSegment(principal, "UTF-8"))
                .replace(PLACEHOLDER_FILE_PATH, filePath);
    }

    private Map<String, String> getAuthorizationHeader(String principal) {
        Map<String, String> headers = new HashMap<>();

        // create basic auth header for iworkflows system user
        if (principal.equals(IWORKFLOWS_USERNAME)) {
            String auth = IWORKFLOWS_USERNAME + ":" + IWORKFLOWS_PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            headers.put(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BASIC + new String(encodedAuth));
            return headers;
        }

        // create bearer auth header for the requesting user
        TokenDTO tokenDTO = oauthTokenService.getToken(principal, NEXTCLOUD);
        if (tokenDTO == null) {
            throw new IworkflowsPreConditionRequiredException("No NextCloud access token found for " + principal);
        }
        headers.put(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BEARER + tokenDTO.getAccessToken().getValue());
        return headers;
    }

    @Autowired
    public void setOauthTokenService(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @Autowired
    public void setWebDavClient(Sardine webDavClient) {
        this.webDavClient = webDavClient;
    }
}
