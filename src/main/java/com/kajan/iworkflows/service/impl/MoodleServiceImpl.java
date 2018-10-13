package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.service.OauthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Service
public class MoodleServiceImpl implements com.kajan.iworkflows.service.MoodleService {

    private final String WS_TOKEN = "?wstoken=";
    private final String WS_FUNCTION = "?wsfunction=";

    @Value("${moodle.uri.webservice}")
    private String webserviceUri;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Override
    public String buildUrl(Principal principal, String wsfunction) {
        String wstoken = oauthTokenService.getToken(principal, MOODLE).getAccessToken().getValue();
        StringBuilder uri = new StringBuilder();
        uri.append(webserviceUri);
        uri.append(WS_TOKEN);
        uri.append(wstoken);
        uri.append(WS_FUNCTION);
        uri.append(wsfunction);
        return uri.toString();
    }
}
