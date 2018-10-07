package com.kajan.iworkflows.service;

import com.kajan.iworkflows.util.Constants;
import org.springframework.http.HttpHeaders;

import java.security.Principal;

public interface NextcloudService {
    HttpHeaders getNextcloudHeaders(Principal principal, Constants.OauthProvider oauthProvider);
}
