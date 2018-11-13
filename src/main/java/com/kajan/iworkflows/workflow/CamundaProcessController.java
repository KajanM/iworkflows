package com.kajan.iworkflows.workflow;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;

@RestController
@RequestMapping("/api/v1/camunda/")
@Slf4j
public class CamundaProcessController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    @Autowired
    public CamundaProcessController(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }


    @PostMapping("start/{processKey}")
    public ResponseEntity<?> startProcess(@PathVariable String processKey, @RequestBody Map<String, Object> payload, Principal principal) {
        try {


            ProcessInstance leaveProcess = runtimeService.startProcessInstanceByKey(processKey,
                    Variables
                            .putValue(OWNER_KEY, principal.getName()));

            if (payload != null) {
                payload.forEach((key, value) -> runtimeService.setVariable(leaveProcess.getId(), key, value));
            }

            // manually set the owner of the task
            List<Task> tasks = taskService.createTaskQuery().executionId(leaveProcess.getId()).list();
            tasks.forEach(task -> taskService.setOwner(task.getId(), principal.getName()));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to start the process", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
