package com.kajan.iworkflows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NoSuchClientRegistrationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String clientRegistrationNotFoundHandler(NoSuchClientRegistrationException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IworkflowsPreConditionRequiredException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    ErrorMessage preConditionRequiredExceptionHandler(IworkflowsPreConditionRequiredException e) {
        return new ErrorMessage(new Date(), 428, "Pre condition required", e.getMessage());
    }
}
