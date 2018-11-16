package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MyTaskFullDetails implements Serializable {
    private SubmittedLeaveFormDetails leaveFormDetails;
}
