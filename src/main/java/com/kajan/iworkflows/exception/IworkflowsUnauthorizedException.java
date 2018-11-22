package com.kajan.iworkflows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IworkflowsUnauthorizedException extends RuntimeException {

    public IworkflowsUnauthorizedException() {
        super();
    }

    public IworkflowsUnauthorizedException(String message) {
        super(message);
    }
}
