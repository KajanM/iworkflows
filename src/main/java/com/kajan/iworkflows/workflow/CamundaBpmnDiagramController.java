package com.kajan.iworkflows.workflow;

import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.io.Reader;

@RestController
@RequestMapping("api/v1/camunda")
@Slf4j
public class CamundaBpmnDiagramController {

    private RepositoryService repositoryService;

    @GetMapping("/diagram/{processDefinitionId}")
    public ResponseEntity<String> getDiagram(@PathVariable("processDefinitionId") String processDefinitionId) {
        try (final Reader reader = new InputStreamReader(repositoryService.getProcessModel(processDefinitionId))) {
            String diagram = CharStreams.toString(reader);
            return new ResponseEntity<>(diagram, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get BPMN diagram for processDefinitionId: {}", processDefinitionId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
}
