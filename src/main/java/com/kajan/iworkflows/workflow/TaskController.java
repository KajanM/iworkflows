package com.kajan.iworkflows.workflow;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
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
public class TaskController {

    private final ProcessEngine camunda;

    @Autowired
    public TaskController(ProcessEngine camunda) {
        this.camunda = camunda;
    }

    @GetMapping("all-tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskDto> getAllTasks() {
        List<TaskDto> result = new ArrayList<>();
        camunda.getTaskService()
                .createTaskQuery().list()
                .forEach(task -> {
                    TaskDto taskDto = TaskDto.fromEntity(task);
                    result.add(taskDto);
                });
        return result;
    }

    @GetMapping("my-tasks")
    public List<TaskDto> getTasks(Principal principal) {
        List<TaskDto> result = new ArrayList<>();
        camunda.getTaskService()
                .createTaskQuery().list().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().equalsIgnoreCase(principal.getName()))
                .forEach(task -> {
                    TaskDto taskDto = TaskDto.fromEntity(task);
                    result.add(taskDto);
                });
        return result;
    }

}
