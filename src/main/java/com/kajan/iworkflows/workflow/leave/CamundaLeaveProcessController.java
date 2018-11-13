package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;

@RestController
@RequestMapping("/api/v1/camunda/")
@Slf4j
public class CamundaLeaveProcessController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    @Autowired
    public CamundaLeaveProcessController(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @PostMapping("start/leave_process")
    public ResponseEntity<?> startProcess(@RequestBody SubmittedLeaveFormDetails leaveDetails, Principal principal) {
        try {
            ProcessInstance leaveProcess = runtimeService.startProcessInstanceByKey("leave_process",
                    Variables
                            .putValue(OWNER_KEY, principal.getName())
                            .putValue(LEAVE_DETAILS_KEY, leaveDetails));

            List<Task> tasks = taskService.createTaskQuery().executionId(leaveProcess.getId()).list();
            tasks.forEach(task -> taskService.setOwner(task.getId(), principal.getName()));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to start the process", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
