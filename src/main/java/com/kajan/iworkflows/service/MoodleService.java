package com.kajan.iworkflows.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface MoodleService {
    <T> ResponseEntity<T> executeWsFunction(String wsFunctionName, HttpMethod httpMethod, Class<T> responseClass, Principal principal);
}
