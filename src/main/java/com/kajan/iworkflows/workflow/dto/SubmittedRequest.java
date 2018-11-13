package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class SubmittedRequest implements Serializable {

    private String taskId;
    private String processInstanceId;
    private String type;
    private Date submittedDate;
    private Date dueDate;
    private String assignee;
}
