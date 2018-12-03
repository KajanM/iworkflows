package com.kajan.iworkflows.service.impl;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.exception.IworkflowsWebDavException;
import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.TokenProvider.NEXTCLOUD;

@Service
@Slf4j
public class NextcloudServiceImpl implements NextcloudService {

    private final String HEADER_VALUE_BEARER = "Bearer ";
    private final String HEADER_VALUE_BASIC = "Basic ";

    /**
     * eg: http://iworkflows.projects.mrt.ac.lk/nextcloud/remote.php/dav/files
     */
    @Value("${nextcloud.uri.file}")
    private String FILE_ROOT_URI_TEMPLATE;

    /**
     * eg: http://iworkflows.projects.mrt.ac.lk/nextcloud/ocs/v2.php/apps/files_sharing/api/v1/shares
     */
    @Value("${nextcloud.uri.ocs-share-api}")
    private String OCS_SHARE_ROOT_URI;

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String IWORKFLOWS_USERNAME;

    @Value("${iworkflows.credentials.nextcloud.password}")
    private String IWORKFLOWS_PASSWORD;

    private OauthTokenService oauthTokenService;
    private Sardine webDavClient;
    private RestTemplate restTemplate;
    private LogStoreRepository logStoreRepository;
    private Timestamp timestamp;

    @Override
    public InputStream getFile(String principal, String filePath) {
        String uri = getResourceUri(principal, filePath);

        log.info("Making request to {} to download the file", uri);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal, timestamp, "Making request to {} to download the file " + uri));

        try {
            InputStream downloadedContent = webDavClient.get(uri, getAuthorizationHeader(principal));
            log.info("Successfully downloaded content from {}", uri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Successfully downloaded content from " + uri));

            return downloadedContent;
        } catch (IOException e) {
            log.error("Unable to get file from NextCloud", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to get file from NextCloud " + e));

            throw new IworkflowsWebDavException("Unable to get file from NextCloud");
        }
    }

    @Override
    public ResponseEntity<String> uploadFile(String principal, String filePath, InputStream fileContent) {
        String uri = getResourceUri(principal, filePath);
        log.info("Attempting to upload file to {}", uri);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal, timestamp, "Attempting to upload file to " + uri));
        try {
            webDavClient.put(uri, fileContent, getAuthorizationHeader(principal));
            log.info("Successfully uploaded file to {}", uri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Successfully uploaded file to " + uri));
        } catch (IOException e) {
            log.error("Unable to upload the file to NextCloud", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to upload the file to NextCloud " + e));
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
            resources = sardine.list(getResourceUri(principal, filePath));
        } catch (IOException e) {
            log.error("Unable to retrieve directory list from NextCloud", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to retrieve directory list from NextCloud " + e));
            throw new IworkflowsWebDavException("Unable to retrieve directory list from NextCloud");
        }
        if (resources == null) return null;

        for (DavResource res : resources) {
            log.debug("Found file from server: {}", res);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Found file from server: " + res));
        }
        return resources;

    }

    @Override
    public void createDirectory(String principal, String directoryPath) {
        if (!principal.equals(IWORKFLOWS_USERNAME)) {
            throw new NotImplementedException();
        }
        Sardine sardine = SardineFactory.begin(IWORKFLOWS_USERNAME, IWORKFLOWS_PASSWORD);
        String uri = getResourceUri(principal, directoryPath);
        try {
            log.info("Attempting to create directory in {}", uri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Attempting to create directory in " + uri));
            sardine.createDirectory(uri);
            log.info("Succesfully created directory at {}", uri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Succesfully created directory at " + uri));
        } catch (IOException e) {
            log.error("Unable to create specified directory in NextCloud", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to create specified directory in NextCloud" + e));
            throw new IworkflowsWebDavException("Unable to create specified directory in NextCloud");
        }
    }

    @Override
    public boolean exists(String principal, String resourcePath) {
        if (!principal.equals(IWORKFLOWS_USERNAME)) {
            throw new NotImplementedException();
        }
        Sardine sardine = SardineFactory.begin(IWORKFLOWS_USERNAME, IWORKFLOWS_PASSWORD);
        String uri = getResourceUri(principal, resourcePath);
        try {
            log.info("Checking if directory or file exists {}", uri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Checking if directory or file exists " + uri));
            boolean exists = sardine.exists(uri);
            log.info("Directory already exists in uri {} {}", uri, exists);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Directory already exists in uri " + uri + " " + exists));
            return exists;
        } catch (IOException e) {
            log.error("Unable to check if the resource exist in NextCloud", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Unable to check if the resource exist in NextCloud " + e));
            throw new IworkflowsWebDavException("Unable to check if the resource exist in NextCloud");
        }
    }

    @Override
    public ResponseEntity<String> share(String principal, String resourcePath) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("OCS-APIRequest", "true");
            headers.add("Authorization", getIworkflowsAuthorizationHeaderValue());

            //UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(OCS_SHARE_ROOT_URI);
            //builder.queryParam("format", "json");
            //builder.queryParam("path", resourcePath);
            //builder.queryParam("shareType", 0);
            //builder.queryParam("shareWith", principal);
            //builder.queryParam("permissions", 1);
            StrBuilder joiner = new StrBuilder();
            joiner.append(OCS_SHARE_ROOT_URI);
            joiner.append("?");
            joiner.append("format=json");
            joiner.append("&path=");
            joiner.append(resourcePath);
            joiner.append("&shareType=0");
            joiner.append("&shareWith=");
            joiner.append(principal);
            joiner.append("&permissions=1");

            //String uri = builder.toUriString();
            String uri = joiner.toString();
            log.debug("Attempting to share {} to {}", uri, principal);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Attempting to share " + uri + " to " + principal));

            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    entity,
                    String.class);
            log.debug("Successfully shared {} with {}", uri, principal);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal, timestamp, "Successfully shared " + uri + " with " + principal));
            return response;
        } catch (HttpClientErrorException e) {
            // already shared, ignore
            return null;
        }
    }

    /**
     * @param principal
     * @param filePath  should be properly encoded
     * @return
     */
    private String getResourceUri(String principal, String filePath) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(FILE_ROOT_URI_TEMPLATE);
        builder.pathSegment(principal);
        return builder.toUriString() + filePath;
    }

    private Map<String, String> getAuthorizationHeader(String principal) {
        Map<String, String> headers = new HashMap<>();

        // create basic auth header for iworkflows system user
        if (principal.equals(IWORKFLOWS_USERNAME)) {
            headers.put(HttpHeaders.AUTHORIZATION, getIworkflowsAuthorizationHeaderValue());
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

    private String getIworkflowsAuthorizationHeaderValue() {
        String auth = IWORKFLOWS_USERNAME + ":" + IWORKFLOWS_PASSWORD;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return HEADER_VALUE_BASIC + new String(encodedAuth);
    }

    @Autowired
    public void setOauthTokenService(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @Autowired
    public void setWebDavClient(Sardine webDavClient) {
        this.webDavClient = webDavClient;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setLogStoreRepository(LogStoreRepository logStoreRepository) {
        this.logStoreRepository = logStoreRepository;
    }
}
