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
    private String comments;
    private int takenCasualLeaves;
    private int takenMedicalLeaves;
    private int takenVacationLeaves;
    private List<String> documents;
    private String submittedDate;
    private int remainingCasualLeaves;
    private int remainingMedicalLeaves;
    private int remainingVacationLeaves;

    public int getRemainingLeavesByLeaveType(String leaveType) {
        if (leaveType == "casual") {
            return getRemainingCasualLeaves();
        } else if (leaveType == "medical") {
            return getRemainingMedicalLeaves();
        } else {
            return getRemainingVacationLeaves();

        }
    }
}
