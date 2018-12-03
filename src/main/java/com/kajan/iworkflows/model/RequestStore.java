package com.kajan.iworkflows.model;

import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data

public class RequestStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String principal;
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
    private int casual;
    private int medical;
    private int vacation;
    @ElementCollection
    private List<String> documents;
    private String submittedDate;
    private int remainingCasual;
    private int remainingMedical;
    private int remainingVacation;
    private String status;
    private String rejectedComment;

    public RequestStore(SubmittedLeaveFormDetails submittedLeaveFormDetails, String principal, String status) {

//        data.setId(userStore.getId());
        this.principal = principal;
        this.setEmployeeName(submittedLeaveFormDetails.getEmployeeName());
        this.setEmployeeId(submittedLeaveFormDetails.getEmployeeId());
        this.setFaculty(submittedLeaveFormDetails.getFaculty());
        this.setDepartment(submittedLeaveFormDetails.getDepartment());
        this.setRole(submittedLeaveFormDetails.getRole());
        this.setEmail(submittedLeaveFormDetails.getEmail());
        this.setMobileNo(submittedLeaveFormDetails.getMobileNo());
        this.setTelephoneNo(submittedLeaveFormDetails.getTelephoneNo());
        this.setAddress(submittedLeaveFormDetails.getAddress());
        this.setCasual(submittedLeaveFormDetails.getCasual());
        this.setMedical(submittedLeaveFormDetails.getMedical());
        this.setVacation(submittedLeaveFormDetails.getVacation());
        this.setRemainingCasual(submittedLeaveFormDetails.getRemainingCasual());
        this.setRemainingVacation(submittedLeaveFormDetails.getRemainingVacation());
        this.setRemainingMedical(submittedLeaveFormDetails.getRemainingMedical());
        this.setLeaveType(submittedLeaveFormDetails.getLeaveType());
        this.setStartDate(submittedLeaveFormDetails.getStartDate());
        this.setEndDate(submittedLeaveFormDetails.getEndDate());
        this.setSubmittedDate(submittedLeaveFormDetails.getSubmittedDate());
        this.setDocuments(submittedLeaveFormDetails.getDocuments());
        this.setComments(submittedLeaveFormDetails.getComments());
        this.setStatus(status);
        this.setRejectedComment(submittedLeaveFormDetails.getRejectedComment());

    }
}
