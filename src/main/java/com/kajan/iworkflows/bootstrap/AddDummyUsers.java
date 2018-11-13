package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AddDummyUsers {

    @Bean
    CommandLineRunner initDatabase(DummyUserStoreRepository userStoreRepository) {
        return args -> {
            log.debug("Preloading " + userStoreRepository.save(new DummyUserStore("kasthuri", "140379F", "Engineering", "CSE", "Student",
                    "kasthuri.14@cse.mrt.ac.lk", "077-9577217", 3, 4, 1)));
            log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("kajan", "140709U", "Engineering", "CSE", "Student",
                    "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
            log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("ramiya", "140709U", "Engineering", "CSE", "Student",
                    "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
            log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("kirisanth", "140709U", "Engineering", "CSE", "Student",
                    "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
            log.debug("Preloading" + userStoreRepository.save(new DummyUserStore("shadhini", "140709U", "Engineering", "CSE", "Student",
                    "kajan.14@cse.mrt.ac.lk", "077-0774946", 2, 0, 6)));
        };
    }

}
