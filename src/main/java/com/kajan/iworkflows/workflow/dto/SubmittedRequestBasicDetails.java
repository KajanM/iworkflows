package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
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
    private String status;
    private Date submittedDate;
    private Date dueDate;
    private String assignee;

    public static SubmittedRequestBasicDetails fromTask(Task task) {
        SubmittedRequestBasicDetails request = new SubmittedRequestBasicDetails();
        request.setTaskId(task.getId());
        request.setProcessInstanceId(task.getProcessInstanceId());
        request.setProcessDefinitionId(task.getProcessDefinitionId());
        //request.setStatus("in_progress");
        request.setName(task.getName());
        request.setSubmittedDate(task.getCreateTime());
        request.setDueDate(task.getDueDate());
        request.setAssignee(task.getAssignee());
        return request;
    }

    public static SubmittedRequestBasicDetails fromHistoricTaskInstance(HistoricTaskInstance instance) {
        SubmittedRequestBasicDetails details = new SubmittedRequestBasicDetails();
        details.setTaskId(instance.getId());
        details.setProcessInstanceId(instance.getProcessInstanceId());
        details.setProcessDefinitionId(instance.getProcessDefinitionId());
        details.setName(instance.getName());
        details.setSubmittedDate(instance.getStartTime());
        details.setDueDate(instance.getDueDate());
        return details;
    }
}
