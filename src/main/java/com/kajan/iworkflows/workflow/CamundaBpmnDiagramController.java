package com.kajan.iworkflows.workflow;

import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/camunda")
@Slf4j
public class CamundaBpmnDiagramController {

    private RepositoryService repositoryService;
    private TaskService taskService;

    @GetMapping("/diagram/{processInstanceId}")
    public Map<String, String> getDiagram(@PathVariable("processInstanceId") String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
        String processDefinitionId = task.getProcessDefinitionId();
        String diagram = null;
        try (final Reader reader = new InputStreamReader(repositoryService.getProcessModel(processDefinitionId))) {
            diagram = CharStreams.toString(reader);
        } catch (Exception e) {
            log.error("Unable to get BPMN diagram for processDefinitionId: {}", processInstanceId, e);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("xml", diagram);
        resultMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
        return resultMap;
    }

    @Autowired
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
