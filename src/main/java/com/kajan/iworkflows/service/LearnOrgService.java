package com.kajan.iworkflows.service;

import com.kajan.iworkflows.model.UserStore;

import java.security.Principal;

public interface LearnOrgService {
    UserStore getLearnOrgUserInfo(Principal principal);
}