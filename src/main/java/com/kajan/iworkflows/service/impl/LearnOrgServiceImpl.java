package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.service.LearnOrgService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.security.Principal;
import java.util.Base64;

import static com.kajan.iworkflows.util.Constants.TokenProvider.LEARNORG;

@Service
@Slf4j
public class LearnOrgServiceImpl implements LearnOrgService {
    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;
    private final String userInfoUri;
    private final String username;
    private final String password;
    private final String HEADER_VALUE_BASIC = "Basic ";

    @Autowired
    public LearnOrgServiceImpl(@Value("${learnorg.uri.userinfo}") String userInforUri,
                               OauthTokenService oauthTokenService, RestTemplate restTemplate,
                               @Value("${iworkflows.credentials.learnorg.username}") String username,
                               @Value("${iworkflows.credentials.learnorg.password}") String password) {
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
        this.userInfoUri = userInforUri;
        this.username = username;
        this.password = password;
    }

    @Override
    public UserStore getLearnOrgUserInfo(Principal principal) {
        TokenDTO tokenDTO = this.oauthTokenService.getToken(principal.getName(), LEARNORG);
        String accesstoken = tokenDTO.getAccessToken().getValue();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("access_token", accesstoken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, getLearnOrgHeaders());
        log.debug("uri : " + userInfoUri);
        ResponseEntity<UserStore> response = this.restTemplate.postForEntity(userInfoUri, request, UserStore.class);
        log.debug("Response ---------" + response.getBody());

        return response.getBody();
    }

    public HttpHeaders getLearnOrgHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
       return headers;
    }

    public HttpHeaders getLearnOrgHeadersAsIworkflows() {
        String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BASIC + encoding);
        return headers;
    }
}




