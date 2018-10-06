package com.kajan.iworkflows.service;

import org.springframework.http.HttpHeaders;

import java.security.Principal;

public interface NextcloudService {
    HttpHeaders getNextcloudHeaders(Principal principal);
}
