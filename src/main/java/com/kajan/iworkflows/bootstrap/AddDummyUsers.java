package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "testing", havingValue = "true")
@Slf4j
public class AddDummyUsers implements CommandLineRunner {

    private DummyUserStoreRepository userStoreRepository;

    @Override
    public void run(String... args) throws Exception {

        log.debug("Preloading " + userStoreRepository.save(new DummyUserStore("kasthuri", "140379F", "Engineering", "CSE", "Student",
                "kasthuri.14@cse.mrt.ac.lk", "077-9577217", 3, 4, 1)));
        log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("kajan", "140709U", "Engineering", "CSE", "Student",
                "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
        log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("ramiya", "140494D", "Engineering", "CSE", "Student",
                "ramiya.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
        log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("kirisanth", "140306G", "Engineering", "CSE", "Student",
                "kirisanth.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
        log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("shadhini", "140512V", "Engineering", "CSE", "Student",
                "shadhini.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));

    }

    @Autowired
    public void setUserStoreRepository(DummyUserStoreRepository userStoreRepository) {
        this.userStoreRepository = userStoreRepository;
    }
}
