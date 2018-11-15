package com.kajan.iworkflows.workflow;

import com.kajan.iworkflows.model.LeaveFormData;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/forms/")
@Slf4j
public class CamundaFormController {

    private final DummyUserStoreServiceImpl storeService;
    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;

    @Autowired
    public CamundaFormController(DummyUserStoreServiceImpl storeService,OauthTokenService oauthTokenService, RestTemplate restTemplate)
    {
        this.storeService = storeService;
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("leave-form")
    public LeaveFormData leaveForm(Principal principal) {
//        List<DummyUserStore> userStoreList = new ArrayList<>();
//
//        storeService.findByPrincipal(principal.getName()).forEach(userStoreList::add);
//        DummyUserStore userStore = userStoreList.get(0);

        LearnOrgServiceImpl userInfo = new LearnOrgServiceImpl(oauthTokenService, restTemplate);
        UserStore userStore = new UserStore(userInfo.getLearnOrgUserInfo(principal));

        LeaveFormData form = new LeaveFormData();

//        form.setId(userStore.getId());
        form.setPrincipal(userStore.getFistName());
        form.setEmployeeId(userStore.getEmployeeId());
        form.setFaculty(userStore.getFaculty());
        form.setDepartment(userStore.getDepartment());
        form.setRole(userStore.getRole());
        form.setEmail(userStore.getEmail());
        form.setMobileNo(userStore.getMobileNo());
        form.setTelephoneNo(userStore.getTelephoneNo());
        form.setAddress(userStore.getAddress());
        form.setCasual(userStore.getCasual());
        form.setMedical(userStore.getMedical());
        form.setVacation(userStore.getVacation());

        return form;
    }
}
