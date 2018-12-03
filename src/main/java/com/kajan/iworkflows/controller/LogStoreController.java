package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.service.impl.LogStoreServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Slf4j
public class LogStoreController {
    private LogStoreServiceImpl logStoreService;

    @GetMapping("logs")
    public List<LogStore> user(Principal user) {
        List<LogStore> logStoreList = new ArrayList<>();
        LogStore logStore;
        logStoreService.findByPrincipal(user.getName()).forEach(logStoreList::add);
        return logStoreList;
    }

    @Autowired
    public void setLogStoreService(LogStoreServiceImpl logStoreService) {
        this.logStoreService = logStoreService;
    }
}
