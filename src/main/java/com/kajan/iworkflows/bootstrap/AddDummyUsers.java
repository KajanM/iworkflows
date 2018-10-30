package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.ApplicationUser;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.ApplicationUserRepository;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AddDummyUsers implements ApplicationListener<ContextRefreshedEvent> {

    DummyUserStoreRepository userStoreRepository;
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    public AddDummyUsers(DummyUserStoreRepository userStoreRepository,
                         ApplicationUserRepository applicationUserRepository) {
        this.userStoreRepository = userStoreRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initUserData();
    }

    private void initUserData() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userStoreRepository.save(new DummyUserStore("Kasthuri", "140379F", "Engineering", "CSE", "Student",
                "kasthuri.14@cse.mrt.ac.lk", "077-9577217", 3, 4, 1));
        userStoreRepository.save(new DummyUserStore("kajan", "140709U", "Engineering", "CSE", "Student",
                "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6));

        Stream.of("kasthuri.14@cse.mrt.ac.lk", "kajan.14@cse.mrt.ac.lk", "kirisanth.14@cse.mrt.ac.lk", "ramiya.14@cse.mrt.ac.lk").forEach(name -> {
            ApplicationUser user = new ApplicationUser();
            user.setUsername(name);
            user.setPassword(bCryptPasswordEncoder.encode("password"));
            applicationUserRepository.save(user);
        });
        applicationUserRepository.findAll().forEach(System.out::println);
    }

}
