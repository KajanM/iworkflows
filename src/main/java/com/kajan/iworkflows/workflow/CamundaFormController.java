package com.kajan.iworkflows.workflow;

import com.kajan.iworkflows.model.LeaveFormData;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import com.kajan.iworkflows.service.impl.NextcloudServiceImpl;
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
    private final LearnOrgServiceImpl learnOrgService;

    @Autowired
    public CamundaFormController(DummyUserStoreServiceImpl storeService,OauthTokenService oauthTokenService, RestTemplate restTemplate, LearnOrgServiceImpl learnOrgService)
    {
        this.storeService = storeService;
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
        this.learnOrgService = learnOrgService;
    }

    @GetMapping("leave-form")
    public LeaveFormData leaveForm(Principal principal) {
//        List<DummyUserStore> userStoreList = new ArrayList<>();
//        storeService.findByPrincipal(principal.getName()).forEach(userStoreList::add);
//        DummyUserStore userStore = userStoreList.get(0);


        UserStore userStore = learnOrgService.getLearnOrgUserInfo(principal);
        LeaveFormData form = LeaveFormData.fromUserStore(userStore);
        return form;
    }
}
