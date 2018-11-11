package com.kajan.iworkflows.workflow.leave;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;

@RestController
@RequestMapping("/api/v1/camunda/")
public class ProcessController {

    private final RuntimeService runtimeService;

    @Autowired
    public ProcessController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping("start/{processKey}")
    public ResponseEntity<?> startProcess(@PathVariable String processKey, @RequestBody Optional<Map<String, Object>> payload, Principal principal) {
        ProcessInstance leaveProcess = runtimeService.startProcessInstanceByKey(processKey,
                Variables
                        .putValue(OWNER_KEY, principal.getName()));
        payload.ifPresent(postParams -> postParams.forEach((key, value) -> {
            runtimeService.setVariable(leaveProcess.getId(), key, value);
        }));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
