package com.kajan.iworkflows.model.mock;

import com.kajan.iworkflows.model.UserStore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Exists for testing purpose only
 * since LearnOrg is only available in university network
 */
@Entity
@Data
@NoArgsConstructor
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
    private int remainingCasualLeaves;
    private int remainingMedicalLeaves;
    private int remainingVacationLeaves;

    public static UserStore toUserStore(DummyUserStore dummyUserStore) {
        UserStore userStore = new UserStore();
        userStore.setFirstName(dummyUserStore.getPrincipal());
        userStore.setEmployeeId(dummyUserStore.getEmployeeId());
        userStore.setFaculty(dummyUserStore.getFaculty());
        userStore.setDepartment(dummyUserStore.getDepartment());
        userStore.setRole(dummyUserStore.getRole());
        userStore.setPrimaryEmail(dummyUserStore.getEmail());
        userStore.setPermanatTelephone("0215489765");
        userStore.setCurrentMobile(dummyUserStore.getMobileNo());
        userStore.setCurrentAddress("Colombo");
        userStore.setCasual(dummyUserStore.getCasual());
        userStore.setMedical(dummyUserStore.getMedical());
        userStore.setVacation(dummyUserStore.getVacation());
        userStore.setRemainingCasualLeaves(7);
        userStore.setRemainingMedicalLeaves(14);
        userStore.setRemainingVacationLeaves(21);

        return userStore;
    }

}