package com.kajan.iworkflows.service;

import java.security.Principal;

public interface ServerSideEventService {

    <T> void emitServerSideEvents(Principal principal, T data);
}
