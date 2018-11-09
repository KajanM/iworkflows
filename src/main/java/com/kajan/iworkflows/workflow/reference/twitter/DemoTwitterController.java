package com.kajan.iworkflows.workflow.reference.twitter;

import com.kajan.iworkflows.model.CamundaTask;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("demo/twitter")
@Slf4j
public class DemoTwitterController {

    private ProcessEngine camunda;

    @PostMapping
    @RequestMapping("/start")
    public void startTwitterProcess(Principal principal) {
        log.debug("starting....................");
        camunda.getRuntimeService().startProcessInstanceByKey(
                TwitterProcessConstants.PROCESS_ID,
                Variables
                        .putValue("email", "kajan.14@cse.mrt.ac.lk")
                        .putValue("content", "hai kajan")
        );
    }

    /**
     * Sample code to retrieve tasks without using camunda-api
     * Can use this method if need more control
     *
     * @param principal
     * @return
     */
    @GetMapping
    @RequestMapping("/tasks")
    public List<CamundaTask> getTasks(Principal principal) {
        List<CamundaTask> tasks = new ArrayList<>();
        camunda.getTaskService()
                .createTaskQuery().list().stream()
                .forEach(task -> {
                    CamundaTask camundaTask = new CamundaTask();
                    camundaTask.setId(task.getId());
                    // TODO: set other parameters if necessary
                    tasks.add(camundaTask);
                });
        return tasks;
    }

    @Autowired
    public void setCamunda(ProcessEngine camunda) {
        this.camunda = camunda;
    }

}
