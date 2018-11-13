package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.Mapper;
import com.kajan.iworkflows.model.mock.DummyUserStore;
import com.kajan.iworkflows.repository.DummyUserStoreRepository;
import com.kajan.iworkflows.repository.MapperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
/**
 * Created by User on 11/12/2018.
 */
public class AddRoles {
    @Bean
    CommandLineRunner initDatabase(MapperRepository mapperRepository) {
        return args -> {
            log.debug("Preloading " + mapperRepository.save(new Mapper("ROLE_CSE", "cse")));
            log.debug("Preloading" + mapperRepository.save(new Mapper("ROLE_CIVIL","civil")));
        };
    }

}
