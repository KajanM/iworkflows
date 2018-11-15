package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.exception.UnauthorizedException;
import com.kajan.iworkflows.service.NextcloudService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_FILE_PATH;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_USERID;
import static com.kajan.iworkflows.util.Constants.TokenProvider.NEXTCLOUD;

@Service
@Slf4j
public class NextcloudServiceImpl implements NextcloudService {

    private final String HEADER_OCS_API_REQUEST = "OCS-APIRequest";
    private final String HEADER_VALUE_BEARER = "Bearer ";
    private final String HEADER_VALUE_TRUE = "true";

    @Value("${nextcloud.uri.file}")
    private String FILE_ROOT_URI_TEMPLATE;

    private OauthTokenService oauthTokenService;
    private RestTemplate restTemplate;

    private HttpHeaders getNextcloudHeaders(String principal) {
        TokenDTO tokenDTO = oauthTokenService.getToken(principal, NEXTCLOUD);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_OCS_API_REQUEST, HEADER_VALUE_TRUE);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BEARER + tokenDTO.getAccessToken().getValue());
        return headers;
    }

    @Override
    public String getFile(String principal, String filePath) {
        String uri = FILE_ROOT_URI_TEMPLATE.replace(PLACEHOLDER_USERID, principal.toLowerCase())
                .replace(PLACEHOLDER_FILE_PATH, filePath);
        HttpEntity<String> httpEntity = new HttpEntity<>("", getNextcloudHeaders(principal));
        log.info("Making request to {} to download the file with headers {}", uri, httpEntity);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        } catch (RestClientException e) {
            log.error("Error occured while trying to download a file from NextCloud", e);
            throw new UnauthorizedException();
        }
        return responseEntity.getBody();
    }

    @Override
    public String getFileAsIworkflows(String filepath) {
        // TODO: kajan, get iworkflows credentials from property file instead
        String uri = getIworkflowsUri(filepath);
        ResponseEntity<String> file = restTemplate.exchange
                (uri, HttpMethod.GET, new HttpEntity<String>(createHeaders("admin", "1234")), String.class);

        return file.getBody();
    }

    @Override
    public ResponseEntity<String> uploadFileAsIworkflows(String filePath, String fileContent) {
        String uri = getIworkflowsUri(filePath);
        ResponseEntity<String> response = restTemplate.exchange
                (uri, HttpMethod.PUT, new HttpEntity<>(fileContent, createHeaders("admin", "1234")), String.class);

        return response;
    }

    private String getIworkflowsUri(String filepath) {
        return FILE_ROOT_URI_TEMPLATE.replace(PLACEHOLDER_USERID, "admin")
                .replace(PLACEHOLDER_FILE_PATH, filepath);
    }

    @Autowired
    public void setOauthTokenService(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
