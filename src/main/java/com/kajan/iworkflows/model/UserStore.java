package com.kajan.iworkflows.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStore {
//    private Long id;
    private String firstName;
    private String userID;
    private String faculty;
    private String department;
    private String role;
    private String primaryEmail;
    private String permanatTelephone;
    private String currentHomeTelephone;
    private String currentAddress;
    private int casual;
    private int medical;
    private int vacation;
    private int casualAllowed;
    private int medicalAllowed;
    private int vacationAllowed;

}
