package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.Comment;
import com.kajan.iworkflows.model.CompletedTaskStore;
import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.model.RequestStore;
import com.kajan.iworkflows.repository.CompletedTaskStoreRepository;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.repository.RequestStoreRepository;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private final LogStoreRepository logStoreRepository;
    private final CompletedTaskStoreRepository completedTaskStoreRepository;
    private Timestamp timestamp;

    @Autowired
    public CamundaLeaveProcessController(RuntimeService runtimeService, TaskService taskService,
                                         RequestStoreRepository requestStoreRepository,
                                         LogStoreRepository logStoreRepository, CompletedTaskStoreRepository completedTaskStoreRepository) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.requestStoreRepository = requestStoreRepository;
        this.logStoreRepository = logStoreRepository;
        this.completedTaskStoreRepository = completedTaskStoreRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startProcess(@RequestBody SubmittedLeaveFormDetails leaveDetails, Principal principal) {
        log.debug("Leave request received principal: {}, leave details: {}", principal, leaveDetails);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Leave request received principal: "
                + principal + ", leave details: " + leaveDetails));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        leaveDetails.setSubmittedDate(dateFormat.format(new Date()));
        requestStoreRepository.save(new RequestStore(leaveDetails, principal.getName(), "in-progress"));
        ProcessInstance leaveProcess = runtimeService.startProcessInstanceByKey(LEAVE_PROCESS_DEFINITION_KEY,
                Variables
                        .putValue(OWNER_KEY, principal.getName())
                        .putValue(LEAVE_DETAILS_KEY, leaveDetails)
                        .putValue(SUBMITTED_DATE_KEY, leaveDetails.getSubmittedDate()));

        List<Task> tasks = taskService.createTaskQuery().executionId(leaveProcess.getId()).list();
        tasks.forEach(task -> taskService.setOwner(task.getId(), principal.getName()));
        log.debug("task setowner {} ", tasks);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal.getName(), timestamp, "task setowner " + tasks));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/complete/{taskId}/{approved}")
    public ResponseEntity<?> completeTask(@RequestBody Comment comment, @PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved, Principal principal) {
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).list().get(0).getProcessInstanceId();
        Object headApproved = runtimeService.getVariable(processInstanceId, HEAD_APPROVED_KEY);
        timestamp = new Timestamp(System.currentTimeMillis());
        if (headApproved == null) {
            taskService.setVariable(taskId, HEAD_APPROVED_KEY, approved);
            if (approved) {
                logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Department Head approved leave request " + taskId));
                logStoreRepository.save(new LogStore(runtimeService.getVariable(processInstanceId, HEAD_APPROVER_KEY).toString(), timestamp, "Approved leave request " + taskId));
                completedTaskStoreRepository.save(new CompletedTaskStore(taskId, principal.getName(), runtimeService.getVariable(processInstanceId, OWNER_KEY).toString(), runtimeService.getVariable(processInstanceId, SUBMITTED_DATE_KEY).toString(), "approved"));
            } else {
                runtimeService.setVariable(processInstanceId, REJECTED_COMMENT_KEY, comment.getComment());
                logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Department Head rejected leave request " + taskId));
                logStoreRepository.save(new LogStore(runtimeService.getVariable(processInstanceId, HEAD_APPROVER_KEY).toString(), timestamp, "Rejected leave request " + taskId));
                completedTaskStoreRepository.save(new CompletedTaskStore(taskId, principal.getName(), runtimeService.getVariable(processInstanceId, OWNER_KEY).toString(), runtimeService.getVariable(processInstanceId, SUBMITTED_DATE_KEY).toString(), "rejected"));
            }

        } else {
            taskService.setVariable(taskId, CLERK_APPROVED_KEY, approved);
            timestamp = new Timestamp(System.currentTimeMillis());
            if (approved) {
                logStoreRepository.save(new LogStore(runtimeService.getVariable(processInstanceId, OWNER_KEY).toString(), timestamp, "Department Clerk approved leave request " + taskId));
                logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Approved leave request " + taskId));
                completedTaskStoreRepository.save(new CompletedTaskStore(taskId, principal.getName(), runtimeService.getVariable(processInstanceId, OWNER_KEY).toString(), runtimeService.getVariable(processInstanceId, SUBMITTED_DATE_KEY).toString(), "approved"));


            } else {
                runtimeService.setVariable(processInstanceId, REJECTED_COMMENT_KEY, comment.getComment());
                logStoreRepository.save(new LogStore((String)runtimeService.getVariable(processInstanceId, OWNER_KEY), timestamp, "Department Clerk rejected leave request " + taskId));
                logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Rejected leave request " + taskId));
                completedTaskStoreRepository.save(new CompletedTaskStore(taskId, principal.getName(), runtimeService.getVariable(processInstanceId, OWNER_KEY).toString(), runtimeService.getVariable(processInstanceId, SUBMITTED_DATE_KEY).toString(), "rejected"));
            }
        }
        taskService.complete(taskId);
        log.debug("Task {} completed successfully", taskId);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Task " + taskId + " completed successfully"));
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
