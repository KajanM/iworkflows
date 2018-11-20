package com.kajan.iworkflows.workflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class SubmittedLeaveFormDetails implements Serializable {
    private String employeeId;
    private String employeeName;
    private String faculty;
    private String department;
    private String role;
    private String address;
    private String email;
    private String mobileNo;
    private String telephoneNo;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int takenCasualLeaves;
    private int takenMedicalLeaves;
    private List<String> documents;
}
