package com.kajan.iworkflows.exception;

public class NoSuchClientRegistrationException extends RuntimeException {

    public NoSuchClientRegistrationException(String registrationId) {
        super(registrationId + " is not a registered client");
    }
}
