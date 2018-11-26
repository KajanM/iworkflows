package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.RequestStore;
import com.kajan.iworkflows.repository.RequestStoreRepository;
import com.kajan.iworkflows.service.impl.RequestStoreServiceImpl;
import com.kajan.iworkflows.workflow.dto.MyTaskFullDetails;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.kajan.iworkflows.util.WorkflowConstants.*;

@RestController
@RequestMapping("/api/v1/camunda/leave")
@Slf4j
public class CamundaLeaveProcessController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RequestStoreRepository requestStoreRepository;

    @Autowired
    public CamundaLeaveProcessController(RuntimeService runtimeService, TaskService taskService,
                                         RequestStoreRepository requestStoreRepository) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.requestStoreRepository = requestStoreRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startProcess(@RequestBody SubmittedLeaveFormDetails leaveDetails, Principal principal) {
        log.debug("Leave request received principal: {}, leave details: {}", principal, leaveDetails);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        leaveDetails.setSubmittedDate(dateFormat.format(new Date()));
        requestStoreRepository.save(new RequestStore(leaveDetails, principal.getName(), "in-progress"));
        ProcessInstance leaveProcess = runtimeService.startProcessInstanceByKey(LEAVE_PROCESS_DEFINITION_KEY,
                Variables
                        .putValue(OWNER_KEY, principal.getName())
                        .putValue(LEAVE_DETAILS_KEY, leaveDetails));

        List<Task> tasks = taskService.createTaskQuery().executionId(leaveProcess.getId()).list();
        tasks.forEach(task -> taskService.setOwner(task.getId(), principal.getName()));
        log.debug("task setowner {} ", tasks);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/complete/{taskId}/{approved}")
    public ResponseEntity<?> completeTask(@PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved, Principal principal) {
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).list().get(0).getProcessInstanceId();
        Object headApproved = runtimeService.getVariable(processInstanceId, HEAD_APPROVED_KEY);
        if (headApproved == null) {
            taskService.setVariable(taskId, HEAD_APPROVED_KEY, approved);
        } else {
            taskService.setVariable(taskId, CLERK_APPROVED_KEY, approved);
        }
        taskService.complete(taskId);
        log.debug("Task {} completed successfully", taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/details/{processInstanceId}")
    public MyTaskFullDetails getDetails(@PathVariable("processInstanceId") String processInstanceId) {
        SubmittedLeaveFormDetails leaveFormDetails = (SubmittedLeaveFormDetails) runtimeService.getVariable(processInstanceId, LEAVE_DETAILS_KEY);
        MyTaskFullDetails fullDetails = new MyTaskFullDetails();
        fullDetails.setLeaveFormDetails(leaveFormDetails);
        return fullDetails;
    }
}
