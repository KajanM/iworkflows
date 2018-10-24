package com.kajan.iworkflows.model.mock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DummyUserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String principal;
    private String employeeId;
    private String faculty;
    private String department;
    private String role;
    private String email;
    private String mobileNo;
    private int casual;
    private int medical;
    private int vacation;

    public DummyUserStore() {
    }

    public DummyUserStore(String principal, String employeeId, String faculty, String department, String role, String email, String mobileNo, int casual, int medical, int vacation) {
        this.principal = principal;
        this.employeeId = employeeId;
        this.faculty = faculty;
        this.department = department;
        this.role = role;
        this.email = email;
        this.mobileNo = mobileNo;
        this.casual = casual;
        this.medical = medical;
        this.vacation = vacation;
    }


    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getCasual() {
        return casual;
    }

    public void setCasual(int casual) {
        this.casual = casual;
    }

    public int getMedical() {
        return medical;
    }

    public void setMedical(int medical) {
        this.medical = medical;
    }

    public int getVacation() {
        return vacation;
    }

    public void setVacation(int vacation) {
        this.vacation = vacation;
    }


    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


    @Override
    public String toString() {
        return "DummyUserStore{" +
                "id=" + id +
                ", principal='" + principal + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", faculty='" + faculty + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", casual=" + casual +
                ", medical=" + medical +
                ", vacation=" + vacation +
                '}';
    }
}