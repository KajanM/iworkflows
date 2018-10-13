package com.kajan.iworkflows.service;

import java.security.Principal;

public interface MoodleService {
    String buildUrl(Principal principal, String wsfunction);
}
