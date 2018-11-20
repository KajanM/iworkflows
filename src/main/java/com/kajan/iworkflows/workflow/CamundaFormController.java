package com.kajan.iworkflows.workflow;

import com.kajan.iworkflows.model.LeaveFormData;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.service.impl.DummyUserStoreServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("api/v1/forms/")
public class CamundaFormController {

    private final DummyUserStoreServiceImpl storeService;
    private final LearnOrgServiceImpl learnOrgService;

    @Value("${testing}")
    private Boolean testing;

    @Autowired
    public CamundaFormController(DummyUserStoreServiceImpl storeService, LearnOrgServiceImpl learnOrgService) {
        this.storeService = storeService;
        this.learnOrgService = learnOrgService;
    }

    @GetMapping("leave-form")
    public LeaveFormData leaveForm(Principal principal) {
        LeaveFormData form;
        if (testing) {
            //since learnorg can only be accessed via uni wifi :(
            List<DummyUserStore> userStoreList = new ArrayList<>();

            storeService.findByPrincipal(principal.getName()).forEach(userStoreList::add);
            DummyUserStore userStore = userStoreList.get(0);
            form = LeaveFormData.fromDummyUserStore(userStore);
        } else {
            UserStore userStore = learnOrgService.getLearnOrgUserInfo(principal);
            form = LeaveFormData.fromUserStore(userStore);
        }

        return form;
    }
}
