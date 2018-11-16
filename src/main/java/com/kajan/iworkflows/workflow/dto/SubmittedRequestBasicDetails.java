package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class SubmittedRequestBasicDetails implements Serializable {

    private String taskId;
    private String processInstanceId;
    private String processDefinitionId;
    private String name;
    private Date submittedDate;
    private Date dueDate;
    private String assignee;

    public static SubmittedRequestBasicDetails fromTask(Task task) {
        SubmittedRequestBasicDetails request = new SubmittedRequestBasicDetails();
        request.setTaskId(task.getId());
        request.setProcessInstanceId(task.getProcessInstanceId());
        request.setProcessDefinitionId(task.getProcessDefinitionId());
        request.setName(task.getName());
        request.setSubmittedDate(task.getCreateTime());
        request.setDueDate(task.getDueDate());
        request.setAssignee(task.getAssignee());
        return request;
    }
}
