package com.kajan.iworkflows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(NoSuchClientRegistrationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String clientRegistrationNotFoundHandler(NoSuchClientRegistrationException ex) {
        return ex.getMessage();
    }
}
