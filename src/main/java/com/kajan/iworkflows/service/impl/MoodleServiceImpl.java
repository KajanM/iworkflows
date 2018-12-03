package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.MoodleService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_MOODLE_WSFUNCTION;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_MOODLE_WSTOKEN;
import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Service
@Slf4j
public class MoodleServiceImpl implements MoodleService {

    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;

    private final String webserviceUri;
    private Timestamp timestamp;
    private final LogStoreRepository logStoreRepository;

    @Autowired
    public MoodleServiceImpl(@Value("${moodle.uri.webservice}") String webserviceUri,
                             OauthTokenService oauthTokenService,
                             RestTemplate restTemplate, LogStoreRepository logStoreRepository) {
        this.webserviceUri = webserviceUri;
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
        this.logStoreRepository = logStoreRepository;
    }

    private String buildUrl(Principal principal, String wsfunction) {
        String wstoken = oauthTokenService.getToken(principal.getName(), MOODLE).getAccessToken().getValue();
        String uri = webserviceUri.replace(PLACEHOLDER_MOODLE_WSTOKEN, wstoken)
                .replace(PLACEHOLDER_MOODLE_WSFUNCTION, wsfunction);
        log.debug("BuiltURL: {}", uri);
        return uri;
    }

    @Override
    public <T> ResponseEntity<T> executeWsFunction(String wsFunctionName, HttpMethod httpMethod, Class<T> responseClass, Principal principal) {
        String url = this.buildUrl(principal, wsFunctionName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map req_payload = new HashMap();

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        ResponseEntity<T> responseEntity;

        switch (httpMethod) {
            case POST:
                responseEntity = restTemplate.postForEntity(url, request, responseClass);
                break;
            case GET:
                responseEntity = restTemplate.getForEntity(url, responseClass);
                break;
            default:
                throw new UnsupportedOperationException("sending request using " + httpMethod + " is not supported yet");
        }
        log.debug("Response: {}", responseEntity);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Response: " + responseEntity));
        return responseEntity;
    }
}
