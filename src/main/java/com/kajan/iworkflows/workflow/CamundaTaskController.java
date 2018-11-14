package com.kajan.iworkflows.workflow;

import com.kajan.iworkflows.workflow.dto.MyTask;
import com.kajan.iworkflows.workflow.dto.SubmittedRequest;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/camunda/")
@Slf4j
public class CamundaTaskController {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    @Autowired
    public CamundaTaskController(TaskService taskService, RuntimeService runtimeService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    /**
     * Get the list of all tasks.
     * Only the user with 'ROLE_ADMIN' can access this end point.
     *
     * @return the list of all tasks.
     */
    @GetMapping("all-tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskDto> getAllTasks() {
        List<TaskDto> result = new ArrayList<>();
        taskService
                .createTaskQuery().list()
                .forEach(task -> {
                    TaskDto taskDto = TaskDto.fromEntity(task);
                    result.add(taskDto);
                });
        return result;
    }

    /**
     * Get the list of tasks that the requesting user is assigned to
     *
     * @param principal the logged in user
     * @return the list of tasks that the requesting user is assigned to
     */
    @GetMapping("my-tasks")
    public List<MyTask> getTasks(Principal principal) {
        List<MyTask> myTasks = new ArrayList<>();
        taskService
                .createTaskQuery().list().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().equalsIgnoreCase(principal.getName()))
                .forEach(task -> {
                    MyTask myTask = MyTask.fromTask(task);
                    myTask.setRecommendation((String) runtimeService.getVariable(task.getProcessInstanceId(), "recommendation"));
                    myTasks.add(myTask);
                });
        return myTasks;
    }

    /**
     * Get the list of tasks that the requesting user submitted
     *
     * @param principal the logged in user
     * @return the list of tasks that the requesting user submitted
     */
    @GetMapping("submitted-tasks")
    public List<SubmittedRequest> getSubmittedRequests(Principal principal) {
        List<SubmittedRequest> submittedRequests = new ArrayList<>();
        taskService
                .createTaskQuery().list().stream()
                .filter(task -> task.getOwner() != null && task.getOwner().equalsIgnoreCase(principal.getName()))
                .forEach(task -> {
                    SubmittedRequest request = SubmittedRequest.fromTask(task);
                    submittedRequests.add(request);
                });
        return submittedRequests;
    }
}
