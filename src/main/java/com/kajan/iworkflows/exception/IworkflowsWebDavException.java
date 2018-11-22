package com.kajan.iworkflows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class IworkflowsWebDavException extends RuntimeException {

    public IworkflowsWebDavException() {
    }

    public IworkflowsWebDavException(String message) {
        super(message);
    }
}
