package com.kajan.iworkflows.bootstrap;

import com.kajan.iworkflows.model.GroupMapper;
import com.kajan.iworkflows.repository.GroupMapperRepository;
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
    CommandLineRunner initDatabase(GroupMapperRepository mapperRepository) {
        return args -> {
            log.debug("Preloading " + mapperRepository.save(new GroupMapper("ROLE_CSE", "cse")));
            log.debug("Preloading" + mapperRepository.save(new GroupMapper("ROLE_CIVIL","civil")));
        };
    }

}
