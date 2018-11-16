package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class MyTaskBasicDetails implements Serializable {
    /**
     * The one who submitted the task
     */
    private String owner;
    private String taskId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String taskType;
    private Date submittedDate;
    private Date dueDate;
    private String recommendation;

    public static MyTaskBasicDetails fromTask(Task task) {
        MyTaskBasicDetails myTaskBasicDetails = new MyTaskBasicDetails();
        myTaskBasicDetails.setOwner(task.getOwner());
        myTaskBasicDetails.setTaskId(task.getId());
        myTaskBasicDetails.setProcessInstanceId(task.getProcessInstanceId());
        myTaskBasicDetails.setProcessDefinitionId(task.getProcessDefinitionId());
        myTaskBasicDetails.setTaskDefinitionKey(task.getTaskDefinitionKey());
        myTaskBasicDetails.setTaskType(task.getName());
        myTaskBasicDetails.setSubmittedDate(task.getCreateTime());
        myTaskBasicDetails.setDueDate(task.getDueDate());
        return myTaskBasicDetails;
    }
}
