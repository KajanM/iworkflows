package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/forms")
public class FormController {

    @Autowired
    private DummyUserStoreServiceImpl storeService;

    @GetMapping({"leave-form", "leave-form.html"})
    public String leaveForm(Principal principal, Model model) {
        List<DummyUserStore> userStoreList = new ArrayList<>();

        storeService.findByPrincipal(principal.getName()).forEach(userStoreList::add);
        model.addAttribute("employeeId", userStoreList.get(0).getEmployeeId());
        model.addAttribute("principal", userStoreList.get(0).getPrincipal());
        model.addAttribute("faculty", userStoreList.get(0).getFaculty());
        model.addAttribute("department", userStoreList.get(0).getDepartment());
        model.addAttribute("role", userStoreList.get(0).getRole());
        model.addAttribute("email", userStoreList.get(0).getEmail());
        model.addAttribute("mobileNo", userStoreList.get(0).getMobileNo());
        model.addAttribute("casual", userStoreList.get(0).getCasual());
        model.addAttribute("medical", userStoreList.get(0).getMedical());
        model.addAttribute("vacation", userStoreList.get(0).getVacation());
        return "leave-form";
    }
}
