package com.kajan.iworkflows.workflow.reference.twitter;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("demo/twitter")
@Slf4j
public class DemoTwitterController {

    @Autowired
    private ProcessEngine camunda;

    @PostMapping
    @RequestMapping("/start")
    public void startTwitterProcess() {
        log.debug("starting....................");
        camunda.getRuntimeService().startProcessInstanceByKey(
                TwitterProcessConstants.PROCESS_ID,
                Variables
                        .putValue("email", "kajan.14@cse.mrt.ac.lk")
                        .putValue("content", "hai kajan")
        );
    }

    @GetMapping
    @RequestMapping("/tasks")
    public List<Task> getTasks() {
        return camunda.getTaskService().createTaskQuery()
                .list();
    }

    /**
     * we need a method returning the {@link ProcessInstance} to allow for easier tests,
     * that's why I separated the REST method (without return) from the actual implementaion (with return value)
     */
//    public ProcessInstance placeOrder(String orderId, int amount) {
//        return camunda.getRuntimeService().startProcessInstanceByKey(//
//                ProcessConstants.PROCESS_KEY_order, //
//                Variables //
//                        .putValue(ProcessConstants.VAR_NAME_orderId, orderId) //
//                        .putValue(ProcessConstants.VAR_NAME_amount, amount));
//    }


}
