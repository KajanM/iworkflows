package com.kajan.iworkflows.config;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Value("${iworkflows.credentials.nextcloud.username}")
    private String nextCloudUsername;

    @Value("${iworkflows.credentials.nextcloud.password}")
    private String nextcloudPassword;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Sardine iworkflowsWebDavClient() {
        return SardineFactory.begin(nextCloudUsername, nextcloudPassword);
    }
}
