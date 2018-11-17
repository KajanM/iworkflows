package com.kajan.iworkflows.model;

import com.sun.javaws.exceptions.InvalidArgumentException;
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
    private int casualAllowed;
    private int medicalAllowed;
    private int vacationAllowed;

    public static LeaveFormData fromUserStore(UserStore userStore) {
        LeaveFormData data = new LeaveFormData();
//        data.setId(userStore.getId());
        data.setPrincipal(userStore.getFirstName());
        data.setEmployeeId(userStore.getUserID());
        data.setFaculty(userStore.getFaculty());
        data.setDepartment(userStore.getDepartment());
        data.setRole(userStore.getRole());
        data.setEmail(userStore.getPrimaryEmail());
        data.setMobileNo(userStore.getPermanatTelephone());
        data.setTelephoneNo(userStore.getCurrentHomeTelephone());
        data.setAddress(userStore.getCurrentAddress());
        data.setCasual(userStore.getCasual());
        data.setMedical(userStore.getMedical());
        data.setVacation(userStore.getVacation());
        data.setCasualAllowed(userStore.getCasualAllowed());
        data.setMedicalAllowed(userStore.getMedicalAllowed());
        data.setVacationAllowed(userStore.getVacationAllowed());
        return data;
    }
}
