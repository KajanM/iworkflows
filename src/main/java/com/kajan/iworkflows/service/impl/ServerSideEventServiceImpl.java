package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.controller.ServerSideEventController;
import com.kajan.iworkflows.service.ServerSideEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;

@Service
@Slf4j
public class ServerSideEventServiceImpl implements ServerSideEventService {
    @Override
    public <T> void emitServerSideEvents(Principal principal, T data) {
        try {
            ServerSideEventController.emitters.get(principal).send(data, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            log.error("Unable to emit event to client. Principal: {]", principal, e);
            ServerSideEventController.emitters.remove(principal);
        }
    }
}
