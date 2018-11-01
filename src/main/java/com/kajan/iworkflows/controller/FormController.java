package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.model.LeaveFormData;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormController {

    private final DummyUserStoreServiceImpl storeService;

    @Autowired
    public FormController(DummyUserStoreServiceImpl storeService) {
        this.storeService = storeService;
    }

    @GetMapping("leave-form")
    public LeaveFormData leaveForm(Principal principal) {
        List<DummyUserStore> userStoreList = new ArrayList<>();

        storeService.findByPrincipal(principal.getName()).forEach(userStoreList::add);
        DummyUserStore userStore = userStoreList.get(0);
        LeaveFormData form = new LeaveFormData();

        form.setId(userStore.getId());
        form.setPrincipal(userStore.getPrincipal());
        form.setEmployeeId(userStore.getEmployeeId());
        form.setFaculty(userStore.getFaculty());
        form.setDepartment(userStore.getDepartment());
        form.setRole(userStore.getRole());
        form.setEmail(userStore.getEmail());
        form.setMobileNo(userStore.getMobileNo());
        form.setCasual(userStore.getCasual());
        form.setMedical(userStore.getMedical());
        form.setVacation(userStore.getVacation());

        return form;
    }
}
