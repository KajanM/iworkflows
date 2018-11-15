package com.kajan.iworkflows.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaveFormData {
    private Long id;
    private String principal;
    private String employeeId;
    private String faculty;
    private String department;
    private String role;
    private String email;
    private String mobileNo;
    private String telephoneNo;
    private String address;
    private int casual;
    private int medical;
    private int vacation;
}
