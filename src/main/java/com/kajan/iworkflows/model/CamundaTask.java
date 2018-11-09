package com.kajan.iworkflows.model;

import lombok.Data;
import org.camunda.bpm.engine.task.DelegationState;

import java.util.Date;

@Data
public class CamundaTask {

    private String id;
    private int revision;
    private String owner;
    private String assignee;
    private DelegationState delegationState;
    private String parentTaskId;
    private String name;
    private String description;
    private int priority;
    private Date createTime;
    private Date dueDate;
    private Date followUpDate;
    private int suspensionState;
    private String tenantId;
    private boolean isIdentityLinksInitialized;
    private String executionId;
    private String processInstanceId;
    private String caseExecutionId;
    private String caseDefinitionId;
    private String taskDefinitionkey;
    private boolean isDeleted;
    private String deleteReason;
    private String eventName;
    private boolean isFormKeyInitialized;
    private String formKey;
}
