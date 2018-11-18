package com.kajan.iworkflows.model;

import com.kajan.iworkflows.model.mock.DummyUserStore;
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
        data.setEmployeeId(userStore.getEmployeeId());
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

    public static LeaveFormData fromDummyUserStore(DummyUserStore userStore) {
        LeaveFormData data = new LeaveFormData();
        data.setPrincipal(userStore.getPrincipal());
        data.setEmployeeId(userStore.getEmployeeId());
        data.setFaculty(userStore.getFaculty());
        data.setDepartment(userStore.getDepartment());
        data.setRole(userStore.getRole());
        data.setEmail(userStore.getEmail());
        data.setMobileNo(userStore.getMobileNo());
        data.setTelephoneNo("0214568973");
        data.setAddress("Vavuniya");
        data.setCasual(5);
        data.setMedical(7);
        data.setVacation(8);
        data.setCasualAllowed(21);
        data.setMedicalAllowed(20);
        data.setVacationAllowed(30);
        return data;
    }
}
