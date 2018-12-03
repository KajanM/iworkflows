package com.kajan.iworkflows.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface MoodleService {
    <T> ResponseEntity<T> executeWsFunction(String wsFunctionName, HttpMethod httpMethod, Class<T> responseClass, String principal);
}
