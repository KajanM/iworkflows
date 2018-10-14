package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.service.OauthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_MOODLE_WSFUNCTION;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_MOODLE_WSTOKEN;
import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Service
public class MoodleServiceImpl implements com.kajan.iworkflows.service.MoodleService {

    @Value("${moodle.uri.webservice}")
    private String webserviceUri;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Override
    public String buildUrl(Principal principal, String wsfunction) {
        String wstoken = oauthTokenService.getToken(principal, MOODLE).getAccessToken().getValue();
        return webserviceUri.replace(PLACEHOLDER_MOODLE_WSTOKEN, wstoken)
                .replace(PLACEHOLDER_MOODLE_WSFUNCTION, wsfunction);
    }
}
