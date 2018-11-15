package com.kajan.iworkflows.model;

import com.fasterxml.jackson.databind.JsonNode;

import static com.kajan.iworkflows.util.Constants.*;

public class UserStore {
    private Long id;
    private String fistName;
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

    public UserStore(JsonNode user) {
        this.fistName = user.path(FIRSTNAME_KEY).asText();
        this.employeeId = user.path(EMPLYEEID_KEY).asText();
        this.faculty = user.path(FACULTY_KEY).asText();
        this.department = user.path(DEPARTMENT_KEY).asText();
        this.email = user.path(EMAIL_KEY).asText();
        this.mobileNo = user.path(MOBILE_KEY).asText();
        this.telephoneNo = user.path(TELEPHONE_KEY).asText();
        this.address = user.path(ADDRESS_KEY).asText();
        this.casual = user.path(CASUAL_KEY).asInt();
        this.medical = user.path(MEDICAL_KEY).asInt();
        this.vacation = user.path(VACATION_KEY).asInt();
        this.role = "Student";
    }

    public Long getId() {
        return id;
    }
    public String getEmployeeId() {return this.employeeId;}
    public String getFistName() {
        return this.fistName;
    }
    public String getFaculty() {
        return this.faculty;
    }
    public String getDepartment() {
        return this.department;
    }
    public String getRole() {
        return this.role;
    }
    public String getEmail() {
        return this.email;
    }
    public String getMobileNo() {
        return this.mobileNo;
    }
    public String getTelephoneNo() { return this.telephoneNo;}
    public String getAddress() {return  this.address;}
    public int getCasual() {
        return this.casual;
    }
    public int getMedical(){return this.medical;}
    public int getVacation(){return this.vacation;}
}
