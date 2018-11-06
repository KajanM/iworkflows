package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.service.ServerSideEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ServerSideEventController {

    public static final Map<Principal, SseEmitter> emitters = Collections.synchronizedMap(new HashMap<>());

    private final ServerSideEventService serverSideEventService;

    @Autowired
    public ServerSideEventController(ServerSideEventService serverSideEventService) {
        this.serverSideEventService = serverSideEventService;
    }

    @CrossOrigin(allowCredentials = "true")
    @RequestMapping(path = "/stream", method = RequestMethod.GET)
    public ResponseEntity<SseEmitter> stream(Principal principal) {

        SseEmitter emitter = new SseEmitter();

        emitters.put(principal, emitter);
        emitter.onCompletion(() -> emitters.remove(principal));

        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @GetMapping("/sse/emit")
    @CrossOrigin
    public ResponseEntity<?> emit(Principal principal) {
        log.debug("Hit end point /sse/emit");
        serverSideEventService.emitServerSideEvents(principal, "Hello World!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
