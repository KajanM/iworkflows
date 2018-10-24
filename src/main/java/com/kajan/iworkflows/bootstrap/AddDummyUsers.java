package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AddDummyUsers implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    DummyUserStoreRepository userStoreRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initUserData();
    }

    private void initUserData() {
        userStoreRepository.save(new DummyUserStore("Kasthuri", "140379F", "Engineering", "CSE", "Student",
                "kasthuri.14@cse.mrt.ac.lk", "077-9577217", 3, 4, 1));
        userStoreRepository.save(new DummyUserStore("kajan", "140709U", "Engineering", "CSE", "Student",
                "kajan.14@cse.mrt.ac.lk", "077-0774946", 2,0,6));
    }
}
