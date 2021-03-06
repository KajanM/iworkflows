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
    private String rejectedComment;
    /**
     * taken casual leaves
     */
    private int casual;
    /**
     * taken medical leaves
     */
    private int medical;
    /**
     * taken vacation leaves
     */
    private int vacation;
    private List<String> documents;
    private String submittedDate;
    private int remainingCasual;
    private int remainingMedical;
    private int remainingVacation;

    public int getRemainingLeavesByLeaveType(String leaveType) {
        switch (leaveType) {
            case "Casual":
                return getRemainingCasual();
            case "Medical":
                return getRemainingMedical();
            default:
                return getRemainingVacation();

        }
    }
}
