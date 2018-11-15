package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class MyTask implements Serializable {
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

    public static MyTask fromTask(Task task) {
        MyTask myTask = new MyTask();
        myTask.setOwner(task.getOwner());
        myTask.setTaskId(task.getId());
        myTask.setProcessInstanceId(task.getProcessInstanceId());
        myTask.setProcessDefinitionId(task.getProcessDefinitionId());
        myTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        myTask.setTaskType(task.getName());
        myTask.setSubmittedDate(task.getCreateTime());
        myTask.setDueDate(task.getDueDate());
        return myTask;
    }
}
