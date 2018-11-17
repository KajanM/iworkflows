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

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.TokenProvider.LEARNORG;

@Service
@Slf4j
public class LearnOrgServiceImpl implements LearnOrgService {
    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;

    private final String userInforUri;

    @Autowired
    public LearnOrgServiceImpl(@Value("${learnorg.uri.userinfo}") String userInforUri, OauthTokenService oauthTokenService, RestTemplate restTemplate) {
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
        this.userInforUri = userInforUri;
    }

    @Override
    public UserStore getLearnOrgUserInfo(Principal principal) {
        TokenDTO tokenDTO = this.oauthTokenService.getToken(principal.getName(), LEARNORG);
        String accesstoken = tokenDTO.getAccessToken().getValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("access_token", accesstoken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        log.debug("uri : " + userInforUri);
        ResponseEntity<UserStore> response = this.restTemplate.postForEntity(userInforUri, request, UserStore.class);
        log.debug("Response ---------" + response.getBody());


        return response.getBody();
    }
}




