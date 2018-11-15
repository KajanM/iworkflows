package com.kajan.iworkflows.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.security.Principal;

public interface LearnOrgService {
    JsonNode getLearnOrgUserInfo(Principal principal);
}
