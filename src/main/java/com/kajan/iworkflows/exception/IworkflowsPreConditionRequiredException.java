package com.kajan.iworkflows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
public class IworkflowsPreConditionRequiredException extends RuntimeException {

    public IworkflowsPreConditionRequiredException() {
        super();
    }

    public IworkflowsPreConditionRequiredException(String message) {
        super(message);
    }
}
