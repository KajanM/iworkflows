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

    @SuppressWarnings("Duplicates")
    @Override
    public void run(String... args) {

        DummyUserStore userStore = new DummyUserStore();
        userStore.setPrincipal("kasthuri");
        userStore.setEmployeeId("140379F");
        userStore.setFaculty("Engineering");
        userStore.setDepartment("CSE");
        userStore.setRole("Student");
        userStore.setEmail("kasthuri.14@cse.mrt.ac.lk");
        userStore.setMobileNo("077-9577217");
        userStore.setCasual(5);
        userStore.setMedical(3);
        userStore.setVacation(10);
        log.debug("Preloading ", userStoreRepository.save(userStore));

        userStore = new DummyUserStore();
        userStore.setPrincipal("kajan");
        userStore.setEmployeeId("140709U");
        userStore.setFaculty("Engineering");
        userStore.setDepartment("CSE");
        userStore.setRole("Student");
        userStore.setEmail("kajan.14@cse.mrt.ac.lk");
        userStore.setMobileNo("077-0774946");
        userStore.setCasual(5);
        userStore.setMedical(3);
        userStore.setVacation(10);
        log.debug("Preloading ", userStoreRepository.save(userStore));

        userStore.setPrincipal("ramiya");
        userStore.setEmployeeId("140494D");
        userStore.setFaculty("Engineering");
        userStore.setDepartment("CSE");
        userStore.setRole("Student");
        userStore.setEmail("ramiya.14@cse.mrt.ac.lk");
        userStore.setMobileNo("077-9577217");
        userStore.setCasual(5);
        userStore.setMedical(3);
        userStore.setVacation(10);
        log.debug("Preloading ", userStoreRepository.save(userStore));

        userStore.setPrincipal("kirisanth");
        userStore.setEmployeeId("140494D");
        userStore.setFaculty("Engineering");
        userStore.setDepartment("CSE");
        userStore.setRole("Student");
        userStore.setEmail("kirisanth.14@cse.mrt.ac.lk");
        userStore.setMobileNo("077-9577217");
        userStore.setCasual(5);
        userStore.setMedical(3);
        userStore.setVacation(10);
        log.debug("Preloading ", userStoreRepository.save(userStore));
    }

    @Autowired
    public void setUserStoreRepository(DummyUserStoreRepository userStoreRepository) {
        this.userStoreRepository = userStoreRepository;
    }
}
