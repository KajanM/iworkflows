package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.controller.ServerSentEventController;
import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.ServerSideEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;

@Service
@Slf4j
public class ServerSideEventServiceImpl implements ServerSideEventService {
    private LogStoreRepository logStoreRepository;

    @Override
    public <T> void emitServerSideEvents(Principal principal, T data) {
        Timestamp timestamp;
        try {
            ServerSentEventController.emitters.get(principal).send(data, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            log.error("Unable to emit event to client. Principal: {]", principal, e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Unable to emit event to client. Principal: " + principal + " " + e));

            ServerSentEventController.emitters.remove(principal);
        }
    }

    public void setLogStoreRepository(LogStoreRepository logStoreRepository) {
        this.logStoreRepository = logStoreRepository;
    }
}
